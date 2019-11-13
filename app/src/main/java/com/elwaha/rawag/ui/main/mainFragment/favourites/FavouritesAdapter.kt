package com.elwaha.rawag.ui.main.mainFragment.favourites

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel

class FavouritesAdapter : BaseQuickAdapter<UserModel, BaseViewHolder>(R.layout.profile_item_view) {

    override fun convert(helper: BaseViewHolder, item: UserModel) {
        helper.addOnClickListener(R.id.profileItem)
    }

}