package com.elwaha.rawag.ui.main.profile.comments

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R

class CommentsAdapter  : BaseQuickAdapter<String, BaseViewHolder>(R.layout.comments_item_view) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.addOnClickListener(R.id.reportComment,R.id.menuComment)
    }

}