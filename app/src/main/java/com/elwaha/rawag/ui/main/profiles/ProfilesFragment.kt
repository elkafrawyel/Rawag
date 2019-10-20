package com.elwaha.rawag.ui.main.profiles

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.profiles_fragment.*

class ProfilesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = ProfilesFragment()
    }

    private lateinit var viewModel: ProfilesViewModel
    private var adapter = ProfilesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profiles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfilesViewModel::class.java)
        arguments?.let {
            val subCategoryName = com.elwaha.rawag.ui.main.profiles.ProfilesFragmentArgs.fromBundle(it)
                .subCategoryId
            activity?.toast(subCategoryName)
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        productsRv.adapter = adapter
        productsRv.setHasFixedSize(true)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when(view?.id){
            R.id.profileItem -> {
                val action =
                    com.elwaha.rawag.ui.main.profiles.ProfilesFragmentDirections.actionProfilesFragmentToProfileFragment(
                        "test",
                        false
                    )
                findNavController().navigate(action)
            }
        }
    }
}
