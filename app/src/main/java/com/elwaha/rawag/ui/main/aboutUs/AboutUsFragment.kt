package com.elwaha.rawag.ui.main.aboutUs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.showLoading
import com.elwaha.rawag.utilies.toast
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.about_us_fragment.*

class AboutUsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var viewModel: AboutUsViewModel
    private var loading: SpotsDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onAboutResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }
    }

    private fun onAboutResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.loggingIn))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                aboutTv.text = viewModel.about
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

}
