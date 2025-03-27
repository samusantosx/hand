package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerformanceJogadorBinding
import com.google.firebase.database.*

class PerformanceJogador : AppCompatActivity() {

    private lateinit var binding: ActivityPerformanceJogadorBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPerformanceJogadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicia conexão com o Firebase
        db = FirebaseDatabase.getInstance().getReference("performance")

        iniciarPerformance()
    }

    private fun iniciarPerformance() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val velocidade = snapshot.child("velocidade").getValue(Double::class.java)?.toFloat() ?: 0.0f
                    val precisao = snapshot.child("precisao").getValue(Double::class.java)?.toFloat() ?: 0.0f
                    val gols  = snapshot.child("Frenquecia de gols").getValue(Double::class.java)?.toFloat() ?: 0.0f
/*
                   /* binding.speedText.text = "Velocidade: $velocidade m/s"
                    binding.precisionText.text = "Precisão: $precisao%"
                    binding.goalsFrequencyText.text = "Frenquecia de gols: $gols"
                    
                    */


 */

                } else {
                    Toast.makeText(applicationContext, "Nenhum dado encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao conectar com o Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
