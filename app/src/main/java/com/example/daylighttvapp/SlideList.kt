package com.example.daylighttvapp

import com.google.gson.annotations.SerializedName

class SlideList {
    @SerializedName("slides")
    var slides = arrayOf<Slide>()

    fun grabSlides(): Array<Slide> {
        return slides;
    }
}

