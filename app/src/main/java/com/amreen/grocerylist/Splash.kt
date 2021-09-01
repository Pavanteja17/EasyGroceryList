package com.amreen.grocerylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        val handler=Handler()
        handler.postDelayed({
            val intent= Intent(this@Splash,MainActivity::class.java)
            startActivity(intent) },1000)

    }
}