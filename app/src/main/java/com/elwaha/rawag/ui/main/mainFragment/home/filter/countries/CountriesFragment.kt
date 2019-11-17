package com.elwaha.rawag.ui.main.mainFragment.home.filter.countries

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
import com.elwaha.rawag.utilies.ViewState
import kotlinx.android.synthetic.main.countries_fragment.*

class CountriesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CountriesFragment()
    }

    private lateinit var viewModel: CountriesViewModel
    private var adapter = AdapterCountries().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.countries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onResponse(it) })
        rootView.setLayout(countriesRv)
        backImgv.setOnClickListener { findNavController().navigateUp() }

        countriesRv.setHasFixedSize(true)
        countriesRv.adapter = adapter
    }

    private fun onResponse(state: ViewState?) {
        when (state) {
            ViewState.Loading -> {
                rootView.setVisible(CustomViews.LOADING)
            }
            ViewState.Success -> {
                rootView.setVisible(CustomViews.LAYOUT)
                setData()
            }
            is ViewState.Error -> {
                rootView.setVisible(CustomViews.ERROR)
            }
            ViewState.NoConnection -> {
                rootView.setVisible(CustomViews.INTERNET)
                rootView.retry {
                    viewModel.getCountries()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
            }
        }
    }

    private fun setData() {
        adapter.replaceData(viewModel.countriesList)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.countryItem -> {
                val action =
                    CountriesFragmentDirections.actionCountriesFragmentToCitiesFragment(
                        viewModel.countriesList[position].id.toString()
                    )
                findNavController().navigate(action)
            }
        }
    }
}
