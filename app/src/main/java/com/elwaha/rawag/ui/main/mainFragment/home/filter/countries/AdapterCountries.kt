package com.elwaha.rawag.ui.main.mainFragment.home.filter.countries

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.loadWithPlaceHolder

class AdapterCountries :
    BaseQuickAdapter<CountryModel, BaseViewHolder>(R.layout.country_item_view) {
    override fun convert(helper: BaseViewHolder, item: CountryModel) {
        helper.getView<ImageView>(R.id.countryImage)
            .loadWithPlaceHolder(Constants.IMAGES_BASE_URL + item.img)
        helper.setText(R.id.countryName, item.toString())
        helper.addOnClickListener(R.id.countryItem)
    }

}