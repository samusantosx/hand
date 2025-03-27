package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityTreinoDeResistenciaBinding
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding
    private var startTime: Long = 0
    private var tempoTotalTreino: Int = 0
    private var intensidadeTreino: Int = (5..10).random()
    private var golsMarcados = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia o cronômetro
        startTime = SystemClock.elapsedRealtime()

        // Exibe gols por minuto
        binding.goalsValue.text = "Gols por minuto: ${calcularGolsPorMinuto()}"

        // Botão de voltar apenas fecha a tela
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        // Botão de finalizar treino → Redireciona para Avaliação de Desgaste
        binding.btnFinalizarTreino.setOnClickListener {
            finalizarTreino()
        }
    }

    private fun finalizarTreino() {
        tempoTotalTreino = ((SystemClock.elapsedRealtime() - startTime) / 1000 / 60).toInt()
        val desgaste = calcularDesgaste(tempoTotalTreino, intensidadeTreino)

        // Salvar no Firebase e redirecionar para Avaliação de Desgaste
        salvarNoFirebase(tempoTotalTreino, golsMarcados, desgaste)
    }

    private fun calcularGolsPorMinuto(): Double {
        return if (tempoTotalTreino > 0) golsMarcados.toDouble() / tempoTotalTreino else 0.0
    }

    private fun calcularDesgaste(tempoTreino: Int, intensidade: Int): Double {
        return max(0.0, (tempoTreino * intensidade * 0.5))
    }

    private fun salvarNoFirebase(tempoTreino: Int, gols: Int, desgaste: Double) {
        val db = FirebaseDatabase.getInstance().getReference("treinoResistencia")
        val treinoData = mapOf(
            "tempoTreino" to tempoTreino,
            "golsFeitos" to gols,
            "desgaste" to desgaste
        )

        db.push().setValue(treinoData)
            .addOnSuccessListener {
                Log.d("Firebase", "Treino salvo com sucesso!")
                Toast.makeText(applicationContext, "Treino salvo!", Toast.LENGTH_SHORT).show()

                // Redireciona para Avaliação de Desgaste
                val intent = Intent(this@TreinoDeResistencia, AvaliacaoDesgaste::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erro ao salvar treino: ${e.message}")
                Toast.makeText(applicationContext, "Erro ao salvar treino.", Toast.LENGTH_SHORT).show()
            }
    }
}
