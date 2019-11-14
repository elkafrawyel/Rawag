package com.elwaha.rawag.ui.main.profile.comments

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CommentModel
import com.elwaha.rawag.data.models.ProblemModel
import com.elwaha.rawag.data.models.requests.AddCommentRequest
import com.elwaha.rawag.data.models.requests.AddReportRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class CommentsViewModel : AppViewModel() {

    var commentPosition: Int? = null
    var action: CommentsActions? = null
    var problems = ArrayList<ProblemModel>()
    var allComments = ArrayList<CommentModel>()
    var addedComment: CommentModel? = null
    var addCommentRequest: AddCommentRequest? = null
    var userId: String? = null
    var problemId: String? = null
    var commentId: String? = null


    fun get(commentsActions: CommentsActions) {
        checkNetwork {
            action = commentsActions
            runOnMainThread {
                _uiState.value = ViewState.Loading
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
                    CommentsActions.GET -> {
                        when (val result =
                            Injector.getUserRepo().allComments(userId!!)) {
                            is DataResource.Success -> {
                                allComments.clear()
                                allComments.addAll(result.data)
                                if (allComments.isEmpty()) {
                                    runOnMainThread {
                                        _uiState.value = ViewState.Empty
                                    }
                                } else {
                                    runOnMainThread {
                                        _uiState.value = ViewState.Success
                                    }
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value = ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
                    CommentsActions.REPORT -> {
                        when (val result =
                            Injector.getUserRepo()
                                .addReport(AddReportRequest(commentId!!, problemId!!))) {
                            is DataResource.Success -> {
                                if (result.data) {
                                    runOnMainThread {
                                        _uiState.value = ViewState.Success
                                    }
                                } else {
                                    runOnMainThread {
                                        _uiState.value = ViewState.Error(
                                            Injector.getApplicationContext().getString(
                                                R.string.errorSendReport
                                            )
                                        )
                                    }
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value = ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
                    CommentsActions.SEND -> {
                        when (val result =
                            Injector.getUserRepo().addComment(addCommentRequest!!)) {
                            is DataResource.Success -> {
                                addedComment = result.data
                                runOnMainThread {
                                    _uiState.value = ViewState.Success
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value =
                                        ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
                    CommentsActions.DELETE -> {
                        when (val result =
                            Injector.getUserRepo().deleteComments(commentId!!)) {
                            is DataResource.Success -> {
                                runOnMainThread {
                                    _uiState.value = ViewState.Success
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value =
                                        ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
                    CommentsActions.HIDE ->{
                        when (val result =
                            Injector.getUserRepo().deleteComments(commentId!!)) {
                            is DataResource.Success -> {
                                runOnMainThread {
                                    _uiState.value = ViewState.Success
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiState.value =
                                        ViewState.Error(result.errorMessage)
                                }
                            }
                        }
                    }
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