package com.infy.assignmentkotlin.network

import com.infy.assignmentkotlin.common.IAppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object ApiClient {

    private val API_BASE_URL = IAppConstants.domain


    private val builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    val client = builder.build()

}