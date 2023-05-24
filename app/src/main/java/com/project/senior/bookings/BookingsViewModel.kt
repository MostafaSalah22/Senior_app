package com.project.senior.bookings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.BookingsData
import com.project.domain.model.BookingsDetails
import com.project.domain.model.InformationCategories
import com.project.domain.model.MiniResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.CancelBookingUseCase
import com.project.domain.usecase.CheckCodeUseCase
import com.project.domain.usecase.GetBookingsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getBookingsDataUseCase: GetBookingsDataUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val checkCodeUseCase: CheckCodeUseCase
): ViewModel() {

    private val _getBookingsDataResponseState: MutableLiveData<Resource<BookingsData>?> = MutableLiveData()
    val getBookingsDataResponseState: LiveData<Resource<BookingsData>?>
    get() = _getBookingsDataResponseState

    private val _bookingsList: MutableLiveData<ArrayList<BookingsDetails>?> = MutableLiveData()
    val bookingsList: LiveData<ArrayList<BookingsDetails>?>
    get() = _bookingsList

    private val _cancelBookingResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val cancelBookingResponseState: LiveData<Resource<MiniResponse>?>
    get() = _cancelBookingResponseState

    private val _checkCodeResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val checkCodeResponseState: LiveData<Resource<MiniResponse>?>
    get() = _checkCodeResponseState

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

    suspend fun cancelBooking(bookingId: Int){
        try {
            _cancelBookingResponseState.value = Resource.Loading()
            cancelBookingUseCase(bookingId)
            _cancelBookingResponseState.value = cancelBookingUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("BookingsViewModel",e.message.toString())
            _cancelBookingResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun checkCode(code: String, userId: Int){
        try {
            _checkCodeResponseState.value = Resource.Loading()
            checkCodeUseCase(code, userId)
            _checkCodeResponseState.value = checkCodeUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("BookingsViewModel",e.message.toString())
            //_checkCodeResponseState.value = Resource.Error(e.message.toString())
        }
    }
}