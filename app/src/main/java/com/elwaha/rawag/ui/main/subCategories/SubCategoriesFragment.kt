package com.elwaha.rawag.ui.main.subCategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.toast
import com.elwaha.rawag.ui.main.adapters.CategoriesAdapter
import com.elwaha.rawag.utilies.CustomParent
import com.elwaha.rawag.utilies.CustomViews
import kotlinx.android.synthetic.main.sub_categories_fragment.*

class SubCategoriesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = SubCategoriesFragment()
    }

    private lateinit var viewModel: SubCategoriesViewModel
    private var adapter = CategoriesAdapter().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubCategoriesViewModel::class.java)
        arguments?.let {
            val categoryId = com.elwaha.rawag.ui.main.subCategories.SubCategoriesFragmentArgs.fromBundle(
                it
            ).categoryId
            activity?.toast(categoryId)
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        subCategoriesRv.adapter = adapter
        subCategoriesRv.setHasFixedSize(true)

        setData()
        rootView.setLayout(subCategoriesCl)
        rootView.setVisible(CustomViews.LAYOUT)
    }



    fun setData(){
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")

        adapter.notifyDataSetChanged()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cardItem -> {
                val action =SubCategoriesFragmentDirections.actionSubCategoriesFragmentToProductsFragment(
                        "test"
                    )
                findNavController().navigate(action)
            }
        }
    }
}
