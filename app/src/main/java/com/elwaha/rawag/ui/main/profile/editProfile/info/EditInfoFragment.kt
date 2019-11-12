package com.elwaha.rawag.ui.main.profile.editProfile.info

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.EditProfileRequest
import com.elwaha.rawag.ui.main.MainViewModel
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.ui.main.auth.register.RC_AVATAR
import com.elwaha.rawag.ui.main.auth.register.RC_IMAGES
import com.elwaha.rawag.ui.main.auth.register.RC_PLACE_PICKER
import com.elwaha.rawag.ui.main.profile.editProfile.EditProfileFragmentDirections
import com.elwaha.rawag.utilies.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.edit_info_fragment.*
import kotlinx.android.synthetic.main.edit_info_fragment.chooseProfileImage
import kotlinx.android.synthetic.main.edit_info_fragment.descEt
import kotlinx.android.synthetic.main.edit_info_fragment.emailEt
import kotlinx.android.synthetic.main.edit_info_fragment.locationTv
import kotlinx.android.synthetic.main.edit_info_fragment.phoneEt
import kotlinx.android.synthetic.main.edit_info_fragment.pickLocation
import kotlinx.android.synthetic.main.edit_info_fragment.userNameEt
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class EditInfoFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = EditInfoFragment()
    }

    private lateinit var viewModel: EditInfoViewModel
    private var user: UserModel? = null
    private lateinit var mainViewModel: MainViewModel
    private var loading: SpotsDialog? = null

    var adapter = ImagesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditInfoViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)


        mainViewModel.uiState.observe(this, Observer { onMainResponse(it) })
        mainViewModel.action = MainViewModel.MainActions.CATEGORIES

        viewModel.uiState.observe(this, Observer { onEditProfileResponse(it) })


        locationTv.isSelected = true
        chooseProfileImage.setOnClickListener { chooseAvatar() }
        pickLocation.setOnClickListener { openPlacePicker() }
        changePasswordMbtn.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToNewPasswordFragment()
            findNavController().navigate(action)
        }
        val userString = Injector.getPreferenceHelper().user
        user = ObjectConverter().getUser(userString!!)
        setProfileData(user!!)

        saveChangesMbtn.setOnClickListener {
            checkStorage()
        }
    }

    private fun onEditProfileResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.changingProfile))
                loading!!.show()
                loading!!.setOnDismissListener {
                    viewModel.cancel()
                }
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.edit_profile_success))
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
        editProfileCategoriesSpinner.adapter = adapterCategories
        editProfileCategoriesSpinner.onItemSelectedListener =
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

        mainViewModel.categoriesList.forEachIndexed { index, categoryModel ->
            if (user!!.categoryId == categoryModel.id) {
                editProfileCategoriesSpinner.setSelection(index)
                viewModel.selectedCategory = categoryModel
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
        editProfileSubCategoriesSpinner.adapter = adapterSubCategories
        editProfileSubCategoriesSpinner.onItemSelectedListener =
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
        mainViewModel.subCategoriesList.forEachIndexed { index, categoryModel ->
            if (user!!.subCategoryId == categoryModel.id) {
                editProfileSubCategoriesSpinner.setSelection(index)
                viewModel.selectedSubCategory = categoryModel
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

    private fun setProfileData(user: UserModel) {
        Glide.with(context!!).load(Constants.IMAGES_BASE_URL + user.avatar)
            .into(editProfileUserImage)
        userNameEt.setText(user.name)
        phoneEt.setText(user.phone)
        emailEt.setText(user.email)
        descEt.setText(user.about)
        locationTv.text = user.address
        viewModel.address = user.address
        viewModel.lang = user.lang
        viewModel.lat = user.lat
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
                        editProfileUserImage.setImageURI(uri)
                        viewModel.avatarUri = uri
                    } else {
                        activity?.toast(getString(R.string.errorSelectImage))
                    }
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


    @AfterPermissionGranted(com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            editProfile()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun editProfile() {

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

        if (viewModel.selectedCategory == null || viewModel.selectedCategory!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.choose_category))
            return
        }

        if (viewModel.selectedSubCategory == null || viewModel.selectedSubCategory!!.id == mainViewModel.fakeId) {
            activity?.toast(getString(R.string.choose_category))
            return
        }

        if (viewModel.selectedSubCategory == null) {
            activity?.toast(getString(R.string.select_sub_category))
            return
        }

        if (descEt.text.toString().isEmpty()) {
            descEt.setEmptyError(context!!)
            return
        }

        val request = EditProfileRequest(
            userNameEt.text.toString(),
            emailEt.text.toString(),
            phoneEt.text.toString(),
            viewModel.address!!,
            viewModel.lang!!,
            viewModel.lat!!,
            descEt.text.toString(),
            viewModel.selectedSubCategory!!.id.toString()
        )

        viewModel.editProfile(request, viewModel.avatarUri)
    }
}
