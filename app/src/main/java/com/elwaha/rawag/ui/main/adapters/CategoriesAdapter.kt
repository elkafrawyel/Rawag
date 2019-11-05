package com.elwaha.rawag.ui.main.adapters

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector

class CategoriesAdapter :
    BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.categories_item_view) {

    override fun convert(helper: BaseViewHolder, item: CategoryModel) {

        Glide.with(mContext).load(Constants.IMAGES_BASE_URL + item.img)
            .into(helper.getView(R.id.categoryImage))
        helper.setText(
            R.id.categoryName,
            if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value)
                item.name_ar
            else
                item.name_ar
        )
        helper.addOnClickListener(R.id.cardItem)
    }

}