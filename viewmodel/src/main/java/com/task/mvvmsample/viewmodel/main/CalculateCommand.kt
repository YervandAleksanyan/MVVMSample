package com.task.mvvmsample.viewmodel.main

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.task.mvvmsample.viewmodel.base.IDisposableCommand
import com.task.mvvmsample.viewmodel.base.implementation.BaseCommand
import com.task.mvvmsample.viewmodel.utils.addSources

class CalculateCommand(private val viewModel: MainViewModel) : BaseCommand(canExecute = false),
    IDisposableCommand {


    private val observer: Observer<Boolean>

    private val isCalculationEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData()

    init {
        isCalculationEnabled.addSources(
            viewModel.value1,
            viewModel.value2
        ) { s: String?, s1: String?, _: String? ->
            combineDataForLoginState(s, s1)
        }

        observer = Observer {
            it?.let { canExecute ->
                setCanExecute(canExecute)
            }
        }
        isCalculationEnabled.observeForever(observer)
    }

    private fun combineDataForLoginState(value1: String?, value2: String?): Boolean =
        (!value1.isNullOrEmpty() && !value2.isNullOrEmpty())

    override fun executeCore() {
        val value1 = viewModel.value1.value?.toInt() ?: 0
        val value2 = viewModel.value2.value?.toInt() ?: 0
        Log.i("tag", "${value1 + value2}")
    }

    override fun dispose() {
        isCalculationEnabled.removeObserver(observer)
    }
}