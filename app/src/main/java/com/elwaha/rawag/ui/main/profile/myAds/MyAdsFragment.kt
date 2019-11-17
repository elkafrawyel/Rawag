package com.elwaha.rawag.ui.main.profile.myAds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.my_ads_fragment.*

class MyAdsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    companion object {
        fun newInstance() = MyAdsFragment()
    }

    private lateinit var viewModel: MyAdsViewModel
    private var adapter = MyAdsAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_ads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyAdsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onAdsResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }

        arguments?.let {
            val userId = MyAdsFragmentArgs.fromBundle(it).userId
            viewModel.userId = userId
            viewModel.getMyAds()
        }

        myAdsRv.adapter = adapter
        myAdsRv.setHasFixedSize(true)

    }

    private fun onAdsResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {

            }
            ViewState.Success -> {
                setData()
            }
            is ViewState.Error -> {

            }
            ViewState.NoConnection -> {

            }
        }
    }

    private fun setData() {
        adapter.replaceData(viewModel.images)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.editImgv -> {
                activity?.toast("Edit")
            }

            R.id.cancelImgv -> {
                activity?.toast("Cancel")
            }
        }
    }
}
