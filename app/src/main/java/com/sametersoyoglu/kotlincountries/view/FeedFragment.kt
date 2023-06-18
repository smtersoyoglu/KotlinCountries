package com.sametersoyoglu.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sametersoyoglu.kotlincountries.R
import com.sametersoyoglu.kotlincountries.adapter.CountryAdapter
import com.sametersoyoglu.kotlincountries.databinding.FragmentFeedBinding
import com.sametersoyoglu.kotlincountries.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFeedBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(FeedViewModel :: class.java)
        viewModel.refreshData()

        // recyclerview de alt altta gelmeleri için linearlayout kullanırız.
        binding.countryList.layoutManager = LinearLayoutManager(context)

        binding.countryList.adapter = countryAdapter


        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.countryList.visibility = View.GONE
            binding.countryError.visibility = View.GONE
            binding.countryLoading.visibility = View.VISIBLE

            viewModel.refreshFromAPI()

            binding.swipeRefreshLayout.isRefreshing = false
        }


        observeLiveData()

    }

    // Livedataları burda observe lücez ( gözlemlicez)
    private fun observeLiveData() {

        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->

            countries?.let {
                // countryList (recyclerView) imiz görünebilir hale getirdik.
                binding.countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->

            error?.let {
                if (it) {
                    binding.countryError.visibility = View.VISIBLE
                }else {
                    binding.countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->

            loading?.let {
                if (it) {
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryList.visibility = View.GONE
                    binding.countryError.visibility = View.GONE
                }else {
                    binding.countryLoading.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}