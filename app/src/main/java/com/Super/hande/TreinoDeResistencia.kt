package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDeResistenciaBinding

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebendo dados do Arduino
        /*
        val goalsData = BluetoothManager.receiveData() ?: "0"
        binding.goalsValue.text = "Gols por minuto: $goalsData"

        // Configura o listener para o botão de voltar ao menu
        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()  // Fecha a tela atual para evitar sobreposição de atividades
        }

         */
    }
}