package com.elwaha.rawag.ui.main.adapters

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R

class ImagesAdapter : BaseQuickAdapter<Uri, BaseViewHolder>(R.layout.image_item_view) {

    override fun convert(helper: BaseViewHolder, item: Uri) {
        helper.addOnClickListener(R.id.delete)
        Glide.with(mContext)
            .load(item)
            .into(helper.getView(R.id.image) as ImageView)
    }

}