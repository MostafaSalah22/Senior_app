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
import com.project.domain.usecase.AddNewCategoryUseCase
import com.project.domain.usecase.DeleteInformationCategoryUseCase
import com.project.domain.usecase.EditInformationCategoryTitleUseCase
import com.project.domain.usecase.GetInformationCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeniorInformationViewModel @Inject constructor(
    private val getInformationCategoriesUseCase: GetInformationCategoriesUseCase,
    private val deleteInformationCategoryUseCase: DeleteInformationCategoryUseCase,
    private val editInformationCategoryTitleUseCase: EditInformationCategoryTitleUseCase,
    private val addNewCategoryUseCase: AddNewCategoryUseCase
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

    private val _editInformationCategoryTitleResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val editInformationCategoryTitleResponseState: LiveData<Resource<MiniResponse>?>
    get() = _editInformationCategoryTitleResponseState

    private val _addNewCategoryResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val addNewCategoryResponseState: LiveData<Resource<MiniResponse>?>
    get() = _addNewCategoryResponseState

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

    suspend fun editInformationCategoryTitle(categoryId: Int, title: String){
        try {
            _editInformationCategoryTitleResponseState.value = Resource.Loading()
            editInformationCategoryTitleUseCase(categoryId, title)
            _editInformationCategoryTitleResponseState.value = editInformationCategoryTitleUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _editInformationCategoryTitleResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun addNewCategory(userId: Int, title: String){
        try {
            _addNewCategoryResponseState.value = Resource.Loading()
            addNewCategoryUseCase(userId, title)
            _addNewCategoryResponseState.value = addNewCategoryUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _addNewCategoryResponseState.value = Resource.Error(e.message.toString())
        }
    }
}