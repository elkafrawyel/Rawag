package com.elwaha.rawag.ui.main.profile.editProfile.social

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.elwaha.rawag.R

class EditSocialFragment : Fragment() {

    companion object {
        fun newInstance() =
            EditSocialFragment()
    }

    private lateinit var viewModel: EditSocialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_social_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditSocialViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
