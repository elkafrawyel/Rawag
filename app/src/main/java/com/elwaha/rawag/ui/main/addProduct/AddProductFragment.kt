package com.elwaha.rawag.ui.main.addProduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.ui.main.auth.register.RC_AVATAR
import com.elwaha.rawag.ui.main.auth.register.RC_IMAGES
import com.elwaha.rawag.utilies.getRealPathFromUri
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.add_product_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class AddProductFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
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

        backImgv.setOnClickListener { findNavController().navigateUp() }
        pickImages.setOnClickListener { chooseBackgroundImages() }

        val adapterCategories = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.days
        )

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daysSpinner.adapter = adapterCategories
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
//                    viewModel.selectedSubCategory = viewModel.subCategories[position]
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


    @AfterPermissionGranted(com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            addProduct()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun addProduct() {

    }
}
