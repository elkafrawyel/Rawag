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

    var job: Job? = null

    private val parentJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + parentJob)

    val dispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    protected var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState> = _uiState

    protected var _uiStateEvent = MutableLiveData<Event<ViewState>>()
    val uiStateEvent: LiveData<Event<ViewState>> = _uiStateEvent

    fun checkNetwork(JobCode: () -> Unit) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(JobCode)
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    fun checkNetworkEvent(code: () -> Unit) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = scope.launch(dispatcherProvider.io) {
                code.invoke()
            }
        } else {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.NoConnection)
            }
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

