package com.elwaha.rawag.ui.main.profile.myAds.editAd

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.SealedImageModel
import com.elwaha.rawag.utilies.loadWithPlaceHolder

class AdapterEditAd : BaseQuickAdapter<SealedImageModel, BaseViewHolder>(R.layout.edit_image_item_view) {

    override fun convert(helper: BaseViewHolder, item: SealedImageModel) {
        helper.addOnClickListener(R.id.delete)

        when (item) {
            is SealedImageModel.StringSealedImage -> {
                helper.getView<ImageView>(R.id.image).loadWithPlaceHolder(item.image)
            }
            is SealedImageModel.UriSealedImage -> {
                helper.getView<ImageView>(R.id.image).loadWithPlaceHolder(item.image)
            }
        }
    }
}