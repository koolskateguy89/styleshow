package com.styleshow.di;

import javax.inject.Singleton;

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

    // TODO: provide firebase instance?

    @Provides
    @Singleton
    LoginDataSource provideLoginDataSource() {
        return new LoginDataSource();
    }

    @Provides
    @Singleton
    LoginRepository provideLoginRepository(LoginDataSource dataSource) {
        return new LoginRepositoryImpl(dataSource);
    }
}
