package com.elwaha.rawag.ui.main.profile.myAds

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
import kotlinx.android.synthetic.main.my_ads_fragment.*

class MyAdsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    companion object {
        fun newInstance() = MyAdsFragment()
    }

    private lateinit var viewModel: MyAdsViewModel
    private var adapter = MyAdsAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_ads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyAdsViewModel::class.java)
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
        myAdsRv.adapter = adapter
        myAdsRv.setHasFixedSize(true)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when(view?.id){
            R.id.editImgv-> {
                activity?.toast("Edit")
            }

            R.id.cancelImgv-> {
                activity?.toast("Cancel")
            }
        }
    }
}
