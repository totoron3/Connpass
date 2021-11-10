package com.s24.connpassclient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

class ShowDetailViewModel(
    private val eventDate: Event?,
    application: Application
) : AndroidViewModel(application), LifecycleObserver {
    val event = MutableLiveData<Event>().also {
        it.value = eventDate
    }
}