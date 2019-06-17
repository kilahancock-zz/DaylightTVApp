package com.example.daylighttvapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val url = "https://hubdaylight.herokuapp.com"
    var slides = emptyArray<Slide>()
    var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        getSlides()
    }

    private fun getSlides() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(APIService::class.java)
        val call = api.getSlides()

        call.enqueue(object : Callback<Array<Slide>> {

            override fun onResponse(call: Call<Array<Slide>?>, response: Response<Array<Slide>?>) {
                slides = response.body() as Array<Slide>
                num = slides.size
                this@MainActivity.runOnUiThread(Runnable {
                    //Picasso.get().load("https://www.tazama.io/img/tazama-logo-1024.png").fit().into(changingImage)
                    Log.i("print", slides[1].image)
                    Picasso.get().load(slides[1].image).fit().into(changingImage)
                    //displaySlides()
                })
            }
            override fun onFailure(call: Call<Array<Slide>?>, t: Throwable?) {
                Log.v("Error", t.toString())
            }
        })
    }

    private fun displaySlides() {
        var k = 0
        while (true) {
            Log.i("print", slides[k].text)
            Log.i("print", slides[k].image)
            if (slides[k].image != "") {
                //changingText.visibility = View.GONE
                Log.i("image check", "just checked if image")
                Picasso.get().load("https://www.tazama.io/img/tazama-logo-1024.png").fit().into(changingImage)
                //Picasso.get().load(slides[k].image).fit().into(changingImage)
                //changingImage.adjustViewBounds = true
                //changingImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                //changingImage.visibility = View.VISIBLE
            } else {
                //changingImage.visibility = View.GONE
                changingText.text = slides[k].text
                //changingText.visibility = View.VISIBLE
            }
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
            }
            if (k == num - 1) {
                k = 0
            } else {
                k++
            }
        }
    }
}

