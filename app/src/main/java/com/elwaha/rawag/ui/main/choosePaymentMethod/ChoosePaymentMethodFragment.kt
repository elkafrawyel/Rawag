package com.elwaha.rawag.ui.main.choosePaymentMethod

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import kotlinx.android.synthetic.main.choose_payment_method_fragment.*

class ChoosePaymentMethodFragment : Fragment() {

    companion object {
        fun newInstance() =
            ChoosePaymentMethodFragment()
    }

    private lateinit var viewModel: ChoosePaymentMethodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_payment_method_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChoosePaymentMethodViewModel::class.java)
        backImgv.setOnClickListener { findNavController().navigateUp() }
    }

}
