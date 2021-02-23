package com.example.noogabab.presentation.ui.main.setting.dog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noogabab.presentation.entity.PresenterBobTime

class DogProfileViewModel constructor(): ViewModel() {
    // value
    private val _currentDogName = MutableLiveData<String>()
    private val _currentDogAge = MutableLiveData<Int>()
    private val _currentDogKind = MutableLiveData<String>()
    private val _currentBobTimes = MutableLiveData<ArrayList<PresenterBobTime>>()

    // validation
    private val _currentBtnState = MutableLiveData<Boolean>()

    init {
        _currentDogName.value = ""
        _currentDogAge.value = 0
        _currentDogKind.value = ""
        _currentBobTimes.value = ArrayList<PresenterBobTime>()
        _currentBtnState.value = false
    }

    // public
    val currentBtnState: LiveData<Boolean>
        get() = _currentBtnState

    fun updateDogName(input: String) {
        _currentDogName.value = input
        isValidation()
    }

    fun updateDogAge(input: String) {
        var myValue = 0
        if (input != "") myValue = input.toInt()
        _currentDogAge.value = myValue
        isValidation()
    }

    fun updateDogKind(input: String) {
        _currentDogKind.value = input
        isValidation()
    }

    fun updateBobTimes(input: PresenterBobTime) {
        _currentBobTimes.value?.add(input)
        isValidation()
    }

    private fun isValidation() {
        _currentBtnState.value =
            !(_currentDogName.value == "" || _currentDogAge.value == 0 ||
                    _currentDogKind.value == "" || _currentBobTimes.value?.size == 0)
    }
}