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
    fun provideDataStoreTypeUseCase(mainRepoInterface: MainRepoInterface): DataStoreTypeUseCase{
        return DataStoreTypeUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetUserIdFromDataStoreUseCase(mainRepoInterface: MainRepoInterface): GetUserIdFromDataStoreUseCase{
        return GetUserIdFromDataStoreUseCase(mainRepoInterface)
    }

    @Provides
    fun provideLoginUseCase(mainRepoInterface: MainRepoInterface): PostLoginUserUseCase{
        return PostLoginUserUseCase(mainRepoInterface)
    }

    @Provides
    fun provideLogoutUseCase(mainRepoInterface: MainRepoInterface): LogoutUseCase{
        return LogoutUseCase(mainRepoInterface)
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

    @Provides
    fun provideGetProfileDataAndUpdateDataStoreUseCase(mainRepoInterface: MainRepoInterface): GetProfileDataAndUpdateDataStoreUseCase{
        return GetProfileDataAndUpdateDataStoreUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetMySeniorsUseCase(mainRepoInterface: MainRepoInterface): GetMySeniorsUseCase{
        return GetMySeniorsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideAddNewSeniorUseCase(mainRepoInterface: MainRepoInterface): AddNewSeniorUseCase{
        return AddNewSeniorUseCase(mainRepoInterface)
    }

    @Provides
    fun provideDeleteSeniorUseCase(mainRepoInterface: MainRepoInterface): DeleteSeniorUseCase{
        return DeleteSeniorUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetSchedulesUseCase(mainRepoInterface: MainRepoInterface): GetSchedulesUseCase{
        return GetSchedulesUseCase(mainRepoInterface)
    }

    @Provides
    fun provideCancelScheduleUseCase(mainRepoInterface: MainRepoInterface): CancelScheduleUseCase{
        return CancelScheduleUseCase(mainRepoInterface)
    }

    @Provides
    fun provideAddNewScheduleUseCase(mainRepoInterface: MainRepoInterface): AddNewScheduleUseCase{
        return AddNewScheduleUseCase(mainRepoInterface)
    }

    @Provides
    fun provideSendNotificationUseCase(mainRepoInterface: MainRepoInterface): SendNotificationUseCase{
        return SendNotificationUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetInformationCategoriesUseCase(mainRepoInterface: MainRepoInterface): GetInformationCategoriesUseCase{
        return GetInformationCategoriesUseCase(mainRepoInterface)
    }
    @Provides
    fun provideDeleteInformationCategoryUseCase(mainRepoInterface: MainRepoInterface): DeleteInformationCategoryUseCase{
        return DeleteInformationCategoryUseCase(mainRepoInterface)
    }

    @Provides
    fun provideEditInformationCategoryTitleUseCase(mainRepoInterface: MainRepoInterface): EditInformationCategoryTitleUseCase{
        return EditInformationCategoryTitleUseCase(mainRepoInterface)
    }

    @Provides
    fun provideAddNewCategoryUseCase(mainRepoInterface: MainRepoInterface): AddNewCategoryUseCase{
        return AddNewCategoryUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetCategoryDetailsUseCase(mainRepoInterface: MainRepoInterface): GetCategoryDetailsUseCase{
        return GetCategoryDetailsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideDeleteCategoryDetailsUseCase(mainRepoInterface: MainRepoInterface): DeleteCategoryDetailsUseCase{
        return DeleteCategoryDetailsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideEditCategoryDetailsUseCase(mainRepoInterface: MainRepoInterface): EditCategoryDetailsUseCase{
        return EditCategoryDetailsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideAddNewCategoryDetailsUseCase(mainRepoInterface: MainRepoInterface): AddNewCategoryDetailsUseCase{
        return AddNewCategoryDetailsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetBookingsDataUseCase(mainRepoInterface: MainRepoInterface): GetBookingsDataUseCase{
        return GetBookingsDataUseCase(mainRepoInterface)
    }

    @Provides
    fun provideCancelBookingUseCase(mainRepoInterface: MainRepoInterface): CancelBookingUseCase{
        return CancelBookingUseCase(mainRepoInterface)
    }

    @Provides
    fun provideCheckCodeUseCase(mainRepoInterface: MainRepoInterface): CheckCodeUseCase{
        return CheckCodeUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetAllUsersUseCase(mainRepoInterface: MainRepoInterface): GetAllUsersUseCase{
        return GetAllUsersUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetChatsUseCase(mainRepoInterface: MainRepoInterface): GetChatsUseCase{
        return GetChatsUseCase(mainRepoInterface)
    }

    @Provides
    fun provideSendMessageUseCase(mainRepoInterface: MainRepoInterface): SendMessageUseCase{
        return SendMessageUseCase(mainRepoInterface)
    }

    @Provides
    fun provideGetMedicinesOfBookingUseCase(mainRepoInterface: MainRepoInterface): GetMedicinesOfBookingUseCase{
        return GetMedicinesOfBookingUseCase(mainRepoInterface)
    }

    @Provides
    fun provideAddNewMedicineUseCase(mainRepoInterface: MainRepoInterface): AddNewMedicineUseCase{
        return AddNewMedicineUseCase(mainRepoInterface)
    }

    @Provides
    fun provideDeleteMedicineUseCase(mainRepoInterface: MainRepoInterface): DeleteMedicineUseCase{
        return DeleteMedicineUseCase(mainRepoInterface)
    }

    @Provides
    fun provideUpdateMedicineUseCase(mainRepoInterface: MainRepoInterface): UpdateMedicineUseCase{
        return UpdateMedicineUseCase(mainRepoInterface)
    }
}