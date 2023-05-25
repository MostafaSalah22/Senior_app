package com.project.senior.bookingInformationDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.CategoryDetails
import com.project.domain.model.CategoryDetailsData
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetCategoryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingInformationDetailsViewModel @Inject constructor(
    private val getCategoryDetailsUseCase: GetCategoryDetailsUseCase
): ViewModel() {

    private val _getCategoryDetailsResponseState: MutableLiveData<Resource<CategoryDetails>?> = MutableLiveData()
    val getCategoryDetailsResponseState: LiveData<Resource<CategoryDetails>?>
    get() = _getCategoryDetailsResponseState

    private val _detailsList: MutableLiveData<ArrayList<CategoryDetailsData>?> = MutableLiveData()
    val detailsList: LiveData<ArrayList<CategoryDetailsData>?>
    get() = _detailsList


    suspend fun getCategoryDetails(categoryId: Int){
        try {
            _getCategoryDetailsResponseState.value = Resource.Loading()
            val detailsResponse = getCategoryDetailsUseCase(categoryId)
            _getCategoryDetailsResponseState.value = getCategoryDetailsUseCase.handleResponse().value
            _detailsList.value = detailsResponse
        } catch (e: Exception){
            Log.e("BookingInformationDetailsViewModel",e.message.toString())
            _getCategoryDetailsResponseState.value = Resource.Error(e.message.toString())
        }
    }
}