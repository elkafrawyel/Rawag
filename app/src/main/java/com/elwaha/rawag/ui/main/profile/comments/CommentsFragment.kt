package com.elwaha.rawag.ui.main.profile.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
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

        viewModel.get(CommentsActions.PROBLEMS)
        arguments?.let {
            val profileId =
                com.elwaha.rawag.ui.main.profile.comments.CommentsFragmentArgs.fromBundle(
                    it
                ).profileId
            activity?.toast(profileId)
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        commentsRv.adapter = adapter
        commentsRv.setHasFixedSize(true)

        sendComment.setOnClickListener {
            if (commentEt.text.toString().isNotEmpty())
                addComment()
        }
    }

    private fun addComment() {

        KeyboardUtils.hideSoftInput(activity)
        val userString = Injector.getPreferenceHelper().user
        val user = ObjectConverter().getUser(userString!!)


        val ratingView = LayoutInflater.from(context).inflate(R.layout.rating, null)
        val ratingBar = ratingView.findViewById<RatingBar>(R.id.ratingBar)
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.add_rate))
            .setView(ratingView)
            .setPositiveButton(R.string.send) { dialog, _ ->
                if (ratingBar.rating > 0) {
                    viewModel.addCommentRequest = AddCommentRequest(
                        commentEt.text.toString(),
                        user.id.toString(),
                        ratingBar.rating.toString()
                    )
                    viewModel.get(CommentsActions.SEND)
                    commentEt.setText("")
                }else{
                    activity?.toast(getString(R.string.select_rate))
                }
            }.show()

    }

    private fun onActionsResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {

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

                    }
                    CommentsActions.HIDE -> {

                    }
                }
            }
            ViewState.Success -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {

                    }
                    CommentsActions.REPORT -> {
                        openReportDialog()
                    }
                    CommentsActions.PROBLEMS -> {

                    }
                    CommentsActions.SEND -> {
                        if (loading != null) {
                            loading!!.dismiss()
                        }
                        activity?.toast(getString(R.string.success))
                    }
                    CommentsActions.DELETE -> {

                    }
                    CommentsActions.HIDE -> {

                    }
                }
            }
            is ViewState.Error -> {
                when (viewModel.action) {
                    CommentsActions.GET -> {

                    }
                    CommentsActions.REPORT -> {

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

                    }
                    CommentsActions.HIDE -> {

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
                openReportDialog()
            }

            R.id.menuComment -> {
                val optionIcon = view.findViewById<ImageView>(R.id.menuComment)
                showCommentMenu(optionIcon)
            }
        }
    }

    private fun openReportDialog() {

        val problemsArray: Array<out String> =
            viewModel.problems.map { it.report }.toTypedArray()
        AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
            .setTitle(getString(R.string.report_reason))
            .setSingleChoiceItems(problemsArray, -1, null)
            .setPositiveButton(R.string.send) { dialog, _ ->
                dialog.dismiss()
                val selectedPosition: Int =
                    (dialog as AlertDialog).listView.checkedItemPosition
                activity?.toast(viewModel.problems[selectedPosition].report)
            }
            .show()


//        val dialog = ReportDialog(context!!, viewModel.problems, object : IReportProblems {
//            override fun getProblem(problems: Problems) {
//                //report a comment
//            }
//        })
//        dialog.show()
    }

    private fun showCommentMenu(optionIcon: ImageView) {
        //creating a popup menu
        val popup = PopupMenu(context!!, optionIcon)
        //inflating menu from xml resource
        popup.inflate(R.menu.comment_menu)
        //adding click listener
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.hide -> {
                    activity?.toast("hide")
                }
                R.id.delete -> {
                    activity?.toast("delete")
                }
            }
            //handle menu2 click
            //handle menu3 click
            false
        }
        //displaying the popup
        popup.show()
    }
}
