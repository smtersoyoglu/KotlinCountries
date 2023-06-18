package com.sametersoyoglu.kotlincountries.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sametersoyoglu.kotlincountries.R

//Extension fonksiyonu oluşturacaksak hangi sınıfa eklenti yapıcaksak ona yazabiliyoruz  --- herhangi bir objeden herhangi bir extension oluşturabiliriz
// String sınıfına eklenti yazdık aşşağıda
/*
fun String.myExtension(myParameter: String) {
    println(myParameter)
}
 */

// görseli internetten alıp getirme.
fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable) {
    // hata olursa ne olucak gibi işlemler için aşağıdaki kodu kullanırız--
    // görseller inene kadar ne göstereceğimizi yapmak için placeholder kullanırız. cünkü textler cok hızlı inerken görseller o kadar hızlı inmiyeceği için inene kadar bir varsayılan görsel göstermek için yaptık.
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

fun placeholderProgressBar(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

// xml de çalıştırmamıza olanak sağlıyor @BindingAdapter
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}

