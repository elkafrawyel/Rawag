package com.elwaha.rawag.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private var loading: SpotsDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onResponse(it) })
        backImgv.setOnClickListener { findNavController().navigateUp() }

        when (Injector.getPreferenceHelper().language) {
            "ar" -> {
                arRadio.isChecked = true
            }

            "en" -> {
                enRadio.isChecked = true
            }
        }

        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.arRadio -> {
                    activity?.saveLanguage(Constants.Language.ARABIC)
                }

                R.id.enRadio -> {
                    activity?.saveLanguage(Constants.Language.ENGLISH)
                }
            }
            activity?.changeLanguage()
            activity?.finish()
            activity?.restartApplication()
        }

        if (Injector.getPreferenceHelper().isLoggedIn) {
            val userString = Injector.getPreferenceHelper().user
            val user = ObjectConverter().getUser(userString!!)

            notificationSwitch.isEnabled = true
            notificationSwitch.isChecked = user.notify_status == 1
        }else{
            notificationSwitch.isEnabled = false
        }

        notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.setNotificationState()
            } else {
                viewModel.setNotificationState()
            }
        }
    }

    private fun onResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.wait))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.saved))

                notificationSwitch.isChecked = viewModel.status

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
}
