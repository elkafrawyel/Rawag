package com.elwaha.rawag.ui.main.choosePaymentMethod

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.requests.AddAdRequest
import com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.choose_payment_method_fragment.*
import paytabs.project.PayTabActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ChoosePaymentMethodFragment : Fragment() {

    companion object {
        fun newInstance() =
            ChoosePaymentMethodFragment()
    }

    private lateinit var viewModel: ChoosePaymentMethodViewModel
    private var loading: SpotsDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_payment_method_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChoosePaymentMethodViewModel::class.java)
        viewModel.uiStateEvent.observeEvent(this) { onAdUploadResponse(it) }
        backImgv.setOnClickListener { findNavController().navigateUp() }
        arguments?.let {
            val subCategoryId = ChoosePaymentMethodFragmentArgs.fromBundle(it).subCategoryId
            viewModel.subCategoryId = subCategoryId
            val cityId = ChoosePaymentMethodFragmentArgs.fromBundle(it).cityId
            viewModel.cityId = cityId
            val price = ChoosePaymentMethodFragmentArgs.fromBundle(it).price
            viewModel.price = price
            val baqaId = ChoosePaymentMethodFragmentArgs.fromBundle(it).baqaId
            viewModel.baqaId = baqaId
            val days = ChoosePaymentMethodFragmentArgs.fromBundle(it).days
            viewModel.days = days
            val images = ChoosePaymentMethodFragmentArgs.fromBundle(it).images
            viewModel.images = images

        }


        sendMbtn.setOnClickListener {
            uploadAd()
        }

        visa.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.paymentType = "visa"
        }

        masterCard.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.paymentType = "mastercard"
        }

        sdad.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.paymentType = "sdad"
        }

        mada.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.paymentType = "mada"
        }
    }

    private fun onAdUploadResponse(state: ViewState) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.uploadingAd))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.uploadAdSuccess))
                activity?.restartApplication()
                activity?.finish()
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

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            startUploading()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                com.elwaha.rawag.ui.main.auth.register.RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun startUploading() {
        viewModel.uploadAd(
            AddAdRequest(
                viewModel.subCategoryId,
                viewModel.cityId,
                viewModel.price,
                viewModel.baqaId,
                viewModel.paymentType!!,
                viewModel.days
            ),
            viewModel.images!!
        )
    }

    private fun uploadAd() {


        if (viewModel.paymentType != null) {
            checkStorage()

//            val userString = Injector.getPreferenceHelper().user
//            val user = ObjectConverter().getUser(userString!!)
//            openPaymentTab(
//                user.email,
//                user.phone,
//                "1",
//                "ar"
//            )
        } else {
            activity?.toast(getString(R.string.selectPaymentMethod))
        }
    }

    private fun openPaymentTab(
        customerEmail: String,
        customerPhone: String,
        amount: String,
        language: String
    ) {
        val intent = Intent(activity, PayTabActivity::class.java)
        intent.putExtra("pt_merchant_email", "khalidalmeezan1@hotmail.com")
        intent.putExtra(
            "pt_secret_key",
            "Wn18IOERwfqh0gKDJqUxdl0H8Wi2WtNSRP1AEFraVRsW8cARDupQPpyYlQ0hkOabDbLhRdsM5UId7j5KNuADT8BeZBwjqGu3RglJ"
        )
        intent.putExtra("pt_amount", amount)
        intent.putExtra("pt_currency_code", "SAR")
        intent.putExtra("pt_transaction_title", "Test Paytabs android library")
        intent.putExtra("pt_customer_phone_number", customerPhone)
        intent.putExtra("pt_country_billing", "SAU")
        intent.putExtra("pt_address_billing", "Flat 1,Building 123, Road 2345")
        intent.putExtra("pt_city_billing", "Manama")
        intent.putExtra("language", language)
        intent.putExtra("pt_state_billing", "Manama")
        intent.putExtra("pt_postal_code_billing", "11564")
        intent.putExtra("pt_customer_email", customerEmail)
        intent.putExtra("pt_order_id", "1234567")
        intent.putExtra("pt_product_name", "Samsung Galaxy S6")
        startActivityForResult(intent, 201)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 201) {
            if (data?.getStringExtra("pt_response_code") == "100") {
                checkStorage()
                Log.e("PAY", data.getStringExtra("pt_transaction_id"))
                Log.e("PAY", data.getStringExtra("pt_result"))
            }
        }
    }

}
