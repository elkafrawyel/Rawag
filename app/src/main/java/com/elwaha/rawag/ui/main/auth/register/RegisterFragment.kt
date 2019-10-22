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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.utilies.getRealPathFromUri
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.register_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val RC_PERMISSION_STORAGE = 111
const val RC_AVATAR = 112
const val RC_IMAGES = 113

class RegisterFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
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
        locationTv.isSelected = true

        backImgv.setOnClickListener { findNavController().navigateUp() }
        termsTv.setOnClickListener { findNavController().navigate(R.id.termsFragment) }
        login.setOnClickListener { findNavController().navigate(R.id.loginFragment) }
        chooseProfileImage.setOnClickListener { chooseAvatar() }
        pickImages.setOnClickListener { chooseBackgroundImages() }
        setUpSpinner()
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
                    val path = activity?.getRealPathFromUri(uri!!)
                    if (path != null) {
                        registerUserImage.setImageURI(uri)
//                        viewModel.userImagePath = path
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

    }

    private fun setUpSpinner() {

        val adapterCategories = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.categories
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
//                    viewModel.selectedSubCategory = viewModel.subCategories[position]
                }
            }

        val adapterSubCategories = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.categories
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
//                    viewModel.selectedSubCategory = viewModel.subCategories[position]
                }
            }
    }

}
