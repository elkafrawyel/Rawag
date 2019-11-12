package com.elwaha.rawag.ui.main.choosePackage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elkafrawyel.CustomViews
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
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
        rootView.setLayout(choosePackageSv)
        viewModel = ViewModelProviders.of(this).get(ChoosePackageViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }

        diamondPackageApply.setOnClickListener {
            val action =
                ChoosePackageFragmentDirections.actionChoosePackageFragmentToAddProductFragment(
                    viewModel.allBaqas[1].id.toString(),
                    viewModel.allBaqas[1].price.toString()
                )
            findNavController().navigate(action)
        }
        goldPackageApply.setOnClickListener {
            val action =
                ChoosePackageFragmentDirections.actionChoosePackageFragmentToAddProductFragment(
                    viewModel.allBaqas[0].id.toString(),
                    viewModel.allBaqas[0].price.toString()
                )
            findNavController().navigate(action)
        }
        silverPackageApply.setOnClickListener {
            val action =
                ChoosePackageFragmentDirections.actionChoosePackageFragmentToAddProductFragment(
                    viewModel.allBaqas[2].id.toString(),
                    viewModel.allBaqas[2].price.toString()

                )
            findNavController().navigate(action)
        }
    }

    private fun onResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                rootView.setVisible(CustomViews.LOADING)
            }
            ViewState.Success -> {
                setData()
                rootView.setVisible(CustomViews.LAYOUT)
            }
            is ViewState.Error -> {
                rootView.setVisible(CustomViews.ERROR)
            }
            ViewState.NoConnection -> {
                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    viewModel.getAllBaqas()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        val gold = viewModel.allBaqas[0]
        if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value) {
            goldPackageNameTv.text = gold.nameAr
        } else {
            goldPackageNameTv.text = gold.nameEn
        }
        goldPackagePriceTv.text = "${gold.price} ${getString(R.string.currency)}"
        goldPackageDecsTv.text = gold.content


        val diamon = viewModel.allBaqas[1]

        if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value) {
            diamondPackageNameTv.text = diamon.nameAr
        } else {
            diamondPackageNameTv.text = diamon.nameEn
        }
        diamondPackagePriceTv.text = "${diamon.price} ${getString(R.string.currency)}"
        diamondPackageDecsTv.text = diamon.content


        val silver = viewModel.allBaqas[2]
        if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value) {
            silverPackageNameTv.text = silver.nameAr
        } else {
            silverPackageNameTv.text = silver.nameEn
        }
        silverPackagePriceTv.text = "${silver.price} ${getString(R.string.currency)}"
        silverPackageDecsTv.text = silver.content

    }

}
