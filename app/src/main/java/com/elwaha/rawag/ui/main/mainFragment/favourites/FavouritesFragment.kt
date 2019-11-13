package com.elwaha.rawag.ui.main.mainFragment.favourites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews

import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.mainFragment.MainFragmentDirections
import com.elwaha.rawag.ui.main.mainFragment.ImageSliderAdapter
import com.elwaha.rawag.ui.main.profiles.ProfilesAdapter
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.favourite_fragment.*
import java.util.*
import kotlin.concurrent.timerTask

class FavouritesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private lateinit var viewModel: FavouritesViewModel
    private var adapter = ProfilesAdapter().also {
        it.onItemChildClickListener = this
    }
    private var loading: SpotsDialog? = null

    private var timer: Timer? = null
    private val imageSliderAdapter = ImageSliderAdapter {
        val images = viewModel.ads
        if (images.isNotEmpty()) {
            activity?.toast("clicked")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        viewModel.uiState.observe(this, androidx.lifecycle.Observer { onFavouritesResponse(it) })
        viewModel.uiStateEvent.observeEvent(this) { onLikeResponse(it) }

        imageSliderAdapter.submitList(viewModel.ads)
        bannerSliderVp.adapter = imageSliderAdapter
        bannerSliderVp.setPadding(80, 0, 80, 0);
        bannerSliderVp.pageMargin = 20
        bannerSliderVp.clipToPadding = false
        bannerSliderVp.adapter = imageSliderAdapter

        favouritesRv.adapter = adapter
        favouritesRv.setHasFixedSize(true)

        rootView.setLayout(favouritesNsv)
        rootView.setVisible(CustomViews.LAYOUT)

        if (viewModel.usersList.isEmpty() || viewModel.ads.isEmpty()) {
            viewModel.get()

        }
    }

    private fun onLikeResponse(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.wait))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                if (viewModel.lastLikeActionPosition != null && viewModel.lastLikeActionResult != null) {
                    if (viewModel.lastLikeActionResult!!) {
                        activity?.toast(getString(R.string.addedToFavourites))
                        adapter.data[viewModel.lastLikeActionPosition!!].isLiked = 1
                        viewModel.usersList[viewModel.lastLikeActionPosition!!].isLiked = 1
                        adapter.notifyItemChanged(viewModel.lastLikeActionPosition!!)
                    } else {
                        activity?.toast(getString(R.string.removedFromFavourites))
                        adapter.data[viewModel.lastLikeActionPosition!!].isLiked = 0
                        viewModel.usersList[viewModel.lastLikeActionPosition!!].isLiked = 0
                        adapter.notifyItemChanged(viewModel.lastLikeActionPosition!!)
                    }
                }
            }
            is ViewState.Error -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.noInternet))
            }
        }
    }


    private fun onFavouritesResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                messageTv.visibility = View.GONE

                rootView.setVisible(CustomViews.LOADING)
            }
            ViewState.Success -> {
                setData()
                rootView.setVisible(CustomViews.LAYOUT)
            }
            is ViewState.Error -> {
                messageTv.visibility = View.GONE

                rootView.setVisible(CustomViews.ERROR)
            }
            ViewState.NoConnection -> {
                messageTv.visibility = View.GONE

                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    viewModel.get()
                }
            }
            ViewState.Empty -> {
                messageTv.visibility = View.GONE

                rootView.setVisible(CustomViews.EMPTY)
            }
            ViewState.LastPage -> {

            }
            null -> {

            }
        }
    }

    private fun setData() {
        imageSliderAdapter.submitList(viewModel.ads)
        bannerSliderVp.setPadding(80, 0, 80, 0);
        bannerSliderVp.pageMargin = 20
        bannerSliderVp.clipToPadding = false
        bannerSliderVp.adapter = imageSliderAdapter

        if (viewModel.usersList.isEmpty()) {
            messageTv.visibility = View.VISIBLE
        } else {
            messageTv.visibility = View.GONE
            adapter.replaceData(viewModel.usersList)
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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.profileItem -> {
                val action = MainFragmentDirections.actionMainFragmentToProfileFragment(
                    viewModel.usersList[position].id.toString(),
                    false
                )
                findNavController().navigate(action)
            }

            R.id.favImgv -> {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    viewModel.like(viewModel.usersList[position].id.toString(), position)
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
    }
}
