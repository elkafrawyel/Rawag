package com.elwaha.rawag.ui.main.contactUs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.requests.AddContactRequest
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.contact_us_fragment.*

class ContactUsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactUsFragment()
    }
    private var loading: SpotsDialog? = null

    private lateinit var viewModel: ContactUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onContactResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }

        saveChangesMbtn.setOnClickListener {
            contactUs()
        }
    }

    private fun onContactResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.contactingUS))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.contactSuccess))
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

    private fun contactUs() {

        if (userNameEt.text.toString().isEmpty()) {
            userNameEt.setEmptyError(context!!)
            return
        }

        if (phoneEt.text.toString().isEmpty()) {
            phoneEt.setEmptyError(context!!)
            return
        }
        if (emailEt.text.toString().isEmpty()) {
            emailEt.setEmptyError(context!!)
            return
        }
        if (messageEt.text.toString().isEmpty()) {
            messageEt.setEmptyError(context!!)
            return
        }

        val addContactModel = AddContactRequest(
            emailEt.text.toString(),
            messageEt.text.toString(),
            userNameEt.text.toString(),
            phoneEt.text.toString()
        )

        viewModel.contactUs(addContactModel)
    }

}
