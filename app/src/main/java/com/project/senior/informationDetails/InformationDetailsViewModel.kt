package com.project.senior.informationDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.CategoryDetails
import com.project.domain.model.CategoryDetailsData
import com.project.domain.model.MiniResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.DeleteCategoryDetailsUseCase
import com.project.domain.usecase.EditCategoryDetailsUseCase
import com.project.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationDetailsViewModel @Inject constructor(
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase,
    private val deleteCategoryDetailsUseCase: DeleteCategoryDetailsUseCase,
    private val editCategoryDetailsUseCase: EditCategoryDetailsUseCase
): ViewModel() {
    private val _getCategoryDetailsResponseState: MutableLiveData<Resource<CategoryDetails>?> = MutableLiveData()
    val getCategoryDetailsResponseState: LiveData<Resource<CategoryDetails>?>
    get() = _getCategoryDetailsResponseState

    private val _detailsList: MutableLiveData<ArrayList<CategoryDetailsData>?> = MutableLiveData()
    val detailsList: LiveData<ArrayList<CategoryDetailsData>?>
    get() = _detailsList

    private val _deleteCategoryDetailsResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val deleteCategoryDetailsResponseState: LiveData<Resource<MiniResponse>?>
    get() = _deleteCategoryDetailsResponseState

    private val _editCategoryDetailsResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val editCategoryDetailsResponseState: LiveData<Resource<MiniResponse>?>
    get() = _editCategoryDetailsResponseState

    suspend fun getCategoryDetails(categoryId: Int){
        try {
            _getCategoryDetailsResponseState.value = Resource.Loading()
            val detailsResponse = getCategoryDetailsUseCase(categoryId)
            _getCategoryDetailsResponseState.value = getCategoryDetailsUseCase.handleResponse().value
            _detailsList.value = detailsResponse
        } catch (e: Exception){
            Log.e("InformationDetailsViewModel",e.message.toString())
            _getCategoryDetailsResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun deleteCategoryDetails(categoryDetailsId: Int){
        try {
            _deleteCategoryDetailsResponseState.value = Resource.Loading()
            deleteCategoryDetailsUseCase(categoryDetailsId)
            _deleteCategoryDetailsResponseState.value = deleteCategoryDetailsUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("InformationDetailsViewModel",e.message.toString())
            _deleteCategoryDetailsResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun editCategoryDetails(categoryDetailsId: Int, title: String, description: String){
        try {
            _editCategoryDetailsResponseState.value = Resource.Loading()
            editCategoryDetailsUseCase(categoryDetailsId, title, description)
            _editCategoryDetailsResponseState.value = editCategoryDetailsUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("InformationDetailsViewModel",e.message.toString())
            _editCategoryDetailsResponseState.value = Resource.Error(e.message.toString())
        }
    }
}