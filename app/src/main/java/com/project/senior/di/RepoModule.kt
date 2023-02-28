package com.project.senior.di

import android.content.Context
import com.project.data.remote.ApiService
import com.project.data.repo.DataStoreRepoImpl
import com.project.data.repo.MainRepoImpl
import com.project.domain.repo.DataStoreRepoInterface
import com.project.domain.repo.MainRepoInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideDataStoreRepo(@ApplicationContext app: Context) : DataStoreRepoInterface{
        return DataStoreRepoImpl(app)
    }

    @Provides
    fun provideRepo(apiService: ApiService, dataStoreRepoInterface: DataStoreRepoInterface): MainRepoInterface{
        return MainRepoImpl(apiService, dataStoreRepoInterface)
    }

}