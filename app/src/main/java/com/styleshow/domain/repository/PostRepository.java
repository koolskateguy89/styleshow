package com.styleshow.domain.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;

public interface PostRepository {

    // TODO: getAllPosts (maybe paginated)

    Task<List<Post>> getPostsByUser(@NonNull String uid);

    Task<List<Post>> getPostsByUser(@NonNull UserProfile author);
}
