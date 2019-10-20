package com.elwaha.rawag.ui.main.profile.editProfile.info

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
import com.chad.library.adapter.base.BaseQuickAdapter

import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.adapters.ImagesAdapter
import com.elwaha.rawag.ui.main.auth.register.RC_AVATAR
import com.elwaha.rawag.ui.main.auth.register.RC_IMAGES
import com.elwaha.rawag.utilies.getRealPathFromUri
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.edit_info_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class EditInfoFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = EditInfoFragment()
    }

    private lateinit var viewModel: EditInfoViewModel
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
        locationTv.isSelected = true
        chooseProfileImage.setOnClickListener { chooseAvatar() }
        pickImages.setOnClickListener { chooseBackgroundImages() }
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
                        editProfileUserImage.setImageURI(uri)
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


    @AfterPermissionGranted(com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            editProdile()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun editProdile() {

    }
}
