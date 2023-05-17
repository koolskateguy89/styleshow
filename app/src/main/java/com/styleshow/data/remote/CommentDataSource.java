package com.styleshow.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.styleshow.common.Constants;
import com.styleshow.data.remote.dto.CommentDto;
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class CommentDataSource {

    private final @NonNull CollectionReference mPostRef;
    private final @NonNull LoginDataSource mLoginDataSource;
    private final @NonNull UserProfileDataSource mUserProfileDataSource;

    private final Executor executor = Executors.newSingleThreadExecutor();

    public CommentDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull LoginDataSource loginDataSource,
            @NonNull UserProfileDataSource userProfileDataSource
    ) {
        mPostRef = firestore.collection(Constants.Post.COLLECTION_NAME);
        mLoginDataSource = loginDataSource;
        mUserProfileDataSource = userProfileDataSource;
    }

    public Task<List<Comment>> getCommentsForPost(@NonNull String postId) {
        var commentsRef = mPostRef.document(postId)
                .collection(Constants.Comment.COLLECTION_NAME);

        var commentDtosTask = commentsRef
                .orderBy(Constants.Comment.FIELD_POSTED_AT, Query.Direction.DESCENDING) // newest first
                .get()
                .continueWith(executor, task -> {
                    if (!task.isSuccessful())
                        return null;

                    var querySnapshot = task.getResult();

                    return querySnapshot.toObjects(CommentDto.class);
                });

        // Combines profiles and comments
        return commentDtosTask.continueWithTask(executor, task -> {
                    if (!task.isSuccessful())
                        return Tasks.forException(task.getException());

                    var commentDtos = task.getResult();

                    if (commentDtos.isEmpty())
                        return Tasks.forResult(List.<Comment>of());

                    var uids = commentDtos.stream()
                            .map(commentDto -> commentDto.uid)
                            .collect(Collectors.toList());

                    // Get the profiles of the commenters
                    return mUserProfileDataSource.getProfilesForUids(uids)
                            .continueWith(executor, task1 -> {
                                if (!task1.isSuccessful())
                                    return null;

                                var profiles = task1.getResult();

                                return profiles.stream()
                                        .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
                            })
                            .continueWith(executor, task1 -> {
                                if (!task1.isSuccessful())
                                    return null;

                                var profilesMap = task1.getResult();

                                return commentDtos.stream()
                                        .map(commentDto -> commentDto.toComment(profilesMap.get(commentDto.uid)))
                                        .collect(Collectors.toList());
                            });
                }).addOnSuccessListener(comments -> {
                    Timber.d("Got %d comments for post %s", comments.size(), postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to get comments for post %s", postId);
                });
    }

    public Task<?> postComment(@NonNull String postId, @NonNull String content) {
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.Comment.FIELD_POSTER_UID, currentUserId);
        data.put(Constants.Comment.FIELD_CONTENT, content);
        data.put(Constants.Comment.FIELD_POSTED_AT, FieldValue.serverTimestamp());

        return mPostRef.document(postId).collection(Constants.Comment.COLLECTION_NAME)
                .add(data)
                .addOnSuccessListener(a -> {
                    Timber.d("Added comment to post %s", postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to add comment to post %s", postId);
                })
                ;
    }

    public Task<Void> deleteComment(@NonNull String postId, @NonNull String commentId) {
        return mPostRef.document(postId).collection(Constants.Comment.COLLECTION_NAME)
                .document(commentId)
                .delete()
                .addOnSuccessListener(ignore -> {
                    Timber.d("Deleted comment %s from post %s", commentId, postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to delete comment %s from post %s", commentId, postId);
                });
    }
}
