package com.sametersoyoglu.kotlincountries.adapter

import android.database.DatabaseUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.sametersoyoglu.kotlincountries.R
import com.sametersoyoglu.kotlincountries.databinding.ItemCountryBinding
import com.sametersoyoglu.kotlincountries.model.Country
import com.sametersoyoglu.kotlincountries.util.downloadFromUrl
import com.sametersoyoglu.kotlincountries.util.placeholderProgressBar
import com.sametersoyoglu.kotlincountries.view.FeedFragmentDirections

class CountryAdapter (val countryList: ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root), CountryClickListener {

        override fun onCountryClicked(v: View) {
            var a = binding.countryUuidText.text.toString().toInt()
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(a)
            Navigation.findNavController(v).navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(binding)


    }


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.binding.country = countryList[position]
        holder.binding.listener = holder

        /* DataBinding ile bu işlemleri daha kolay yaptık
        // view'ı item_country.xml altında linearLayout a id vererek yaptın onu değiştir.
        holder.binding.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadFromUrl(countryList[position].imageUrl, placeholderProgressBar(holder.itemView.context))

         */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()

    }


}