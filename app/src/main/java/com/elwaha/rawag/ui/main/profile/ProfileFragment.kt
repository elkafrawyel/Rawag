package com.elwaha.rawag.ui.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.elkafrawyel.CustomViews
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.ui.main.mainFragment.ImageSliderAdapter
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.*
import kotlin.concurrent.timerTask


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var loadingDialog: SpotsDialog? = null

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
        viewModel.uiState.observe(
            this,
            androidx.lifecycle.Observer { onProfileActionsResponse(it) })
        viewModel.uiStateEvent.observeEvent(this) { onLikeResponse(it) }

        rootView.setLayout(profileCl)
        rootView.setVisible(CustomViews.LAYOUT)

        arguments?.let {
            val isMyAccount = ProfileFragmentArgs.fromBundle(it).isMyProfile
            if (isMyAccount) {
                myAccountData()

            } else {

                val userId = ProfileFragmentArgs.fromBundle(it).userId

                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                if (userId == user.id.toString()){
                    //my account too
                    myAccountData()
                }else{
                    viewModel.userId = userId
                    viewModel.get(ProfileViewModel.ProfileActions.GET_PROFILE)

                    otherUserData()
                }

            }
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        editImgv.setOnClickListener { findNavController().navigate(R.id.editProfileFragment) }

        watchMyAdsMbtn.setOnClickListener { findNavController().navigate(R.id.myAdsFragment) }

        showComments.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToCommentsFragment(
                    viewModel.userId!!
                )
            findNavController().navigate(action)
        }

        callMbtn.setOnClickListener {
            if (viewModel.user != null) {
                val uri = "tel:" + viewModel.user!!.phone
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(uri)
                activity?.startActivity(intent)
            }
        }

        favImgv.setOnClickListener {
            if (Injector.getPreferenceHelper().isLoggedIn) {
                viewModel.like(viewModel.userId!!)
            } else {
                activity?.snackBarWithAction(
                    getString(R.string.you_must_login),
                    getString(R.string.login),
                    rootView
                ) {
                    findNavController().navigate(R.id.action_profilesFragment_to_loginFragment)
                }
            }
        }
    }

    private fun otherUserData() {
        callMbtn.visibility = View.VISIBLE
        watchMyAdsMbtn.visibility = View.GONE
        favImgv.visibility = View.VISIBLE
        editImgv.visibility = View.GONE
    }

    private fun myAccountData() {
        callMbtn.visibility = View.GONE
        watchMyAdsMbtn.visibility = View.VISIBLE
        favImgv.visibility = View.GONE
        editImgv.visibility = View.VISIBLE

        val userString = Injector.getPreferenceHelper().user
        val user = ObjectConverter().getUser(userString!!)
        viewModel.user = user
        viewModel.userId = user.id.toString()
        setProfileData(user)
    }

    private fun onLikeResponse(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loadingDialog = activity?.showLoading(getString(R.string.wait))
                loadingDialog!!.show()
            }
            ViewState.Success -> {
                if (loadingDialog != null) {
                    loadingDialog!!.dismiss()
                }
                if (viewModel.lastLikeActionResult != null) {
                    if (viewModel.lastLikeActionResult!!) {
                        activity?.toast(getString(R.string.addedToFavourites))
                        favImgv.loadWithPlaceHolder(R.drawable.likeee)
                        viewModel.user!!.isLiked = 1
                    } else {
                        activity?.toast(getString(R.string.removedFromFavourites))
                        favImgv.loadWithPlaceHolder(R.drawable.like)
                        viewModel.user!!.isLiked = 0
                    }
                }
            }
            is ViewState.Error -> {
                if (loadingDialog != null) {
                    loadingDialog!!.dismiss()
                }
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {
                if (loadingDialog != null) {
                    loadingDialog!!.dismiss()
                }
                activity?.toast(getString(R.string.noInternet))
            }
        }
    }

    private fun onProfileActionsResponse(state: ViewState?) {
        when (viewModel.action) {
            ProfileViewModel.ProfileActions.GET_PROFILE -> {
                when (state) {
                    ViewState.Loading -> {
                        loading.visibility = View.VISIBLE
                    }

                    ViewState.Success -> {
                        loading.visibility = View.GONE
                        setProfileData(viewModel.user!!)
                        if (viewModel.user!!.isLiked == 1) {
                            favImgv.loadWithPlaceHolder(R.drawable.likeee)
                        } else {
                            favImgv.loadWithPlaceHolder(R.drawable.like)
                        }
                    }
                    is ViewState.Error -> {
                        loading.visibility = View.GONE
                        activity?.toast(state.message)
                    }
                    ViewState.NoConnection -> {
                        loading.visibility = View.GONE
                        activity?.toast(getString(R.string.noInternet))
                    }

                }
            }

            ProfileViewModel.ProfileActions.GET_ADS -> {
                when (state) {
                    ViewState.Loading -> {
                        loading.visibility = View.VISIBLE
                        noAdsTv.visibility = View.GONE
                        pageIndicator.visibility = View.INVISIBLE
                        bannerSliderVp.visibility = View.INVISIBLE
                    }
                    ViewState.Success -> {
                        loading.visibility = View.GONE
                        noAdsTv.visibility = View.GONE
                        pageIndicator.visibility = View.VISIBLE
                        bannerSliderVp.visibility = View.VISIBLE

                        imageSliderAdapter.submitList(viewModel.images)
                        bannerSliderVp.setPadding(80, 0, 80, 0);
                        bannerSliderVp.pageMargin = 20
                        bannerSliderVp.clipToPadding = false
                        bannerSliderVp.adapter = imageSliderAdapter
                    }
                    is ViewState.Error -> {
                        loading.visibility = View.GONE
                        noAdsTv.visibility = View.GONE
                        pageIndicator.visibility = View.INVISIBLE
                        bannerSliderVp.visibility = View.INVISIBLE
                        activity?.toast(state.message)
                    }
                    ViewState.NoConnection -> {
                        loading.visibility = View.GONE
                        noAdsTv.visibility = View.GONE
                        pageIndicator.visibility = View.INVISIBLE
                        bannerSliderVp.visibility = View.INVISIBLE

                        activity?.toast(getString(R.string.noInternet))
                    }
                    ViewState.Empty -> {
                        pageIndicator.visibility = View.INVISIBLE
                        bannerSliderVp.visibility = View.INVISIBLE
                        loading.visibility = View.GONE
                        noAdsTv.visibility = View.VISIBLE
                        noAdsTv.text = getString(R.string.you_have_no_ads)
                    }
                }
            }
        }

    }

    private fun openUrl(url: String) {
        if (url != "") {
            try {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(url)
                startActivity(openURL)
            } catch (e: Exception) {
                activity?.toast(getString(R.string.error_link))
            }

        } else {
            activity?.toast(getString(R.string.no_link))
        }
    }

    private fun setProfileData(user: UserModel) {
        Glide.with(context!!).load(Constants.IMAGES_BASE_URL + user.avatar).into(profileUserImage)
        profileName.text = user.name
        profileRateBar.rating = user.rate_value.toFloat()
        profileAddress.text = user.address
        profileViews.text = user.views.toString()
        profileAddress.isSelected = true
        descTv.text = user.about

        youtube.setOnClickListener { openUrl(user.youtube) }
        facebook.setOnClickListener { openUrl(user.facebook) }
        twitter.setOnClickListener { openUrl(user.twitter) }
        snap.setOnClickListener { openUrl(user.snabchat) }
        ins.setOnClickListener { openUrl(user.instagram) }

        viewModel.get(ProfileViewModel.ProfileActions.GET_ADS)

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
