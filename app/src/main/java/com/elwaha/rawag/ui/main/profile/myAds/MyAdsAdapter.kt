package com.elwaha.rawag.ui.main.profile.myAds

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.loadWithPlaceHolder
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class MyAdsAdapter : BaseQuickAdapter<AdModel, BaseViewHolder>(R.layout.my_ad_item_view) {

    override fun convert(helper: BaseViewHolder, item: AdModel) {
        helper.addOnClickListener(R.id.editImgv,R.id.cancelImgv)


        val imageListener =
            ImageListener { position, imageView ->
                imageView.loadWithPlaceHolder(Constants.IMAGES_BASE_URL+item.imageModels!![position]!!.img)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }

        helper.getView<CarouselView>(R.id.carouselView).setImageListener(imageListener)
        helper.getView<CarouselView>(R.id.carouselView).pageCount = item.imageModels!!.size
    }


}