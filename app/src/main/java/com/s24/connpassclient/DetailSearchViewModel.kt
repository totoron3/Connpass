package com.s24.connpassclient

import android.app.Application
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

class DetailSearchViewModel (
    application: Application
) : AndroidViewModel(application), LifecycleObserver {
    val keyword = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val condition = MutableLiveData<Int>()
    var isChecked = MutableLiveData<BooleanArray>()

    init {
        isChecked.value = BooleanArray(48){false}
    }

    fun onClick(view: View, ordinal: Int) {
        if (view is CheckBox) {
            isChecked.value?.set(ordinal, view.isChecked)
        }
    }

}