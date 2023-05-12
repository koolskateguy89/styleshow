package com.styleshow.di;

import javax.inject.Singleton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.styleshow.data.remote.LoginDataSource;
import com.styleshow.data.remote.PostDataSource;
import com.styleshow.data.remote.UserProfileDataSource;
import com.styleshow.data.repository.LoginRepositoryImpl;
import com.styleshow.data.repository.PostRepositoryImpl;
import com.styleshow.data.repository.UserProfileRepositoryImpl;
import com.styleshow.domain.repository.LoginRepository;
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
    FirebaseStorage provideStorageReference() {
        return FirebaseStorage.getInstance();
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
            FirebaseFirestore firestore,
            FirebaseStorage storage,
            LoginDataSource loginDataSource,
            UserProfileDataSource userProfileDataSource
    ) {
        return new PostDataSource(firestore, storage, loginDataSource, userProfileDataSource);
    }

    @Provides
    @Singleton
    PostRepository providePostRepository(PostDataSource dataSource) {
        return new PostRepositoryImpl(dataSource);
    }

    @Provides
    @Singleton
    UserProfileDataSource provideUserProfileDataSource(FirebaseFirestore firestore) {
        return new UserProfileDataSource(firestore);
    }

    @Provides
    @Singleton
    UserProfileRepository provideUserProfileRepository(UserProfileDataSource dataSource) {
        return new UserProfileRepositoryImpl(dataSource);
    }
}
