package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityAvaliacaoDesgasteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AvaliacaoDesgaste : AppCompatActivity() {

    private lateinit var binding: ActivityAvaliacaoDesgasteBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityAvaliacaoDesgasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conectar ao Firebase para salvar os dados do treino
        db = FirebaseDatabase.getInstance().getReference("treinoResistencia")

        // Configuração do SeekBar para autoavaliação do desgaste
        binding.seekBarDesgaste.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Atualiza o texto conforme o jogador move o slider
                binding.tvResultadoDesgaste.text = when (progress) {
                    in 1..3 -> "Baixo desgaste"
                    in 4..6 -> "Médio desgaste"
                    in 7..10 -> "Alto desgaste"
                    else -> "Selecione uma nota"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Botão para salvar avaliação no Firebase
        binding.btnSalvarAvaliacao.setOnClickListener {
            val desgasteNivel = binding.seekBarDesgaste.progress
            val avaliacao = when (desgasteNivel) {
                in 1..3 -> "Baixo desgaste"
                in 4..6 -> "Médio desgaste"
                in 7..10 -> "Alto desgaste"
                else -> "Não avaliado"
            }

            // Salvar no Firebase
            val treinoData = mapOf(
                "tempoTreino" to "30 min",  // Pode ser ajustado conforme a lógica do treino
                "desgaste" to avaliacao
            )

            db.child("avaliacaoDesgaste").setValue(treinoData)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Avaliação salva com sucesso!", Toast.LENGTH_SHORT).show()
                    // Voltar ao Menu após salvar
                    startActivity(Intent(this@AvaliacaoDesgaste, MenuPrincipal::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Erro ao salvar!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
