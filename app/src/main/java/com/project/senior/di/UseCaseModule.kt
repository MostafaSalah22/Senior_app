package com.project.senior.di

import com.project.domain.repo.MainRepoInterface
import com.project.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoginUseCase(mainRepoInterface: MainRepoInterface): PostLoginUserUseCase{
        return PostLoginUserUseCase(mainRepoInterface)
    }

    @Provides
    fun provideRegisterUseCase(mainRepoInterface: MainRepoInterface): PostRegisterUserUseCase{
        return PostRegisterUserUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetProfileUseCase(mainRepoInterface: MainRepoInterface): GetProfileDataUseCase{
        return GetProfileDataUseCase(mainRepoInterface)
    }

    @Provides
    fun provideChangeProfilePasswordUseCase(mainRepoInterface: MainRepoInterface): ChangeProfilePasswordUseCase{
        return ChangeProfilePasswordUseCase(mainRepoInterface)
    }

    @Provides
    fun provideChangeProfileImageUseCase(mainRepoInterface: MainRepoInterface): ChangeProfileImageUseCase{
        return ChangeProfileImageUseCase(mainRepoInterface)
    }

    @Provides
    fun provideUpdateProfileDataUseCase(mainRepoInterface: MainRepoInterface): UpdateProfileDataUseCase{
        return UpdateProfileDataUseCase(mainRepoInterface)
    }
}