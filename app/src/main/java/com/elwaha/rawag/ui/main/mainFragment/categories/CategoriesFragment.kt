package com.elwaha.rawag.ui.main.mainFragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elkafrawyel.CustomViews
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.adapters.CategoriesAdapter
import com.elwaha.rawag.ui.main.mainFragment.ImageSliderAdapter
import com.elwaha.rawag.ui.main.mainFragment.MainFragmentDirections
import com.elwaha.rawag.utilies.ViewState
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.categories_fragment.*
import java.util.*
import kotlin.concurrent.timerTask

class CategoriesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel
    private var adapter = CategoriesAdapter().also {
        it.onItemChildClickListener = this
    }

    private var timer: Timer? = null
    private val imageSliderAdapter = ImageSliderAdapter { position ->
        val images = viewModel.ads
        if (images.isNotEmpty()) {
            val action = MainFragmentDirections.actionMainFragmentToProfileFragment(
                viewModel.ads[position].userId!!.toString(),
                false
            )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
        viewModel.uiState.observe(this, androidx.lifecycle.Observer { onHomeResponse(it) })
        categoriesRv.adapter = adapter
        categoriesRv.setHasFixedSize(true)
        rootView.setLayout(categoriesNsv)
        rootView.setVisible(CustomViews.LAYOUT)
        if (viewModel.categories.isEmpty() || viewModel.ads.isEmpty()) {
            viewModel.get()
        }
    }

    private fun onHomeResponse(state: ViewState?) {
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
                    viewModel.get()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
            }
            ViewState.LastPage -> {

            }
            null -> {

            }
        }
    }

    private fun setData() {
        imageSliderAdapter.submitList(viewModel.ads)
        bannerSliderVp.setPadding(80, 0, 80, 0);
        bannerSliderVp.pageMargin = 20
        bannerSliderVp.clipToPadding = false
        bannerSliderVp.adapter = imageSliderAdapter

        adapter.replaceData(viewModel.categories)
    }


    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < imageSliderAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 5000)
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cardItem -> {
                val categoryId = viewModel.categories[position].id
                val action =
                    MainFragmentDirections.actionMainFragmentToSubCategoriesFragment(categoryId.toString())
                findNavController().navigate(action)
            }
        }
    }
}
