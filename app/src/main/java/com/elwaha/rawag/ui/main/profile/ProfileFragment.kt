package com.elwaha.rawag.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.toast
import com.elwaha.rawag.ui.main.mainFragment.ImageSliderAdapter
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.*
import kotlin.concurrent.timerTask

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private var timer: Timer? = null
    private val imageSliderAdapter =
        ImageSliderAdapter {
            val images = viewModel.images
            if (images.isNotEmpty()) {
                activity?.toast("clicked")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        arguments?.let {
            val isMyAccount = com.elwaha.rawag.ui.main.profile.ProfileFragmentArgs.fromBundle(it).isMyProfile
            if (isMyAccount){
                callMbtn.visibility = View.GONE
                watchMyAdsMbtn.visibility = View.VISIBLE
                favImgv.visibility = View.GONE
                editImgv.visibility = View.VISIBLE

                // call with token
            }else{
                callMbtn.visibility = View.VISIBLE
                watchMyAdsMbtn.visibility = View.GONE
                favImgv.visibility = View.VISIBLE
                editImgv.visibility = View.GONE

                val userId = com.elwaha.rawag.ui.main.profile.ProfileFragmentArgs.fromBundle(it).userId
                activity?.toast(userId!!)

                //call with user id
            }

        }

        imageSliderAdapter.submitList(viewModel.images)
        bannerSliderVp.setPadding(80, 0, 80, 0);
        bannerSliderVp.pageMargin = 20
        bannerSliderVp.clipToPadding = false
        bannerSliderVp.adapter = imageSliderAdapter

        backImgv.setOnClickListener { findNavController().navigateUp() }

        editImgv.setOnClickListener { findNavController().navigate(R.id.editProfileFragment) }

        watchMyAdsMbtn.setOnClickListener { findNavController().navigate(R.id.myAdsFragment) }

        showComments.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToCommentsFragment(
                    "test"
                )
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < imageSliderAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 5000)
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }


}
