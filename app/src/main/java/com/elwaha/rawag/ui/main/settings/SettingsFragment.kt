package com.elwaha.rawag.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.*
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
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
            when(checkedId){
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
    }
}
