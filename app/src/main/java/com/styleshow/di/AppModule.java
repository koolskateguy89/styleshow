package com.styleshow.di;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.styleshow.data.remote.ChatDataSource;
import com.styleshow.data.remote.CommentDataSource;
import com.styleshow.data.remote.LoginDataSource;
import com.styleshow.data.remote.PostDataSource;
import com.styleshow.data.remote.UserProfileDataSource;
import com.styleshow.data.repository.CommentRepositoryImpl;
import com.styleshow.data.repository.LoginRepositoryImpl;
import com.styleshow.data.repository.ChatRepositoryImpl;
import com.styleshow.data.repository.PostRepositoryImpl;
import com.styleshow.data.repository.UserProfileRepositoryImpl;
import com.styleshow.domain.repository.CommentRepository;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.ChatRepository;
import com.styleshow.domain.repository.PostRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    FirebaseStorage provideFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @Singleton
    FirebaseMessaging provideFirebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }

    @Provides
    @Singleton
    LoginDataSource provideLoginDataSource(FirebaseAuth auth) {
        return new LoginDataSource(auth);
    }

    @Provides
    @Singleton
    LoginRepository provideLoginRepository(LoginDataSource dataSource) {
        return new LoginRepositoryImpl(dataSource);
    }

    @Provides
    @Singleton
    PostDataSource providePostDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull FirebaseStorage storage,
            @NonNull LoginDataSource loginDataSource,
            @NonNull UserProfileDataSource userProfileDataSource
    ) {
        return new PostDataSource(
                firestore,
                storage,
                loginDataSource,
                userProfileDataSource
        );
    }

    @Provides
    @Singleton
    PostRepository providePostRepository(PostDataSource dataSource) {
        return new PostRepositoryImpl(dataSource);
    }

    @Provides
    @Singleton
    UserProfileDataSource provideUserProfileDataSource(
            @NonNull LoginDataSource loginDataSource,
            @NonNull FirebaseFirestore firestore) {
        return new UserProfileDataSource(loginDataSource, firestore);
    }

    @Provides
    @Singleton
    UserProfileRepository provideUserProfileRepository(UserProfileDataSource dataSource) {
        return new UserProfileRepositoryImpl(dataSource);
    }

    @Provides
    @Singleton
    CommentDataSource provideCommentDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull LoginDataSource loginDataSource,
            @NonNull UserProfileDataSource userProfileDataSource
    ) {
        return new CommentDataSource(firestore, loginDataSource, userProfileDataSource);
    }

    @Provides
    @Singleton
    CommentRepository provideCommentRepository(@NonNull CommentDataSource dataSource) {
        return new CommentRepositoryImpl(dataSource);
    }

    @Provides
    @Singleton
    ChatDataSource provideChatDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull FirebaseMessaging messaging
    ) {
        return new ChatDataSource(firestore, messaging);
    }

    @Provides
    @Singleton
    ChatRepository provideChatRepository(@NonNull ChatDataSource dataSource) {
        return new ChatRepositoryImpl(dataSource);
    }
}
