package com.elwaha.rawag.ui.main.choosePackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import kotlinx.android.synthetic.main.choose_package_fragment.*

class ChoosePackageFragment : Fragment() {

    companion object {
        fun newInstance() = ChoosePackageFragment()
    }

    private lateinit var viewModel: ChoosePackageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_package_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChoosePackageViewModel::class.java)
        backImgv.setOnClickListener { findNavController().navigateUp() }
        diamondPackageApply.setOnClickListener { findNavController().navigate(R.id.addProductFragment) }
        goldPackageApply.setOnClickListener { findNavController().navigate(R.id.addProductFragment) }
        silverPackageApply.setOnClickListener { findNavController().navigate(R.id.addProductFragment) }
    }

}
