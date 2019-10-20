package com.elwaha.rawag.ui.main.profile.editProfile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.elwaha.rawag.ui.main.profile.editProfile.info.EditInfoFragment
import com.elwaha.rawag.ui.main.profile.editProfile.social.EditSocialFragment

class ProfileViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = listOf(
        EditInfoFragment(),
        EditSocialFragment()
    )
    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}