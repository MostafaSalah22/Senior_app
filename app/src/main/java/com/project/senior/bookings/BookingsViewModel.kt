package com.project.senior.bookings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.BookingsData
import com.project.domain.model.BookingsDetails
import com.project.domain.model.InformationCategories
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetBookingsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getBookingsDataUseCase: GetBookingsDataUseCase
): ViewModel() {

    private val _getBookingsDataResponseState: MutableLiveData<Resource<BookingsData>?> = MutableLiveData()
    val getBookingsDataResponseState: LiveData<Resource<BookingsData>?>
    get() = _getBookingsDataResponseState

    private val _bookingsList: MutableLiveData<ArrayList<BookingsDetails>?> = MutableLiveData()
    val bookingsList: LiveData<ArrayList<BookingsDetails>?>
    get() = _bookingsList

    suspend fun getBookingsData(){
        try {
            _getBookingsDataResponseState.value = Resource.Loading()
            val bookingsResponse = getBookingsDataUseCase()
            _getBookingsDataResponseState.value = getBookingsDataUseCase.handleResponse().value
            _bookingsList.value = bookingsResponse
        } catch (e: Exception){
            Log.e("BookingsViewModel",e.message.toString())
            _getBookingsDataResponseState.value = Resource.Error(e.message.toString())
        }
    }
}