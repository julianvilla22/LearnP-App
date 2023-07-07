package es.com.uam.eps.tfg.learnp.database

import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WordApi {
    @GET("/word")
    public fun getAllWords() : Call<List<Word>>
    
    @GET("/word/{id}/siblings")
    public fun getWordSiblings(@Path("id") id : Long) : Call<List<Word>>
    @GET("/word/examples/{ruleid}")
    public fun getRuleExamples(@Path("ruleid") id : Long) : Call<List<Word>>

    @GET("/word/{id}/homophones")
    public fun getWordHomophones(@Path("id") id : Long) : Call<List<Word>>

    @GET("/word/exceptions/{ruleid}")
    public fun getRuleExceptions(@Path("ruleid") ruleid: Long): Call<List<Word>>

}