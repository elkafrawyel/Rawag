package com.elwaha.rawag.ui.main.termsAndCondition

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
import kotlinx.android.synthetic.main.terms_fragment.*

class TermsFragment : Fragment() {

    companion object {
        fun newInstance() = TermsFragment()
    }

    private lateinit var viewModel: TermsViewModel
    private var loading: SpotsDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.terms_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TermsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onTermsResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }
    }

    private fun onTermsResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.loggingIn))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                termsTv.text = viewModel.terms
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
