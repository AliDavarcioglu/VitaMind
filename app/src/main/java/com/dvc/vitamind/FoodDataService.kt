package com.dvc.vitamind

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodDataService {
    @GET("search")
    fun searchFood(
        @Query("api_key") apiKey: String,
        @Query("query") foodName: String
    ): Call<FoodResponse>
}