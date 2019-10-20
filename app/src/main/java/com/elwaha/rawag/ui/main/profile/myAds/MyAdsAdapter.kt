package com.elwaha.rawag.ui.main.profile.myAds

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R

class MyAdsAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.my_ad_item_view) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.addOnClickListener(R.id.editImgv,R.id.cancelImgv)
    }

}