package com.elwaha.rawag.ui.main.mainFragment.home.filter.cities

import android.widget.RadioButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CityModel

class AdapterCities :
    BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.city_item_view) {

    var lastSelectCity = -1
    override fun convert(helper: BaseViewHolder, item: CityModel) {
        helper.getView<RadioButton>(R.id.cityRb).text = item.toString()

        helper.getView<RadioButton>(R.id.cityRb).isChecked = helper.adapterPosition == lastSelectCity


        helper.addOnClickListener(R.id.cityRb)
    }

}