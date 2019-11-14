package com.elwaha.rawag.ui.main.search

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
import com.elwaha.rawag.ui.main.profiles.ProfilesAdapter
import com.elwaha.rawag.ui.main.profiles.ProfilesFragmentArgs
import com.elwaha.rawag.ui.main.profiles.ProfilesFragmentDirections
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private var loading: SpotsDialog? = null
    private var adapter = ProfilesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onUsersResponse(it) })
        viewModel.uiStateEvent.observeEvent(this) { onLikeResponse(it) }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        productsRv.adapter = adapter
        productsRv.setHasFixedSize(true)

        searchMbtn.setOnClickListener {
            if (searchEt.text.toString().isEmpty()) {
                searchEt.setEmptyError(context!!)
                return@setOnClickListener
            }else{
                viewModel.search(searchEt.text.toString())
            }
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

    private fun onUsersResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loadingPb.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                setData()
                loadingPb.visibility = View.GONE
            }
            is ViewState.Error -> {
                loadingPb.visibility = View.GONE
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {
                loadingPb.visibility = View.GONE
                activity?.toast(getString(R.string.noInternet))
            }
            ViewState.Empty -> {
                loadingPb.visibility = View.GONE
                adapter.data.clear()
                viewModel.usersList.clear()
                adapter.notifyDataSetChanged()
                activity?.toast(getString(R.string.no_search_result))
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
                val action = SearchFragmentDirections.actionSearchFragmentToProfileFragment(
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
                        findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}
