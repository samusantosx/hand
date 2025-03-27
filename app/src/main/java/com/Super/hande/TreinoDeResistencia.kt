package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityTreinoDeResistenciaBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding
    private lateinit var db: DatabaseReference
    private var startTime: Long = 0
    private var tempoTotalTreino: Int = 0
    private var intensidadeTreino: Int = (5..10).random()
    private var golsMarcados = 0
    private var desgasteRecebido: Double = 0.0

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conectar ao Firebase
        db = FirebaseDatabase.getInstance().getReference("treinoResistencia")

        // Iniciar o cronômetro
        startTime = SystemClock.elapsedRealtime()

        // Verifica se há um nível de desgaste vindo da Avaliação de Desgaste
        desgasteRecebido = intent.getDoubleExtra("desgaste", 0.0)
        binding.tvDesgaste.text = "Desgaste: $desgasteRecebido/10"

        // Atualiza a exibição do tempo de treino dinamicamente
        atualizarTempoTreino()

        // Botão de voltar
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        // Botão para finalizar treino
        binding.btnFinalizarTreino.setOnClickListener {
            finalizarTreino()
        }
    }

    private fun atualizarTempoTreino() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                tempoTotalTreino = ((SystemClock.elapsedRealtime() - startTime) / 1000 / 60).toInt()
                binding.tvTempoTreino.text = "Tempo médio de treino: $tempoTotalTreino min"
                handler.postDelayed(this, 1000) // Atualiza a cada segundo
            }
        }, 0)
    }

    private fun finalizarTreino() {
        tempoTotalTreino = ((SystemClock.elapsedRealtime() - startTime) / 1000 / 60).toInt()
        val desgaste = calcularDesgaste(tempoTotalTreino, intensidadeTreino)

        // Salvar no Firebase e redirecionar para Avaliação de Desgaste
        salvarNoFirebase(tempoTotalTreino, golsMarcados, desgaste)
    }

    private fun calcularDesgaste(tempoTreino: Int, intensidade: Int): Double {
        return max(0.0, (tempoTreino * intensidade * 0.5))
    }

    private fun salvarNoFirebase(tempoTreino: Int, gols: Int, desgaste: Double) {
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
                intent.putExtra("tempoTreino", tempoTotalTreino)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erro ao salvar treino: ${e.message}")
                Toast.makeText(applicationContext, "Erro ao salvar treino.", Toast.LENGTH_SHORT).show()
            }
    }
}
