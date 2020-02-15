package com.task.mvvmsample.utils

import android.content.ContextWrapper
import android.view.View
import androidx.lifecycle.LifecycleOwner

val View.lifecycleOwner: LifecycleOwner
    get() {
        var context = context
        while (context !is LifecycleOwner) {
            context = (context as ContextWrapper).baseContext
        }
        return context
    }