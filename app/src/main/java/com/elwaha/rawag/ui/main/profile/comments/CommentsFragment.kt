package com.elwaha.rawag.ui.main.profile.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.requests.AddCommentRequest
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.comments_fragment.*

class CommentsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CommentsFragment()
    }

    private var loading: SpotsDialog? = null

    private lateinit var viewModel: CommentsViewModel
    private var adapter = CommentsAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.comments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onActionsResponse(it) })

        arguments?.let {
            val profileId = CommentsFragmentArgs.fromBundle(it).profileId
            viewModel.userId = profileId

            if (Injector.getPreferenceHelper().isLoggedIn) {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                if (profileId == user.id.toString()) {
                    adapter.isMyAccount = true
                }
            }
            viewModel.get(CommentsActions.GET)
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        commentsRv.adapter = adapter
        commentsRv.setHasFixedSize(true)

        sendComment.setOnClickListener {
            if (Injector.getPreferenceHelper().isLoggedIn) {
                if (commentEt.text.toString().isNotEmpty())
                    addComment()
            } else {
                activity?.toast(getString(R.string.you_must_login))
            }
        }
    }

    private fun addComment() {

        KeyboardUtils.hideSoftInput(activity)


        val ratingView = LayoutInflater.from(context).inflate(R.layout.rating, null)
        val ratingBar = ratingView.findViewById<RatingBar>(R.id.ratingBar)
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.add_rate))
            .setView(ratingView)
            .setPositiveButton(R.string.send) { _, _ ->
                if (ratingBar.rating > 0) {
                    viewModel.addCommentRequest = AddCommentRequest(
                        commentEt.text.toString(),
                        viewModel.userId!!,
                        ratingBar.rating.toString()
                    )
                    viewModel.get(CommentsActions.SEND)
                    commentEt.setText("")

                } else {
                    activity?.toast(getString(R.string.select_rate))
                }
            }.show()

    }

    private fun onActionsResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {
                        loading =
                            activity?.showLoading(context!!.resources.getString(R.string.wait))
                        loading!!.show()
                    }
                    CommentsActions.REPORT -> {

                    }
                    CommentsActions.PROBLEMS -> {

                    }
                    CommentsActions.SEND -> {
                        loading = activity?.showLoading(getString(R.string.wait))
                        loading!!.show()
                    }
                    CommentsActions.DELETE -> {
                        loading = activity?.showLoading(getString(R.string.wait))
                        loading!!.show()
                    }
                    CommentsActions.HIDE -> {
                        loading = activity?.showLoading(getString(R.string.wait))
                        loading!!.show()
                    }
                }
            }
            ViewState.Success -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        adapter.replaceData(viewModel.allComments)
                        emptyView.visibility = View.GONE
                        viewModel.get(CommentsActions.PROBLEMS)

                    }
                    CommentsActions.REPORT -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(getString(R.string.success))
                    }
                    CommentsActions.PROBLEMS -> {

                    }
                    CommentsActions.SEND -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(getString(R.string.success))
                        adapter.addData(viewModel.addedComment!!)
                        viewModel.allComments.add(viewModel.addedComment!!)
                        emptyView.visibility = View.GONE
                    }
                    CommentsActions.DELETE -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        adapter.data.removeAt(viewModel.commentPosition!!)
                        viewModel.allComments.removeAt(viewModel.commentPosition!!)
                        adapter.notifyItemRemoved(viewModel.commentPosition!!)
                        activity?.toast(getString(R.string.deleted))
                        if (viewModel.allComments.isEmpty()) {
                            emptyView.visibility = View.VISIBLE
                            emptyView.text = getString(R.string.empty_comments)
                        }
                    }
                    CommentsActions.HIDE -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        adapter.data.removeAt(viewModel.commentPosition!!)
                        viewModel.allComments.removeAt(viewModel.commentPosition!!)
                        adapter.notifyItemRemoved(viewModel.commentPosition!!)
                        activity?.toast(getString(R.string.hided))
                        if (viewModel.allComments.isEmpty()) {
                            emptyView.visibility = View.VISIBLE
                            emptyView.text = getString(R.string.empty_comments)
                        }
                    }
                }
            }
            is ViewState.Error -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(state.message)
                    }
                    CommentsActions.REPORT -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(state.message)
                    }
                    CommentsActions.PROBLEMS -> {

                    }
                    CommentsActions.SEND -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(state.message)
                    }
                    CommentsActions.DELETE -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(state.message)
                    }
                    CommentsActions.HIDE -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(state.message)
                    }
                }
            }
            ViewState.NoConnection -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.noInternet))
            }
            ViewState.Empty -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        emptyView.visibility = View.VISIBLE
                        emptyView.text = getString(R.string.empty_comments)
                    }
                    CommentsActions.REPORT -> {

                    }
                    CommentsActions.PROBLEMS -> {

                    }
                    CommentsActions.SEND -> {

                    }
                    CommentsActions.DELETE -> {

                    }
                    CommentsActions.HIDE -> {

                    }
                }
            }
            ViewState.LastPage -> {

            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.reportComment -> {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    openReportDialog(position)
                } else {
                    activity?.toast(getString(R.string.you_must_login))
                }
            }

            R.id.menuComment -> {
                val optionIcon = view.findViewById<ImageView>(R.id.menuComment)
                showCommentMenu(optionIcon, position)
            }
        }
    }

    private fun openReportDialog(commentPosition: Int) {

        val problemsArray: Array<out String> = viewModel.problems.map { it.report }.toTypedArray()
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.report_reason))
            .setSingleChoiceItems(problemsArray, 0, null)
            .setPositiveButton(R.string.send) { dialog, _ ->
                dialog.dismiss()
                val selectedPosition: Int =
                    (dialog as AlertDialog).listView.checkedItemPosition

                viewModel.commentId = viewModel.allComments[commentPosition].id.toString()
                viewModel.problemId = viewModel.problems[selectedPosition].id.toString()

                viewModel.get(CommentsActions.REPORT)
            }
            .show()


//        val dialog = ReportDialog(context!!, viewModel.problems, object : IReportProblems {
//            override fun getProblem(problems: Problems) {
//                //report a comment
//            }
//        })
//        dialog.show()
    }

    private fun showCommentMenu(optionIcon: ImageView, position: Int) {
        val popup = PopupMenu(context!!, optionIcon)
        popup.inflate(R.menu.comment_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.hide -> {
                    viewModel.commentId = viewModel.allComments[position].id.toString()
                    viewModel.commentPosition = position
                    viewModel.get(CommentsActions.HIDE)
                }
                R.id.delete -> {
                    viewModel.commentId = viewModel.allComments[position].id.toString()
                    viewModel.commentPosition = position
                    viewModel.get(CommentsActions.DELETE)
                }
            }
            false
        }
        popup.show()
    }
}
