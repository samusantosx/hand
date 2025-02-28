package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnDashboard = findViewById<Button>(R.id.btnDashboard)

        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnDashboard.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
        }
*/
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =  Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
