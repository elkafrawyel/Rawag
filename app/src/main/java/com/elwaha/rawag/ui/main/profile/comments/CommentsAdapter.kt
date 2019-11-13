package com.elwaha.rawag.ui.main.profile.comments

import android.widget.ImageView
import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CommentModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ObjectConverter
import com.elwaha.rawag.utilies.loadWithPlaceHolder

class CommentsAdapter :
    BaseQuickAdapter<CommentModel, BaseViewHolder>(R.layout.comments_item_view) {

    var isMyAccount: Boolean = false

    override fun convert(helper: BaseViewHolder, item: CommentModel) {
        helper.addOnClickListener(R.id.reportComment, R.id.menuComment)
        helper.getView<ImageView>(R.id.commentUserImage)
            .loadWithPlaceHolder(Constants.IMAGES_BASE_URL + item.user_img)
        helper.setText(R.id.commentUserName, item.user_name)
        helper.setText(R.id.commentText, item.comment)
        helper.getView<RatingBar>(R.id.commentRateBar).rating = item.value!!.toFloat()

        if (isMyAccount) {
            helper.setGone(R.id.reportComment, false)
            helper.setGone(R.id.menuComment, true)
        } else {
            helper.setGone(R.id.reportComment, true)
            helper.setGone(R.id.menuComment, false)
        }
    }

}