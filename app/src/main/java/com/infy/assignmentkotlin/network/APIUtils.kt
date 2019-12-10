package com.infy.assignmentkotlin.network

import okhttp3.ResponseBody
import retrofit2.Call

object APIUtils {

    fun getAPI(strUrl: String): Call<ResponseBody> {
        val apiService = ApiClient.client.create(ApiInterface::class.java)
        return apiService.getAPI(strUrl)
    }

}