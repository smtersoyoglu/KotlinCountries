package com.sametersoyoglu.kotlincountries.service

import com.sametersoyoglu.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI {

    //GET, POST verileri çekerken GET, verileri değiştirirken POST kullanırız. sadece veri cekeceğimiz için GET kullanıcaz.

    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    //BASE URL -> https://raw.githubusercontent.com/
    //EXT -> atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json


    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries() : Single<List<Country>>


}