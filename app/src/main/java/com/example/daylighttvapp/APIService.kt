package com.example.daylighttvapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/tv-slides")
    fun getSlides(): Call<Array<Slide>>
    //@get:GET("/tv-slides")
    //val slides: Call<SlideList>
}

