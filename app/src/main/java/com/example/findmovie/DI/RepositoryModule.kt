package com.example.findmovie.DI

import com.example.findmovie.data.repository.AuthRepository
import com.example.findmovie.data.repository.MoviesRepository
import com.example.findmovie.data.repository.UserMoviesRepository
import com.example.findmovie.data.repositoryimpl.AuthRepositoryImpl
import com.example.findmovie.data.repositoryimpl.MoviesRepositoryImpl
import com.example.findmovie.data.repositoryimpl.UserMoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule{
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun userMoviesRepository (impl: UserMoviesRepositoryImpl): UserMoviesRepository


}
