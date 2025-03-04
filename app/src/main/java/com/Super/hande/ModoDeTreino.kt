package com.Super.hande

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityModoDeTreinoBinding

class ModoDeTreino : AppCompatActivity() {

    private lateinit var binding: ActivityModoDeTreinoBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Define o layout da atividade
        binding = ActivityModoDeTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa os botões
        // Navega para Treino de Precisão
        binding.precisionTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDePrecisao::class.java)
            startActivity(intent)
        }

        // Navega para Treino de Velocidade
        binding.speedTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDeVelocidade::class.java)
            startActivity(intent)
        }

        // Navega para Treino de Resistência
        binding.staminaTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDeResistencia::class.java)
            startActivity(intent)
        }

        // Botão para voltar ao menu principal
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}