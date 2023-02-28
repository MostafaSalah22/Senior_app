package com.project.senior.schedule

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(app:Application): AndroidViewModel(app) {

    private val calendar = Calendar.getInstance()
    private var currentTime = calendar.time
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEEE" , Locale.getDefault())


    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
    get() = _date

    private var _dayName = MutableLiveData<String>()
    val dayName: LiveData<String>
    get() = _dayName

    init {
        _date.value = dateFormat.format(currentTime)
        _dayName.value = dayFormat.format(currentTime)
    }

    fun nextDayClick() {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        updateDateAndDayName()
    }

    fun previousDayClick() {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        updateDateAndDayName()
    }

    private fun datePicker(): DatePickerDialog.OnDateSetListener {
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateAndDayName()
        }
        return datePicker
    }

    fun showCalender(context: Context) {
        DatePickerDialog(context, datePicker(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateDateAndDayName() {
        currentTime = calendar.time
        _date.value = dateFormat.format(currentTime)
        _dayName.value = dayFormat.format(currentTime)
    }
}