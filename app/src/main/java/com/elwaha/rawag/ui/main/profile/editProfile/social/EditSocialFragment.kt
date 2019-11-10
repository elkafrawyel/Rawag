package com.elwaha.rawag.ui.main.profile.editProfile.social

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.EditSocialRequest
import com.elwaha.rawag.utilies.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.edit_social_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*

class EditSocialFragment : Fragment() {

    companion object {
        fun newInstance() =
            EditSocialFragment()
    }

    private lateinit var viewModel: EditSocialViewModel
    private var loading: SpotsDialog? = null
    private var user: UserModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_social_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditSocialViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onEditResponse(it) })
        newAccountMbtn.setOnClickListener {
            editSocial()
        }
        val userString = Injector.getPreferenceHelper().user
        user = ObjectConverter().getUser(userString!!)
        setProfileData(user!!)

    }

    private fun setProfileData(user: UserModel) {
        facebookEt.setText(user.facebook)
        twitterEt.setText(user.twitter)
        instgramEt.setText(user.instagram)
        snapChatEt.setText(user.snabchat)
        youtubeEt.setText(user.youtube)
    }

    private fun onEditResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                loading = activity?.showLoading(getString(R.string.wait))
                loading!!.show()
            }
            ViewState.Success -> {
                if (loading != null) {
                    loading!!.dismiss()
                }
                activity?.toast(getString(R.string.changes_saved))
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

    private fun editSocial() {
        val request = EditSocialRequest()

        if (facebookEt.text.toString().isNotEmpty()) {
            request.facebook = facebookEt.text.toString()
        }

        if (twitterEt.text.toString().isNotEmpty()) {
            request.twitter = twitterEt.text.toString()
        }

        if (snapChatEt.text.toString().isNotEmpty()) {
            request.snabchat = snapChatEt.text.toString()
        }

        if (instgramEt.text.toString().isNotEmpty()) {
            request.instagram = instgramEt.text.toString()
        }

        if (youtubeEt.text.toString().isNotEmpty()) {
            request.youtube = youtubeEt.text.toString()
        }

//        if (request.facebook != "" ||
//            request.twitter != ""  ||
//            request.instagram != ""||
//            request.snabchat != "" ||
//            request.instagram != "") {
            viewModel.edit(request)
//        }
    }

}
