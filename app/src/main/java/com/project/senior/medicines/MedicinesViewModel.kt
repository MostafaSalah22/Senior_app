package com.project.senior.medicines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.*
import com.project.domain.repo.Resource
import com.project.domain.usecase.AddNewMedicineUseCase
import com.project.domain.usecase.DeleteMedicineUseCase
import com.project.domain.usecase.GetMedicinesOfBookingUseCase
import com.project.domain.usecase.UpdateMedicineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicinesViewModel @Inject constructor(
    private val getMedicinesOfBookingUseCase: GetMedicinesOfBookingUseCase,
    private val addNewMedicineUseCase: AddNewMedicineUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val updateMedicineUseCase: UpdateMedicineUseCase
): ViewModel() {

    private val _getMedicinesResponseState: MutableLiveData<Resource<MedicinesModel>?> = MutableLiveData()
    val getMedicinesResponseState: LiveData<Resource<MedicinesModel>?>
    get() = _getMedicinesResponseState

    private val _medicinesList: MutableLiveData<ArrayList<MedicineData>?> = MutableLiveData()
    val medicinesList: LiveData<ArrayList<MedicineData>?>
    get() = _medicinesList

    private val _deleteMedicineResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val deleteMedicineResponseState: LiveData<Resource<MiniResponse>?>
    get() = _deleteMedicineResponseState

    private val _updateMedicineResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val updateMedicineResponseState: LiveData<Resource<MiniResponse>?>
    get() = _updateMedicineResponseState

    private val _addNewMedicineResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val addNewMedicineResponseState: LiveData<Resource<MiniResponse>?>
    get() = _addNewMedicineResponseState

    suspend fun getMedicines(userId: Int){
        try {
            _getMedicinesResponseState.value = Resource.Loading()
            val medicinesResponse = getMedicinesOfBookingUseCase(userId)
            _getMedicinesResponseState.value = getMedicinesOfBookingUseCase.handleResponse().value
            _medicinesList.value = medicinesResponse
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _getMedicinesResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun deleteMedicineCategory(medicineId: Int){
        try {
            _deleteMedicineResponseState.value = Resource.Loading()
            deleteMedicineUseCase(medicineId)
            _deleteMedicineResponseState.value = deleteMedicineUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _deleteMedicineResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun updateMedicine(medicineId: Int,
                               medicineName: String,
                               medicineDose: Int,
                               medicineDescription: String){
        try {
            _updateMedicineResponseState.value = Resource.Loading()
            updateMedicineUseCase(medicineId, medicineName, medicineDose, medicineDescription)
            _updateMedicineResponseState.value = updateMedicineUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _updateMedicineResponseState.value = Resource.Error(e.message.toString())
        }
    }

    suspend fun addNewMedicine(userId: Int,
                               medicineName: String,
                               medicineDose: Int,
                               medicineDescription: String){
        try {
            _addNewMedicineResponseState.value = Resource.Loading()
            addNewMedicineUseCase(userId, medicineName, medicineDose, medicineDescription)
            _addNewMedicineResponseState.value = addNewMedicineUseCase.handleResponse().value
        } catch (e: Exception){
            Log.e("SeniorInformationViewModel",e.message.toString())
            _addNewMedicineResponseState.value = Resource.Error(e.message.toString())
        }
    }

}