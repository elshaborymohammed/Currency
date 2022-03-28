package com.test.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataValueCapture<T> {

    val lock = Any()

    private var _value: T? = null
    val value: T?
        get() = synchronized(lock) {
             _value
        }

    fun addValue(value: T?) = synchronized(lock) {
        _value = value
    }
}

inline fun <T> LiveData<T>.captureValues(block: LiveDataValueCapture<T>.() -> Unit) {
    val capture = LiveDataValueCapture<T>()
    val observer = Observer<T> {
        capture.addValue(it)
    }
    observeForever(observer)
    try {
        capture.block()
    } finally {
        removeObserver(observer)
    }
}

fun <T> LiveData<T>.getValueForTest(): T? {
    var value: T? = null
    var observer = Observer<T> {
        value = it
        println("observer: $it")
    }
    observeForever(observer)
    removeObserver(observer)
    return value
}
