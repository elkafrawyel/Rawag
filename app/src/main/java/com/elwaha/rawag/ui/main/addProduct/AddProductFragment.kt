package com.elwaha.rawag.ui.main.addProduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.AdImages
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.ui.main.MainViewModel
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.ui.main.auth.register.RC_IMAGES
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.add_product_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class AddProductFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var mainViewModel: MainViewModel
    var adapter = ImagesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainViewModel.uiState.observe(this, Observer { onMainResponse(it) })
        mainViewModel.action = MainViewModel.MainActions.CATEGORIES


        arguments?.let {
            val baqaId = AddProductFragmentArgs.fromBundle(it).baqaId
            viewModel.baqaId = baqaId
            val price = AddProductFragmentArgs.fromBundle(it).price
            viewModel.price = price

            priceTodayTv.text = "$price ${getString(R.string.currency)}"
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }
        pickImages.setOnClickListener { chooseBackgroundImages() }
        confirmMbtn.setOnClickListener { addProduct() }

        val adapterDays = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.days
        )

        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daysSpinner.adapter = adapterDays
        daysSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selectedDays = viewModel.days[position]
                }
            }

        if (viewModel.uriList.isNotEmpty()){
            adapter.replaceData(viewModel.uriList)
            imagesRv.setHasFixedSize(true)
            imagesRv.adapter = adapter
        }
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


    private fun chooseBackgroundImages() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, RC_IMAGES)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_IMAGES -> {
                    val uriList = ArrayList<Uri>()
                    val result = data?.clipData
                    val uri = data?.data
                    when {
                        result != null -> {
                            for (i in 0 until result.itemCount) {
                                uriList.add(result.getItemAt(i).uri)
                            }
                            adapter.addData(uriList)
                            viewModel.uriList.addAll(uriList)
                        }
                        uri != null -> {
                            uriList.add(uri)
                            viewModel.uriList.add(uri)
                            adapter.addData(uri)

                        }
                        else -> activity?.toast(getString(R.string.errorSelectImage))
                    }

                    imagesRv.setHasFixedSize(true)
                    imagesRv.adapter = adapter
                }
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.delete -> {
                (adapter?.data as ArrayList<*>).removeAt(position)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun addProduct() {

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

        if (adapter.data.isEmpty()) {
            activity?.toast(getString(R.string.choose_images))
        }

        val adImages = AdImages(adapter.data.map {
            it.toString()
        })

        val action =
            AddProductFragmentDirections.actionAddProductFragmentToChoosePaymentMethodFragment(
                viewModel.selectedSubCategory!!.id.toString(),
                viewModel.selectedCity!!.id.toString(),
                viewModel.price!!,
                viewModel.baqaId!!,
                adImages,
                viewModel.selectedDays.toString()
            )
        findNavController().navigate(action)
    }
}
