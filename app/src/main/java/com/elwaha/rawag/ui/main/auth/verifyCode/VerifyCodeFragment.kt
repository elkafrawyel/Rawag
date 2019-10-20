package com.elwaha.rawag.ui.main.auth.verifyCode

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import kotlinx.android.synthetic.main.verify_code_fragment.*

class VerifyCodeFragment : Fragment() {

    companion object {
        fun newInstance() = VerifyCodeFragment()
    }

    private lateinit var viewModel: VerifyCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verify_code_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VerifyCodeViewModel::class.java)
        backImgv.setOnClickListener { findNavController().navigateUp() }
        confirmMbtn.setOnClickListener { findNavController().navigate(R.id.newPasswordFragment)}
    }

}
