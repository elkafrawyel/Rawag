package com.elwaha.rawag.ui.main.subCategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.ui.main.adapters.CategoriesAdapter
import com.elwaha.rawag.utilies.ViewState
import kotlinx.android.synthetic.main.sub_categories_fragment.*

class SubCategoriesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

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
        rootView.setLayout(subCategoriesRv)

        viewModel = ViewModelProviders.of(this).get(SubCategoriesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onResponse(it) })

        arguments?.let {
            val categoryId = SubCategoriesFragmentArgs.fromBundle(it).categoryId
            if (viewModel.categoryId == null) {
                viewModel.categoryId = categoryId
                viewModel.getSubCategories()
            }
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        subCategoriesRv.adapter = adapter
        subCategoriesRv.setHasFixedSize(true)

    }

    private fun onResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                rootView.setVisible(CustomViews.LOADING)
            }
            ViewState.Success -> {
                setData()
                rootView.setVisible(CustomViews.LAYOUT)
            }
            is ViewState.Error -> {
                rootView.setVisible(CustomViews.ERROR)
            }
            ViewState.NoConnection -> {
                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    viewModel.refresh()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
                rootView.setEmptyText(R.string.empty_category)
            }
            ViewState.LastPage -> {

            }
            null -> {

            }
        }
    }

    private fun setData() {
        adapter.replaceData(viewModel.subCategoriesList)
        adapter.notifyDataSetChanged()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cardItem -> {
                val action =
                    SubCategoriesFragmentDirections.actionSubCategoriesFragmentToProductsFragment(
                        viewModel.subCategoriesList[position].id.toString()
                    )
                findNavController().navigate(action)
            }
        }
    }
}
