package com.Super.hande

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#440031")

        Handler(Looper.getMainLooper()).postDelayed({
            val intent =  Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
