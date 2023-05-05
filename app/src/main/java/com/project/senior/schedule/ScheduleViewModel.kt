package com.project.senior.schedule

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.AppUser
import com.project.domain.model.MiniResponse
import com.project.domain.model.ScheduleData
import com.project.domain.model.SeniorSchedules
import com.project.domain.repo.Resource
import com.project.domain.usecase.AddNewScheduleUseCase
import com.project.domain.usecase.CancelScheduleUseCase
import com.project.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val cancelScheduleUseCase: CancelScheduleUseCase,
    private val addNewScheduleUseCase: AddNewScheduleUseCase
): ViewModel() {

    private val calendar = Calendar.getInstance()
    private var currentTime = calendar.time
    private val calendarEt = Calendar.getInstance()
    private var currentTimeEt = calendar.time
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val dateFormatApi = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val dayFormat = SimpleDateFormat("EEEE" , Locale.getDefault())


    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
    get() = _date

    private val _dateEt = MutableLiveData<String>()
    val dateEt: LiveData<String>
    get() = _dateEt

    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
    get() = _time

    private var _dayName = MutableLiveData<String>()
    val dayName: LiveData<String>
    get() = _dayName

    private var currentDate = dateFormatApi.format(currentTime)
    private lateinit var schedules: ArrayList<ScheduleData>

    private val _getSchedulesResponseState: MutableLiveData<Resource<SeniorSchedules>?> = MutableLiveData()
    val getSchedulesResponseState: LiveData<Resource<SeniorSchedules>?>
    get() = _getSchedulesResponseState

    private var _scheduleList = MutableLiveData<ArrayList<ScheduleData>>()
    val scheduleList: LiveData<ArrayList<ScheduleData>>
    get() = _scheduleList

    private val _cancelScheduleResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val cancelScheduleResponseState: LiveData<Resource<MiniResponse>?>
    get() = _cancelScheduleResponseState

    private val _addNewScheduleResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val addNewScheduleResponseState: LiveData<Resource<MiniResponse>?>
    get() = _addNewScheduleResponseState

    init {
        _dateEt.value = dateFormatApi.format(currentTimeEt)
        _date.value = dateFormat.format(currentTime)
        _dayName.value = dayFormat.format(currentTime)
        _time.value = "" //SimpleDateFormat("HH:mm").format(currentTime)
    }

    suspend fun nextDayClick() {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        updateDateAndDayName()
        filterSchedules()
    }

    suspend fun previousDayClick() {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        updateDateAndDayName()
        filterSchedules()
    }

    private fun datePicker(): DatePickerDialog.OnDateSetListener {
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateAndDayName()
            runBlocking {
                filterSchedules()
            }
        }
        return datePicker
    }

    private fun datePickerEt(): DatePickerDialog.OnDateSetListener {
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendarEt.set(Calendar.YEAR, year)
            calendarEt.set(Calendar.MONTH, month)
            calendarEt.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            currentTimeEt = calendarEt.time
            _dateEt.value = dateFormatApi.format(currentTimeEt)
        }
        return datePicker
    }

    fun showCalender(context: Context) {
        DatePickerDialog(context, datePicker(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun showCalenderEt(context: Context) {
        DatePickerDialog(context, datePickerEt(), calendarEt.get(Calendar.YEAR), calendarEt.get(Calendar.MONTH),
                                calendarEt.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun showTimeDialog(context: Context) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            _time.value = SimpleDateFormat("HH:mm").format(calendar.time)
        }
        TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
    }
    private fun updateDateAndDayName() {
        currentTime = calendar.time
        _date.value = dateFormat.format(currentTime)
        _dayName.value = dayFormat.format(currentTime)
        currentDate = dateFormatApi.format(currentTime)
    }

    suspend fun getSchedules(userId: Int) {
        try {
            _getSchedulesResponseState.value = Resource.Loading()
            schedules = getSchedulesUseCase(userId).data
            _getSchedulesResponseState.value = getSchedulesUseCase.handleResponse().value
        } catch (e:Exception){
            Log.e("ScheduleViewModel",e.message.toString())
        }
        filterSchedules()
    }

    private suspend fun filterSchedules() {
        val arr = ArrayList<ScheduleData>()
        for(item in schedules){
            if(item.date == currentDate){
                arr.add(item)
                //_scheduleList.value?.add(item)
            }
        }
        _scheduleList.value = arr
    }

    suspend fun cancelSchedule(scheduleId: Int) {
        try {
            _cancelScheduleResponseState.value = Resource.Loading()
            cancelScheduleUseCase(scheduleId)
            _cancelScheduleResponseState.value = cancelScheduleUseCase.handleResponse().value
        } catch (e:Exception){
            Log.e("ScheduleViewModel",e.message.toString())
        }
    }

    suspend fun addNewSchedule(userId: Int, title: String, date: String, time: String, description: String){
        try {
            _addNewScheduleResponseState.value = Resource.Loading()
            addNewScheduleUseCase(userId, title, date, time, description)
            _addNewScheduleResponseState.value = addNewScheduleUseCase.handleResponse().value
        } catch (e:Exception){
            Log.e("ScheduleViewModel",e.message.toString())
        }
    }
}