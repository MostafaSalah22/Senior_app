package com.project.senior.bookingInformation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.CategoryData
import com.project.domain.model.InformationCategories
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetInformationCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingInformationViewModel @Inject constructor(
    private val getInformationCategoriesUseCase: GetInformationCategoriesUseCase
): ViewModel() {


    private val _getInformationCategoriesResponseState: MutableLiveData<Resource<InformationCategories>?> = MutableLiveData()
    val getInformationCategoriesResponseState: LiveData<Resource<InformationCategories>?>
    get() = _getInformationCategoriesResponseState

    private val _categoriesList: MutableLiveData<ArrayList<CategoryData>?> = MutableLiveData()
    val categoriesList: LiveData<ArrayList<CategoryData>?>
    get() = _categoriesList


    suspend fun getInformationCategories(userId: Int){
        try {
            _getInformationCategoriesResponseState.value = Resource.Loading()
            val categoriesResponse = getInformationCategoriesUseCase(userId)
            //_loginUser.value = user
            _getInformationCategoriesResponseState.value = getInformationCategoriesUseCase.handleResponse().value
            _categoriesList.value = categoriesResponse
        } catch (e: Exception){
            Log.e("BookingInformationViewModel",e.message.toString())
            _getInformationCategoriesResponseState.value = Resource.Error(e.message.toString())
        }
    }
}