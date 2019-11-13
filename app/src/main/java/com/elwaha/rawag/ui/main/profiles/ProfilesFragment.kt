package com.elwaha.rawag.ui.main.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.profiles_fragment.*
import kotlinx.android.synthetic.main.profiles_fragment.rootView

class ProfilesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = ProfilesFragment()
    }

    private var loading: SpotsDialog? = null

    private lateinit var viewModel: ProfilesViewModel
    private var adapter = ProfilesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profiles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfilesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onUsersResponse(it) })
        viewModel.uiStateEvent.observeEvent(this) { onLikeResponse(it) }
        arguments?.let {
            val subCategoryId = ProfilesFragmentArgs.fromBundle(it).subCategoryId
            viewModel.subCategoryId = subCategoryId
            if (viewModel.usersList.isEmpty())
                viewModel.getUsers()
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }
        productsRv.adapter = adapter
        productsRv.setHasFixedSize(true)

        rootView.setLayout(profilesCl)
        rootView.setVisible(CustomViews.LAYOUT)
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

    private fun onUsersResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                rootView.setVisible(CustomViews.LOADING)
            }
            ViewState.Success -> {
                setData()
                rootView.setVisible(CustomViews.LAYOUT)
            }
            is ViewState.Error -> {
                rootView.setVisible(CustomViews.ERROR)
            }
            ViewState.NoConnection -> {
                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    viewModel.getUsers()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
            }
            ViewState.LastPage -> {

            }
            null -> {

            }
        }
    }

    private fun setData() {
        adapter.replaceData(viewModel.usersList)
        adapter.notifyDataSetChanged()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.profileItem -> {
                val action =
                    ProfilesFragmentDirections.actionProfilesFragmentToProfileFragment(
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
