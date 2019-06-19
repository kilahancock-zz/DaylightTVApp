package com.daylightdata.hubdaylighttvapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var fetchDelayMillis:Long = 0;
    private val fetchPeriodMillis:Long = 1000 * 60;
    private val displayDelayMillis:Long = 0;
    private val displayPeriodMillis:Long = 1000 * 5;

    private var fetchTimer:Timer? = null
    private var displayTimer:Timer? = null

    private val baseURL = "https://hubdaylight.herokuapp.com"

    var slides = emptyArray<Slide>()
    private var currentSlideIndex = 0

    private lateinit var slidesAPIService:APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRetrofit()
    }


    override fun onResume() {
        super.onResume()

        this.startTimers()
    }

    override fun onPause() {
        super.onPause()

        this.fetchTimer?.cancel()
        this.fetchTimer = null

        this.displayTimer?.cancel()
        this.displayTimer = null
    }


    fun startTimers() {
        this.fetchTimer = timer("slidefetcher", false, fetchDelayMillis, fetchPeriodMillis) {
            Log.d("fetchtimer", "starting slide fetching")

            val call = this@MainActivity.slidesAPIService.getSlides()
            call.enqueue(object : Callback<Array<Slide>> {

                override fun onResponse(call: Call<Array<Slide>?>, response: Response<Array<Slide>?>) {
                    val receivedSlides = response.body() as Array<Slide>
                    Log.d("fetchtimer", "retrieved " + receivedSlides.size + " slides from API")

                    runOnUiThread {
                        Log.d("fetchtimer", "settings slides array")
                        this@MainActivity.slides = receivedSlides
                    }
                }

                override fun onFailure(call: Call<Array<Slide>?>, t: Throwable?) {
                    Log.e("fetchtimer", "error: " + t.toString())
                }
            })
        }

        this.displayTimer = timer("slidefetcher", false, displayDelayMillis, displayPeriodMillis) {
            Log.d("displaytimer", "setting new slide")
            runOnUiThread {
                if(this@MainActivity.slides.size > 0) {
                    val useableIndex = this@MainActivity.currentSlideIndex % this@MainActivity.slides.size
                    val currentSlide = this@MainActivity.slides[useableIndex]
                    if (currentSlide.image != null && currentSlide.image != "") {
                        Log.d("displaytimer", "processing image slide at index " + useableIndex)
                        Picasso.get().load(currentSlide.image).fit().centerInside().into(changingImage)
                        changingText.visibility = View.GONE
                        changingImage.visibility = View.VISIBLE
                    } else {
                        Log.d("displaytimer", "processing text slide at index " + useableIndex)
                        changingText.text = currentSlide.text
                        changingText.visibility = View.VISIBLE
                        changingImage.visibility = View.GONE
                    }
                    this@MainActivity.currentSlideIndex++
                }
            }
        }
    }

    fun setupRetrofit() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.slidesAPIService = retrofit.create(APIService::class.java)
    }
}

