package com.task.mvvmsample.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ImmutableLiveData<T>(private val source: LiveData<T>) : LiveData<T>() {
    private val observer = Observer<T> {
        value = it
    }

    override fun getValue(): T? {
        return source.value
    }

    override fun onActive() {
        super.onActive()
        source.observeForever(observer)
    }

    override fun onInactive() {
        super.onInactive()
        source.removeObserver(observer)
    }
}