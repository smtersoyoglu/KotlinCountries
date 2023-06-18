package com.sametersoyoglu.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sametersoyoglu.kotlincountries.model.Country

@Dao
interface CountryDao {
    // Coroutines -> Bizim farklı threadlerde işlem yapmamıza olanak tanıyan ve bu işi çok kolaylaştıran bir kotlin yapısı
    // Data Access Object   -  burda veritabanımıza ulaşmak istediğimiz metodları yazarız.

    @Insert
    suspend fun insertAll(vararg countries: Country) : List<Long>

    // Insert -> INSERT INTO - Database(veritabanına) erişim için metodu,fonksiyonu
    // suspend -> coroutine ler içerisinde çağrılıyor ve fonksiyonları durdurup devam etmeye olanak sağlıyan kod ( pause & resume )
    //vararg -> multiple country objects döndürcek
    // List<Long> -> primary keys döndürüyor


    @Query ("SELECT * FROM country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM country WHERE uuid = :countryId")
    suspend fun getCountry(countryId : Int): Country

    @Query ("DELETE FROM country")
    suspend fun deleteAllCountries()
}