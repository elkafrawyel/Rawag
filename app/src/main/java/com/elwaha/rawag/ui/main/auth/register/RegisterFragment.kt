package com.elwaha.rawag.ui.main.auth.register

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
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.requests.RegisterRequest
import com.elwaha.rawag.ui.main.MainViewModel
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.utilies.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.register_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


const val RC_PERMISSION_STORAGE = 111
const val RC_AVATAR = 112
const val RC_IMAGES = 113
const val RC_PLACE_PICKER = 114

class RegisterFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private var loading: SpotsDialog? = null
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mainViewModel: MainViewModel
    var adapter = ImagesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.uiStateEvent.observeEvent(this) { onRegisterResponse(it) }
        mainViewModel.uiState.observe(this, Observer { onMainResponse(it) })
        mainViewModel.action = MainViewModel.MainActions.CATEGORIES

        locationTv.isSelected = true

        backImgv.setOnClickListener { findNavController().navigateUp() }
        termsTv.setOnClickListener { findNavController().navigate(R.id.termsFragment) }
        login.setOnClickListener { findNavController().navigate(R.id.loginFragment) }
        chooseProfileImage.setOnClickListener { chooseAvatar() }
        pickImages.setOnClickListener { chooseBackgroundImages() }
        pickLocation.setOnClickListener { openPlacePicker() }
        newAccountMbtn.setOnClickListener { checkStorage() }
    }

    private fun onRegisterResponse(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.creatingNewAccount))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.login_success))
                findNavController().popBackStack(R.id.mainFragment, true)
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
                    }
                    MainViewModel.MainActions.SUB_CATEGORIES -> {
                        addSubCategoriesToSpinner()
                    }
                    null -> {

                    }
                }
            }
            is ViewState.Error -> {
                activity?.toast(state.message)
            }
            ViewState.NoConnection -> {

            }

            null -> {

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
                    if (viewModel.selectedCategory!!.id != mainViewModel.fakeCategoryId) {
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

    private fun openPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            // for activity
            // startActivityForResult(builder.build(this), RC_PLACE_PICKER)
            // for fragment
            startActivityForResult(builder.build(activity), RC_PLACE_PICKER);
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    private fun chooseAvatar() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivityForResult(intent, RC_AVATAR)
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

                RC_AVATAR -> {
                    val uri = data?.data
                    if (uri != null) {
                        registerUserImage.setImageURI(uri)
                        viewModel.avatarUri = uri
                    } else {
                        activity?.toast(getString(R.string.errorSelectImage))
                    }
                }

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
                        }
                        uri != null -> {
                            uriList.add(uri)
                            adapter.addData(uri)

                        }
                        else -> activity?.toast(getString(R.string.errorSelectImage))
                    }

                    imagesRv.setHasFixedSize(true)
                    imagesRv.adapter = adapter
                }

                RC_PLACE_PICKER -> {
                    val place = PlacePicker.getPlace(activity, data)
                    val lat = place.latLng.latitude
                    viewModel.lat = lat.toString()
                    val lang = place.latLng.longitude
                    viewModel.lang = lang.toString()
                    val address = place.address!!.toString()
                    viewModel.address = address
                    locationTv.text = address
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

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            register()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun register() {
        if (viewModel.avatarUri == null) {
            activity?.toast(getString(R.string.choose_avatar))
            return
        }

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

        if (viewModel.lang == null || viewModel.lat == null || viewModel.address == null) {
            activity?.toast("We need your address")
            return
        }

        if (viewModel.selectedCategory == null || viewModel.selectedCategory!!.id == mainViewModel.fakeCategoryId) {
            activity?.toast(getString(R.string.choose_category))
            return
        }

        if (viewModel.selectedSubCategory == null || viewModel.selectedSubCategory!!.id == mainViewModel.fakeCategoryId) {
            activity?.toast(getString(R.string.choose_category))
            return
        }

        if (viewModel.selectedSubCategory == null) {
            activity?.toast(getString(R.string.select_sub_category))
            return
        }

        if (passwordEt.text.toString().isEmpty()) {
            passwordEt.setEmptyError(context!!)
            return
        }

        if (descEt.text.toString().isEmpty()) {
            descEt.setEmptyError(context!!)
            return
        }

        if (!termsCheckBox.isChecked) {
            activity?.toast(getString(R.string.agree_to_terms))
            return
        }

        val registerRequest = RegisterRequest(
            userNameEt.text.toString(), emailEt.text.toString(),
            phoneEt.text.toString(),
            passwordEt.text.toString(),
            viewModel.address!!,
            viewModel.lang!!,
            viewModel.lat!!,
            descEt.text.toString(),
            viewModel.selectedSubCategory!!.id.toString(),
            accepted = "1"
        )

        viewModel.register(registerRequest, viewModel.avatarUri!!)
    }
}
