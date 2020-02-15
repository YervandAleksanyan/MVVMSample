package com.task.mvvmsample.core.utils

import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class WeakReferenceDelegate<T>(obj: T? = null) : ReadOnlyProperty<Any?, T?> {

    private var reference: WeakReference<T>? = obj?.let { WeakReference(it) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return reference?.get()
    }
}

fun <T> weak(obj: T? = null) = WeakReferenceDelegate(obj)