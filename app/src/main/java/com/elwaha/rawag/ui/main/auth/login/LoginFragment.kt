package com.elwaha.rawag.ui.main.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.observeEvent
import com.elwaha.rawag.utilies.showLoading
import com.elwaha.rawag.utilies.toast
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var loading: SpotsDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.uiStateEvent.observeEvent(this) { onResponseEvent(it) }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        register.setOnClickListener { findNavController().navigate(R.id.registerFragment) }

        forgetPassword.setOnClickListener { findNavController().navigate(R.id.resetPasswordFragment) }

        skipMbtn.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, true)
        }

        loginMbtn.setOnClickListener {
            if (userNameEt.text.toString().isEmpty()) {
                userNameEt.error = getString(R.string.empty_field)
                return@setOnClickListener
            }

            if (passwordEt.text.toString().isEmpty()) {
                passwordEt.error = getString(R.string.empty_field)
                return@setOnClickListener
            }

            viewModel.login(userNameEt.text.toString(), passwordEt.text.toString())
        }

    }

    private fun onResponseEvent(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.loggingIn))
                loading!!.show()
            }
            ViewState.Success -> {
                loading!!.dismiss()
                activity?.toast(getString(R.string.login_success))
                findNavController().popBackStack(R.id.mainFragment, true)
            }
            is ViewState.Error -> {
                loading!!.dismiss()
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {
                loading!!.dismiss()
                activity?.toast(getString(R.string.noInternet))
            }
        }
    }
}
