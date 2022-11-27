package com.s24.connpassclient

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SearchViewModel (
    application: Application
) : AndroidViewModel(application), LifecycleObserver {
    var query = Query()
    var hasNext = true

    val keyword = MutableLiveData<String>()
    val eventList = MutableLiveData<ArrayList<Event>>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var nextStart = 1
    private val searchRepository = ServiceRepository()
    private val _isLoading = MutableLiveData<Boolean>(false)

    fun clear() {
        nextStart = 1
    }

    fun loadNext() {
        if (isLoading.value == false) {
            viewModelScope.launch {
                _isLoading.postValue(true)

                val tootListSnapshot = eventList.value ?: ArrayList()
                query.start = nextStart
                val tootListResponse = searchRepository.search(query)
                tootListSnapshot.addAll(tootListResponse.events)
                eventList.postValue(tootListSnapshot)
                nextStart = tootListResponse.resultsStart + tootListResponse.resultsReturned
                hasNext = tootListResponse.events.isNotEmpty()
                _isLoading.postValue(false)
            }
        } else {
            _isLoading.postValue(false)
        }
    }

    fun setSimpleSearch() {
        query = Query(keyword = keyword.value)
        nextStart = 1
    }
}