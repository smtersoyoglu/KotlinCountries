package com.sametersoyoglu.kotlincountries.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sametersoyoglu.kotlincountries.model.Country


@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {
    // Room veritabanı

    abstract fun countryDao() : CountryDao

    // Singleton -> içerisinden tek bir obje oluşturulabilen bir sınıftır.

    companion object {
        // @Volatile -> diğer threadlerede farklı threadlerede görünür hale getirmeye yarar
        @Volatile private var instance : CountryDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                //also ile bunu yap üstüne birde şunu yap demektir. burda bu database'i oluştur ondan sonra instance'ı CountryDatabase'e eşitle diyoruz.
                instance = it
            }
        }


        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,CountryDatabase::class.java,"countrydatabase").build()

    }

}