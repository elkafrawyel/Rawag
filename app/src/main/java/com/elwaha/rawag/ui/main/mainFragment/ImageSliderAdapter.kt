package com.elwaha.rawag.ui.main.mainFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.loadWithPlaceHolder


class ImageSliderAdapter(private val openFullScreenSlider: (Int) -> Unit) : PagerAdapter() {

    private val ads = ArrayList<AdModel>()

    override fun isViewFromObject(view: View, v: Any): Boolean {
        return view == v
    }

    override fun getCount(): Int {
        return ads.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val cardView = LayoutInflater.from(container.context)
            .inflate(R.layout.image_slider_item, container, false) as CardView

        container.addView(cardView)

        val imageView = cardView.findViewById<ImageView>(R.id.imageSlider)

        imageView.loadWithPlaceHolder(Constants.IMAGES_BASE_URL + ads[position].img)
        imageView.setOnClickListener { openFullScreenSlider(position) }
        return cardView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun submitList(imagesList: List<AdModel>) {
        ads.clear()
        ads.addAll(imagesList)
        notifyDataSetChanged()
    }

}