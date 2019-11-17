package com.elwaha.rawag.ui.main.mainFragment.home.filter.cities

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
import kotlinx.android.synthetic.main.cities_fragment.*

class CitiesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CitiesFragment()
    }

    private lateinit var viewModel: CitiesViewModel
    private var adapter = AdapterCities().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cities_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CitiesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onCitiesResponse(it) })
        arguments?.let {
            val countryId = CitiesFragmentArgs.fromBundle(it).countryId
            viewModel.countryId = countryId
            if (viewModel.citiesList.isEmpty())
                viewModel.getCities()
        }

        rootView.setLayout(citiesRv)
        backImgv.setOnClickListener { findNavController().navigateUp() }
        showResults.setOnClickListener {
            val action =
                CitiesFragmentDirections.actionCitiesFragmentToProfilesFragment(
                null, viewModel.cityId,null
            )
            findNavController().navigate(action)
        }

        citiesRv.setHasFixedSize(true)
        citiesRv.adapter = adapter
    }

    private fun onCitiesResponse(state: ViewState?) {
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
                    viewModel.getCities()
                }
            }
            ViewState.Empty -> {
                rootView.setVisible(CustomViews.EMPTY)
            }
        }
    }

    private fun setData() {
        adapter.replaceData(viewModel.citiesList)
    }

    override fun onItemChildClick(a: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.cityRb -> {
                adapter.lastSelectCity = position
                adapter.notifyDataSetChanged()
                val cityId = viewModel.citiesList[position].id.toString()
                viewModel.cityId = cityId
            }
        }
    }
}
