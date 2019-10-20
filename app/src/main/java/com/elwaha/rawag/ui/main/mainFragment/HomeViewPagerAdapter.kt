package com.elwaha.rawag.ui.main.mainFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.elwaha.rawag.ui.main.mainFragment.categories.CategoriesFragment
import com.elwaha.rawag.ui.main.mainFragment.favourites.FavouritesFragment
import com.elwaha.rawag.ui.main.mainFragment.home.HomeFragment

class HomeViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = listOf(
        HomeFragment(),
        CategoriesFragment(),
        FavouritesFragment()
    )
    override fun getItem(position: Int): Fragment = fragmentList[position] as Fragment

    override fun getCount(): Int = fragmentList.size

}