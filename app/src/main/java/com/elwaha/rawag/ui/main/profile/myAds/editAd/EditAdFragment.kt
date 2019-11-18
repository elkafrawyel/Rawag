package com.elwaha.rawag.ui.main.profile.myAds.editAd

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.data.models.requests.EditAdRequest
import com.elwaha.rawag.ui.main.MainViewModel
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.observeEvent
import com.elwaha.rawag.utilies.showLoading
import com.elwaha.rawag.utilies.toast
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.edit_ad_fragment.*

class EditAdFragment : Fragment() {

    companion object {
        fun newInstance() = EditAdFragment()
    }
    private var loading: SpotsDialog? = null

    private lateinit var viewModel: EditAdViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_ad_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditAdViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainViewModel.uiState.observe(this, Observer { onMainResponse(it) })
        mainViewModel.action = MainViewModel.MainActions.CATEGORIES
        viewModel.uiStateEvent.observeEvent(this) { onEditInfoResponse(it) }

        arguments?.let {
            val adModel = EditAdFragmentArgs.fromBundle(it).adModel
            viewModel.ad = adModel
        }

        editInfoMbtn.setOnClickListener {
            editAdInfo()
        }
    }

    private fun onEditInfoResponse(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.msg_please_wait))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.saved))
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

    private fun editAdInfo() {
        if (viewModel.selectedCategory!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.select_category))
            return
        }

        if (viewModel.selectedSubCategory!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.select_sub_category))
            return
        }

        if (viewModel.selectedCountry!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.select_country))
            return
        }

        if (viewModel.selectedCity!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.select_city))
            return
        }
        viewModel.editInfo(
            EditAdRequest(
                viewModel.selectedCategory!!.id.toString(),
                viewModel.selectedCity!!.id.toString(),
                viewModel.ad!!.id.toString()
            )
        )
    }

    private fun onMainResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {

            }
            ViewState.Success -> {
                when (mainViewModel.action) {
                    MainViewModel.MainActions.CATEGORIES -> {
                        addCategoriesToSpinner()
                        //just initialized with first object
                        addSubCategoriesToSpinner()

                        mainViewModel.get(MainViewModel.MainActions.COUNTRIES)

                    }
                    MainViewModel.MainActions.SUB_CATEGORIES -> {
                        addSubCategoriesToSpinner()

                    }
                    MainViewModel.MainActions.COUNTRIES -> {
                        addCountriesToSpinner()

                        addCitiesToSpinner()
                    }
                    MainViewModel.MainActions.CITIES -> {
                        addCitiesToSpinner()
                    }
                }
            }
            is ViewState.Error -> {
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {

            }

        }
    }


    private fun addCategoriesToSpinner() {
        val adapterCategories = ArrayAdapter<CategoryModel>(
            context!!,
            android.R.layout.simple_spinner_item,
            mainViewModel.categoriesList
        )

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = adapterCategories
        categoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selectedCategory = mainViewModel.categoriesList[position]
                    //call subCategory when select category
                    if (viewModel.selectedCategory!!.id != mainViewModel.fakeId) {
                        mainViewModel.categoryId = viewModel.selectedCategory!!.id.toString()
                        mainViewModel.get(MainViewModel.MainActions.SUB_CATEGORIES)
                    }
                }
            }
    }


    private fun addSubCategoriesToSpinner() {
        val adapterSubCategories = ArrayAdapter<CategoryModel>(
            context!!,
            android.R.layout.simple_spinner_item,
            mainViewModel.subCategoriesList
        )

        adapterSubCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subCategoriesSpinner.adapter = adapterSubCategories
        subCategoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selectedSubCategory = mainViewModel.subCategoriesList[position]
                }
            }
    }

    private fun addCountriesToSpinner() {
        val adapterCountries = ArrayAdapter<CountryModel>(
            context!!,
            android.R.layout.simple_spinner_item,
            mainViewModel.countriesList
        )

        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countriesSpinner.adapter = adapterCountries
        countriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selectedCountry = mainViewModel.countriesList[position]
                    //call subCategory when select category
                    if (viewModel.selectedCountry!!.id != mainViewModel.fakeId) {
                        mainViewModel.countryId = viewModel.selectedCountry!!.id.toString()
                        mainViewModel.get(MainViewModel.MainActions.CITIES)
                    }
                }
            }
    }

    private fun addCitiesToSpinner() {
        val adapterCities = ArrayAdapter<CityModel>(
            context!!,
            android.R.layout.simple_spinner_item,
            mainViewModel.citiesList
        )

        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citiesSpinner.adapter = adapterCities
        citiesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selectedCity = mainViewModel.citiesList[position]
                }
            }
    }

}
