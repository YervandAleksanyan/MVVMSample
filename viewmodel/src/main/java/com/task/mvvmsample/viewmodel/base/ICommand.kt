package com.task.mvvmsample.viewmodel.base

import androidx.lifecycle.LiveData


interface ICommand {

    val isExecutable: LiveData<Boolean>

    fun execute()
}