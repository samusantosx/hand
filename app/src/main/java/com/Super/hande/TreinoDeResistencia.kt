package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityTreinoDeResistenciaBinding
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding
    private var startTime: Long = 0
    private var tempoTotalTreino: Int = 0
    private var intensidadeTreino: Int = (5..10).random() // Simula intensidade aleatória de 5 a 10
    private var golsMarcados = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia o cronômetro quando o usuário entra na tela
        startTime = SystemClock.elapsedRealtime()

        // Simula gols sendo marcados durante o treino
        binding.goalsValue.text = "Gols por minuto: ${calcularGolsPorMinuto()}"

        // Atualiza a resistência quando o treino termina (ao sair da tela)
        binding.btnVoltar.setOnClickListener {
            finalizarTreino()
        }
    }

    private fun finalizarTreino() {
        tempoTotalTreino = ((SystemClock.elapsedRealtime() - startTime) / 1000 / 60).toInt() // Converte ms para minutos

        val desgaste = calcularDesgaste(tempoTotalTreino, intensidadeTreino)

        // Atualiza os valores na tela
        binding.tvTempoTreino.text = "Tempo médio de treino: $tempoTotalTreino min"
        binding.tvDesgaste.text = "Desgaste: %.2f%%".format(desgaste)
        binding.goalsValue.text = "Gols por minuto: ${calcularGolsPorMinuto()}"

        // Salva os dados no Firebase
        salvarNoFirebase(tempoTotalTreino, golsMarcados, desgaste)

        // Volta ao menu
        startActivity(Intent(this, MenuPrincipal::class.java))
        finish()
    }

    private fun calcularGolsPorMinuto(): Double {
        return if (tempoTotalTreino > 0) golsMarcados.toDouble() / tempoTotalTreino else 0.0
    }

    private fun calcularDesgaste(tempoTreino: Int, intensidade: Int): Double {
        return max(0.0, (tempoTreino * intensidade * 0.5)) // Fórmula simples de desgaste
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
                Toast.makeText(applicationContext, "Treino salvo!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Erro ao salvar treino.", Toast.LENGTH_SHORT).show()
            }

        // Quando o treino acabar, iniciar a avaliação de desgaste
        val intent = Intent(this, AvaliacaoDesgaste::class.java)
        startActivity(intent)
        finish() // Fecha a tela do treino
    }
}
