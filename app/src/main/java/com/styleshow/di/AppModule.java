package com.styleshow.di;

import javax.inject.Singleton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.styleshow.data.remote.LoginDataSource;
import com.styleshow.data.repository.LoginRepositoryImpl;
import com.styleshow.domain.repository.LoginRepository;
import dagger.Binds;
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

    // TODO?: provide Firebase DatabaseReference
    //@Provides
    //@Singleton
    //DatabaseReference provideDatabaseReference() {
    //    return FirebaseDatabase.getInstance().getReference();
    //}

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
}
