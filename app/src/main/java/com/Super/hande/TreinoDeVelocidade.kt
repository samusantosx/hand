package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDeVelocidadeBinding

class TreinoDeVelocidade : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeVelocidadeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTreinoDeVelocidadeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebendo dados do Arduino
        /*
        val speedData = BluetoothManager.receiveData() ?: "0.0"
        binding.speedValue.text = "Velocidade da bola: $speedData m/s"

        // Configura o listener para o botão de voltar ao menu
        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()  // Fecha a tela atual para evitar sobreposição de atividades
        }

         */
    }
}