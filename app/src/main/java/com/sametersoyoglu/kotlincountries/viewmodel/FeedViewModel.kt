package com.sametersoyoglu.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sametersoyoglu.kotlincountries.model.Country
import com.sametersoyoglu.kotlincountries.service.CountryAPIService
import com.sametersoyoglu.kotlincountries.service.CountryDatabase
import com.sametersoyoglu.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    // 10dk da bir olması için 10dk yı yukardaki gibi tanımlıyoruz.

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()


    fun refreshData() {

        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getDataFromSQLite()
        }else {
            getDataFromAPI()
        }
        getDataFromAPI()
    }
    // Sayfayı yenilediğinde API'dan internetten verileri alma
    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value = true

        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite(Veriler SQLite dan alındı)",Toast.LENGTH_LONG).show()
        }
    }

    //internetten veriyi alma
    private fun getDataFromAPI() {
        countryLoading.value = true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSqlite(t)
                        Toast.makeText(getApplication(),"Countries From API(Veriler API dan,internetten alındı)",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun showCountries(countryList : List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }

    private fun storeInSqlite(list : List<Country>) {

        launch {
            // database ile ilgili işlemleri yapıyoruz -- toTypedArray() listedeki verileri tek tek tekil hale getirir.
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray()) // -> list -> individual(tek tek) hale getiriyor.

            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i = i + 1
            }
            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}