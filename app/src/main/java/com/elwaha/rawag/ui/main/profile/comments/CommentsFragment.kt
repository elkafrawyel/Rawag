package com.elwaha.rawag.ui.main.profile.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.profile.comments.report.ReportDialog
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.comments_fragment.*

class CommentsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CommentsFragment()
    }

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
        val dialog = ReportDialog(context!!)
        dialog.show()
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
