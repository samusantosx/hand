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
import com.google.firebase.database.*
import kotlin.math.max

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding
    private lateinit var db: DatabaseReference
    private var startTime: Long = 0
    private var tempoTotalTreino: Int = 0
    private var intensidadeTreino: Int = (5..10).random()
    private var golsMarcados = 0
    private var desgasteRecebido: Double = 0.0
    private var tempoTreinoSalvo: Int = 0
    private var treinoFinalizado = false

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (!treinoFinalizado) {
                atualizarTempoTreino()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().getReference("treinoResistencia")

        // Resetar estado quando vier de uma avaliação de desgaste
        if (intent.getBooleanExtra("novoTreino", false)) {
            resetarTreino()
        }

        desgasteRecebido = intent.getDoubleExtra("desgaste", 0.0)
        binding.tvDesgaste.text = "Desgaste: $desgasteRecebido/10"

        // Se for um novo treino (vindo da avaliação), começa do zero
        if (intent.getBooleanExtra("novoTreino", false)) {
            tempoTreinoSalvo = 0
            startTime = SystemClock.elapsedRealtime()
            treinoFinalizado = false
        } else {
            recuperarTempoSalvo()
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnFinalizarTreino.setOnClickListener {
            finalizarTreino()
        }
    }

    private fun resetarTreino() {
        tempoTotalTreino = 0
        tempoTreinoSalvo = 0
        startTime = SystemClock.elapsedRealtime()
        treinoFinalizado = false
        golsMarcados = 0
    }

    override fun onResume() {
        super.onResume()
        if (!treinoFinalizado) {
            if (startTime == 0L) {
                startTime = SystemClock.elapsedRealtime() - tempoTreinoSalvo * 1000L
            }
            handler.post(updateRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!treinoFinalizado) {
            val tempoAtual = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()
            tempoTreinoSalvo += tempoAtual
            handler.removeCallbacks(updateRunnable)
        }
    }

    private fun recuperarTempoSalvo() {
        db.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (treino in snapshot.children) {
                    tempoTreinoSalvo = treino.child("tempoTreino").getValue(Int::class.java) ?: 0
                    atualizarTempoTreino()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao recuperar tempo: ${error.message}")
            }
        })
    }

    private fun atualizarTempoTreino() {
        val tempoAtual = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()
        val tempoTotal = tempoTreinoSalvo + tempoAtual

        val minutos = tempoTotal / 60
        val segundos = tempoTotal % 60

        binding.tvTempoTreino.text = "Tempo de treino: %02d:%02d".format(minutos, segundos)
    }

    private fun finalizarTreino() {
        treinoFinalizado = true
        val tempoAtual = ((SystemClock.elapsedRealtime() - startTime) / 1000).toInt()
        tempoTotalTreino = tempoTreinoSalvo + tempoAtual
        val desgaste = calcularDesgaste(tempoTotalTreino / 60, intensidadeTreino)

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

                val intent = Intent(this@TreinoDeResistencia, AvaliacaoDesgaste::class.java)
                intent.putExtra("tempoTreino", tempoTotalTreino)
                intent.putExtra("novoTreino", true)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erro ao salvar treino: ${e.message}")
                Toast.makeText(applicationContext, "Erro ao salvar treino.", Toast.LENGTH_SHORT).show()
            }
    }
}