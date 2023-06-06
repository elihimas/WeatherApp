package com.elihimas.weatherapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.elihimas.weatherapp.databinding.ActivitySplashBinding

class SplashActivity : ComponentActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        waitAndMoveTOMain()
    }

    private fun waitAndMoveTOMain() {
        Handler(Looper.getMainLooper())
            .postDelayed(::moveToMain, splashTime)
    }

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        const val splashTime = 3000L
    }
}
