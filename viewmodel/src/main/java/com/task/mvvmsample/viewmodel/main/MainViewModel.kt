package com.task.mvvmsample.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val value1 = MutableLiveData<String>()
    val value2 = MutableLiveData<String>()
    val calculateCommand = CalculateCommand(this)

    override fun onCleared() {
        super.onCleared()
        calculateCommand.dispose()
    }

}