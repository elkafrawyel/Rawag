package com.elwaha.rawag.ui.main.adapters

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.loadWithPlaceHolder

class CategoriesAdapter :
    BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.categories_item_view) {

    override fun convert(helper: BaseViewHolder, item: CategoryModel) {

        helper.getView<ImageView>(R.id.categoryImage)
            .loadWithPlaceHolder(Constants.IMAGES_BASE_URL + item.img)
        helper.setText(
            R.id.categoryName,
            if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value)
                item.name_ar
            else
                item.name_en
        )
        helper.addOnClickListener(R.id.cardItem)
    }

}