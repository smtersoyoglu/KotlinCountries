package com.sametersoyoglu.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sametersoyoglu.kotlincountries.R
import com.sametersoyoglu.kotlincountries.databinding.FragmentCountryBinding
import com.sametersoyoglu.kotlincountries.util.downloadFromUrl
import com.sametersoyoglu.kotlincountries.util.placeholderProgressBar
import com.sametersoyoglu.kotlincountries.viewmodel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel : CountryViewModel

    private var countryUuid = 0
    private lateinit var dataBinding : FragmentCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProvider(this).get(CountryViewModel :: class.java)
        viewModel.getDataFromRoom(countryUuid)


        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            //country varmı yokmu (veri varmı yokmu) kontrol etme .let varsa içersindeki işlemleri yapma
            country?.let {

                dataBinding.selectedCountry = country

                /* viewbinding işlemlerini DataBinding ile yaptık
                binding.countryName.text = country.countryName
                binding.countryCapital.text = country.countryCapital
                binding.countryRegion.text = country.countryRegion
                binding.countryCurrency.text = country.countryCurrency
                binding.countryLanguage.text = country.countryLanguage
                context?.let {
                    binding.countryImage.downloadFromUrl(country.imageUrl, placeholderProgressBar(it))
                }
                 */

            }
        })

    }

}