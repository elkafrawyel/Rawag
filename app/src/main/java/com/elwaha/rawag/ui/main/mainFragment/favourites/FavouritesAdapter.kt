package com.elwaha.rawag.ui.main.mainFragment.favourites

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R

class FavouritesAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.profile_item_view) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.addOnClickListener(R.id.profileItem)
    }

}