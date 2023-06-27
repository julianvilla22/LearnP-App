package es.com.uam.eps.tfg.learnp.database

import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.http.GET

interface WordApi {
    @GET("/word")
    public fun getAllWords() : Call<List<Word>>

}