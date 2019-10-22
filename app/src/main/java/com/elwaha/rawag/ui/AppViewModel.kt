package com.elwaha.rawag.ui

import androidx.lifecycle.ViewModel
import com.elwaha.rawag.utilies.CoroutinesDispatcherProvider
import kotlinx.coroutines.*

abstract class AppViewModel : ViewModel() {

    val dispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    private val parentJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun runOnMainThread(action: () -> Unit) {
        scope.launch {
            withContext(dispatcherProvider.main) {
                action.invoke()
            }
        }
    }

}