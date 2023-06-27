package es.com.uam.eps.tfg.learnp.database

import es.com.uam.eps.tfg.learnp.model.Rule
import es.com.uam.eps.tfg.learnp.model.Word
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RuleApi {
    @GET("/word/{id}/rule")
    public fun getWordRules(@Path("id") id : Long) : Call<List<Rule>>
}