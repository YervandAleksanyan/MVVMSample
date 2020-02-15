package com.task.mvvmsample.viewmodel.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


@JvmOverloads
fun <A, B, C, S> MediatorLiveData<S>.addSources(
    source1: LiveData<A>? = null,
    source2: LiveData<B>? = null,
    source3: LiveData<C>? = null,
    combine: (data1: A?, data2: B?, data3: C?) -> S
) {
    var data1: A? = null
    var data2: B? = null
    var data3: C? = null

    source1?.let { liveData ->
        this.addSource(liveData) {
            data1 = it
            value = combine(data1, data2, data3)
        }
    }
    source2?.let { liveData ->
        this.addSource(liveData) {
            data2 = it
            value = combine(data1, data2, data3)
        }
    }

    source3?.let { liveData ->
        this.addSource(liveData) {
            data3 = it
            value = combine(data1, data2, data3)
        }
    }
}
