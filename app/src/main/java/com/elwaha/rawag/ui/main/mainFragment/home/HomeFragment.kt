package com.elwaha.rawag.ui.main.mainFragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.adapters.CategoriesAdapter
import com.elwaha.rawag.ui.main.mainFragment.ImageSliderAdapter
import com.elwaha.rawag.ui.main.mainFragment.MainFragmentDirections
import com.elwaha.rawag.utilies.CustomViews
import com.elwaha.rawag.utilies.toast
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.*
import kotlin.concurrent.timerTask

class HomeFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var adapter = CategoriesAdapter().also {
        it.onItemChildClickListener = this
    }

    private lateinit var viewModel: HomeViewModel
    private var timer: Timer? = null
    private val imageSliderAdapter = ImageSliderAdapter {
        val images = viewModel.images
        if (images.isNotEmpty()) {
            activity?.toast("clicked")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        imageSliderAdapter.submitList(viewModel.images)
        bannerSliderVp.setPadding(80, 0, 80, 0);
        bannerSliderVp.pageMargin = 20
        bannerSliderVp.clipToPadding = false
        bannerSliderVp.adapter = imageSliderAdapter

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
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")
        adapter.data.add("A")

        categoriesRv.adapter = adapter
        categoriesRv.setHasFixedSize(true)

        rootView.setLayout(homeNsv)
        rootView.setVisible(CustomViews.LAYOUT)
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
                val action =
                    MainFragmentDirections.actionMainFragmentToSubCategoriesFragment("test")
                findNavController().navigate(action)
            }
        }
    }
}
