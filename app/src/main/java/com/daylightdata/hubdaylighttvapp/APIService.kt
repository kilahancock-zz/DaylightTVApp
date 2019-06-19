package com.daylightdata.hubdaylighttvapp

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("/tv-slides")
    fun getSlides(): Call<Array<Slide>>
}

