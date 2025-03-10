package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDePrecisaoBinding
import com.google.firebase.database.*

class TreinoDePrecisao : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDePrecisaoBinding
    private lateinit var db: DatabaseReference

    private lateinit var goalOverlayView: GoalOverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTreinoDePrecisaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        goalOverlayView = binding.goalOverlayView

        // Conectar ao Firebase para receber os dados de performance (gols, precisão e velocidade)
        db = FirebaseDatabase.getInstance().getReference("performance")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val precisao = snapshot.child("precisao").getValue(Int::class.java) ?: 0
                val gols = snapshot.child("gols").getValue(Int::class.java) ?: 0
                val velocidade = snapshot.child("velocidade").getValue(Int::class.java) ?: 0

                binding.precisionValue.text = "Precisão: $precisao%"
                Log.d("Firebase", "Precisão: $precisao, Gols: $gols, Velocidade: $velocidade")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase", Toast.LENGTH_SHORT).show()
            }
        })

        // Configura o botão para voltar ao menu principal
        binding.backToMenuButton.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()
        }

        monitorarArremessos()
    }

    private fun monitorarArremessos() {
        val sensoresDb = FirebaseDatabase.getInstance().getReference("precisao")

        sensoresDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sensorTopLeft = snapshot.child("sensor_top_left").getValue(Int::class.java) ?: 100
                val sensorTopRight = snapshot.child("sensor_top_right").getValue(Int::class.java) ?: 100
                val sensorBottomLeft = snapshot.child("sensor_bottom_left").getValue(Int::class.java) ?: 100
                val sensorBottomRight = snapshot.child("sensor_bottom_right").getValue(Int::class.java) ?: 100

                val acertos = mutableListOf<Pair<Float, Float>>()
                val width = goalOverlayView.width.toFloat()
                val height = goalOverlayView.height.toFloat()

                if (sensorTopLeft < 20) acertos.add(Pair(width * 0.1f, height * 0.1f))
                if (sensorTopRight < 20) acertos.add(Pair(width * 0.9f, height * 0.1f))
                if (sensorBottomLeft < 20) acertos.add(Pair(width * 0.1f, height * 0.9f))
                if (sensorBottomRight < 20) acertos.add(Pair(width * 0.9f, height * 0.9f))

                goalOverlayView.atualizarPontos(acertos)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao ler dados", error.toException())
            }
        })
    }
}
