package com.elwaha.rawag.ui.main.profile.comments

import com.elwaha.rawag.data.models.ProblemModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class CommentsViewModel : AppViewModel() {

    var action: CommentsActions? = null
    var problems = ArrayList<ProblemModel>()

    fun get(commentsActions: CommentsActions) {
        checkNetwork {
            action = commentsActions
            runOnMainThread {
                _uiState.value = ViewState.Success
            }
            scope.launch(dispatcherProvider.io) {
                when (action) {
                    CommentsActions.PROBLEMS -> {
                        when (val result =
                            Injector.getStaticRepo().getProblems()) {
                            is DataResource.Success -> {
                                problems.clear()
                                problems.addAll(result.data)
                                runOnMainThread {
                                    _uiState.value = ViewState.Success
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value = ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
                    CommentsActions.GET -> TODO()
                    CommentsActions.REPORT -> TODO()
                    CommentsActions.SEND -> TODO()
                    CommentsActions.DELETE -> TODO()
                    CommentsActions.HIDE -> TODO()
                }
            }
        }
    }
}

enum class CommentsActions(val value: String) {
    GET("get"),
    REPORT("report"),
    PROBLEMS("problems"),
    SEND("send"),
    DELETE("delete"),
    HIDE("hide"),
}