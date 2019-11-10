package com.elwaha.rawag.ui.main.profile.editProfile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ObjectConverter
import kotlinx.android.synthetic.main.edit_profile_fragment.*

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        val adapter =
            ProfileViewPagerAdapter(fragmentManager = childFragmentManager)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> tabs.getTabAt(0)!!.select()
                    1 -> tabs.getTabAt(1)!!.select()
                }
            }
        })

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewPager.setCurrentItem(0,true)
                    }

                    1 -> {
                        viewPager.setCurrentItem(1,true)
                    }
                }
            }
        })

        backImgv.setOnClickListener { findNavController().navigateUp() }



    }

}
