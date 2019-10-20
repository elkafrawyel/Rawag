package com.elwaha.rawag.ui.main.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R

class CategoriesAdapter  : BaseQuickAdapter<String, BaseViewHolder>(R.layout.categories_item_view) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.addOnClickListener(R.id.cardItem)
    }

}