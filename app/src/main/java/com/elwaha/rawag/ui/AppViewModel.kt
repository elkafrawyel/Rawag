package com.elwaha.rawag.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.elwaha.rawag.utilies.CoroutinesDispatcherProvider
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.*

abstract class AppViewModel : ViewModel() {

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    var job: Job? = null

    protected var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    protected var _uiStateEvent = MutableLiveData<Event<ViewState>>()
    val uiStateEvent: LiveData<Event<ViewState>>
        get() = _uiStateEvent

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    fun checkNetwork(JobCode: () -> Unit) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(JobCode)
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(jobCode: () -> Unit): Job {
        return scope.launch(dispatcherProvider.io) {
            jobCode.invoke()
        }
    }

    fun runOnMainThread(action: () -> Unit) {
        scope.launch {
            withContext(dispatcherProvider.main) {
                action.invoke()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}