package com.elwaha.rawag.ui.main.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.elwaha.rawag.R
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)

        navigationView.setNavigationItemSelectedListener(this)

        drawerToggleImgv.setOnClickListener {
            rootView.openDrawer(GravityCompat.START)
        }

        val adapter =
            HomeViewPagerAdapter(fragmentManager = childFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3

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
                    2 -> tabs.getTabAt(2)!!.select()
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

                    2 -> {
                        viewPager.setCurrentItem(2,true)
                    }
                }
            }
        })

        searchImgv.setOnClickListener { findNavController().navigate(R.id.searchFragment) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_Profile -> {
//                if (Injector.getPreferenceHelper().isLoggedIn) {
                //isMyAccount is false
                val action =
                    com.elwaha.rawag.ui.main.mainFragment.MainFragmentDirections.actionMainFragmentToProfileFragment(
                        null,
                        true
                    )
                findNavController().navigate(action)
//                } else {
//                    snackBarWithAction(
//                        getString(R.string.you_must_login),
//                        getString(R.string.login),
//                        rootView
//                    ) {
//                        findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)
//                    }
//                }
            }

            R.id.nav_AddProduct -> {
//                if (Injector.getPreferenceHelper().isLoggedIn) {
                findNavController().navigate(R.id.choosePackageFragment)

//                } else {
//                   snackBarWithAction(
//                        getString(R.string.you_must_login),
//                        getString(R.string.login),
//                        rootView
//                    ) {
//                        findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)
//                    }
//                }
            }
            R.id.nav_About -> {
                findNavController().navigate(R.id.aboutUsFragment)
            }
            R.id.nav_Terms -> {
                findNavController().navigate(R.id.termsFragment)

            }
            R.id.nav_contactUs -> {
                findNavController().navigate(R.id.contactUsFragment)
            }
            R.id.nav_Settings -> {
                findNavController().navigate(R.id.settingsFragment)
            }

            R.id.nav_LogOut -> {
//                if (Injector.getPreferenceHelper().isLoggedIn) {

                findNavController().navigate(R.id.loginFragment)
//                } else {
//                    findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)
//                }
            }
        }
        rootView.closeDrawer(GravityCompat.START)
        return true
    }
}
