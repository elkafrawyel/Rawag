package com.elwaha.rawag.ui.main.profiles

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.loadWithPlaceHolder

class ProfilesAdapter : BaseQuickAdapter<UserModel, BaseViewHolder>(R.layout.profile_item_view) {

    override fun convert(helper: BaseViewHolder, item: UserModel) {
        helper.addOnClickListener(R.id.profileItem, R.id.favImgv)
        helper.getView<ImageView>(R.id.userImage)
            .loadWithPlaceHolder(Constants.IMAGES_BASE_URL + item.avatar)

        helper.setText(R.id.userName, item.name)
        helper.setText(R.id.userAddress, item.address)
        helper.getView<TextView>(R.id.userAddress).isSelected = true
        helper.getView<RatingBar>(R.id.ratingBar).rating = item.rate_value.toFloat()

        if (item.isLiked == 1) {
            Glide.with(mContext).load(R.drawable.likeee)
                .into(helper.getView(R.id.favImgv))
        } else {
            Glide.with(mContext).load(R.drawable.like)
                .into(helper.getView(R.id.favImgv))
        }
    }

}