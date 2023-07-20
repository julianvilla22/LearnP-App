package es.com.uam.eps.tfg.learnp.database

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BACKEND_URL = "https://learnp-bbb7a5d36c06.herokuapp.com"
class DbRequest() {
    lateinit var retrofit : Retrofit;

    init {
        initializeRetrofit()
    }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

}