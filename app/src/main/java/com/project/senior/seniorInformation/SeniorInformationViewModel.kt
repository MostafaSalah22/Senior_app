package com.project.senior.seniorInformation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.model.CategoryData
import com.project.domain.model.InformationCategories
import com.project.domain.model.MiniResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.DeleteInformationCategoryUseCase
import com.project.domain.usecase.GetInformationCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeniorInformationViewModel @Inject constructor(
    private val getInformationCategoriesUseCase: GetInformationCategoriesUseCase,
    private val deleteInformationCategoryUseCase: DeleteInformationCategoryUseCase
): ViewModel() {

    private val _getInformationCategoriesResponseState: MutableLiveData<Resource<InformationCategories>?> = MutableLiveData()
    val getInformationCategoriesResponseState: LiveData<Resource<InformationCategories>?>
    get() = _getInformationCategoriesResponseState

    private val _categoriesList: MutableLiveData<ArrayList<CategoryData>?> = MutableLiveData()
    val categoriesList: LiveData<ArrayList<CategoryData>?>
    get() = _categoriesList

    private val _deleteInformationCategoryResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val deleteInformationCategoryResponseState: LiveData<Resource<MiniResponse>?>
    get() = _deleteInformationCategoryResponseState

    suspend fun getInformationCategories(userId: Int){
            try {
                _getInformationCategoriesResponseState.value = Resource.Loading()
                val categoriesResponse = getInformationCategoriesUseCase(userId)
                //_loginUser.value = user
                _getInformationCategoriesResponseState.value = getInformationCategoriesUseCase.handleResponse().value
                _categoriesList.value = categoriesResponse
            } catch (e: Exception){
                Log.e("SeniorInformationViewModel",e.message.toString())
                _getInformationCategoriesResponseState.value = Resource.Error(e.message.toString())
            }
    }

    suspend fun deleteInformationCategory(categoryId: Int){
        try {
            _deleteInformationCategoryResponseState.value = Resource.Loading()
            deleteInformationCategoryUseCase(categoryId)
            _deleteInformationCategoryResponseState.value = deleteInformationCategoryUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _deleteInformationCategoryResponseState.value = Resource.Error(e.message.toString())
        }
    }
}