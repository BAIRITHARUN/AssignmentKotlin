package com.infy.assignmentkotlin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun getAPI(@Url url: String): Call<ResponseBody>

}