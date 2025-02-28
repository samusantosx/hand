package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModoDeTreino : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Define o layout da atividade
        setContentView(R.layout.activity_modo_de_treino)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa os botões
        val precisionTrainingButton = findViewById<Button>(R.id.precisionTrainingButton)
        val speedTrainingButton = findViewById<Button>(R.id.speedTrainingButton)
        val staminaTrainingButton = findViewById<Button>(R.id.staminaTrainingButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Navega para Treino de Precisão
        precisionTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDePrecisao::class.java)
            startActivity(intent)
        }

        // Navega para Treino de Velocidade
        speedTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDeVelocidade::class.java)
            startActivity(intent)
        }

        // Navega para Treino de Resistência
        staminaTrainingButton.setOnClickListener {
            val intent = Intent(this, TreinoDeResistencia::class.java)
            startActivity(intent)
        }

        // Botão para voltar ao menu principal
        backButton.setOnClickListener {
            finish()
        }
    }
}