package com.amreen.grocerylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.google.android.gms.ads.*

class LastActivity : AppCompatActivity() {
    lateinit var button:Button
    lateinit var mAdView:AdView
    var mInterstitialAd: InterstitialAd?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)
        val handler= Handler()
        handler.postDelayed({
            mAdView=findViewById(R.id.adView)
            MobileAds.initialize(this) {}

            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)

        },500)
        prepareAd()
        button=findViewById(R.id.back)
        button.setOnClickListener {
            onBackPressed()

        }
    }

    override fun onBackPressed() {
        if(mInterstitialAd?.isLoaded==true){
            mInterstitialAd?.show()

            mInterstitialAd?.setAdListener(object: AdListener(){
                override fun onAdClosed() {
                    super.onAdClosed()
                    val intent=Intent(this@LastActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            })

        }
        else{
            val intent=Intent(this@LastActivity,MainActivity::class.java)
            startActivity(intent)

        }

    }
    fun prepareAd(){
        mInterstitialAd = InterstitialAd(application)
        mInterstitialAd?.adUnitId = "ca-app-pub-6873376364307728/1616520841"
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    override fun onPause() {
        mAdView.destroy()
        mInterstitialAd=null
        super.onPause()
    }
}