package com.elwaha.rawag.ui.main.auth.newPassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.requests.UpdatePasswordRequest
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.new_password_fragment.*

class NewPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = NewPasswordFragment()
    }

    private lateinit var viewModel: NewPasswordViewModel
    private var loading: SpotsDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewPasswordViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onUpdateResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }

        confirmMbtn.setOnClickListener {
            changePassword()
        }
    }

    private fun onUpdateResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.wait))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.password_change_success))
                activity?.finish()
                activity?.restartApplication()
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

    private fun changePassword() {

        if (oldPasswordEt.text.toString().isEmpty()) {
            oldPasswordEt.setEmptyError(context!!)
            return
        }

        if (newPasswordEt.text.toString().isEmpty()) {
            newPasswordEt.setEmptyError(context!!)
            return
        }

        if (confirmPasswordEt.text.toString().isEmpty()) {
            confirmPasswordEt.setEmptyError(context!!)
            return
        }

        val request = UpdatePasswordRequest(
            oldPasswordEt.text.toString(),
            newPasswordEt.text.toString(),
            confirmPasswordEt.text.toString()
        )

        viewModel.changePassword(request)
    }

}
