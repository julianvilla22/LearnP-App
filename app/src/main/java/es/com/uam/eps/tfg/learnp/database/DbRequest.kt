package es.com.uam.eps.tfg.learnp.database

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import es.com.uam.eps.tfg.learnp.model.Word
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DbRequest {
    lateinit var retrofit : Retrofit;

    constructor(){
        initializeRetrofit();
    }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://learnp-bbb7a5d36c06.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

}