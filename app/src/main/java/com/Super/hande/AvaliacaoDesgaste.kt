package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityAvaliacaoDesgasteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AvaliacaoDesgaste : AppCompatActivity() {

    private lateinit var binding: ActivityAvaliacaoDesgasteBinding
    private lateinit var db: DatabaseReference
    private var isSaving = false  // Flag para evitar múltiplos cliques

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout com ViewBinding
        binding = ActivityAvaliacaoDesgasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conecta ao Firebase
        db = FirebaseDatabase.getInstance().getReference("avaliacoesDesgaste")

        // Configuração do SeekBar
        binding.seekBarDesgaste.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvResultadoDesgaste.text = "Nível de desgaste: $progress/10"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Botão para salvar avaliação no Firebase
        binding.btnSalvarAvaliacao.setOnClickListener {
            salvarAvaliacao()
        }
    }

    private fun salvarAvaliacao() {
        if (isSaving) return  // Evita múltiplos cliques no botão

        isSaving = true
        val desgasteNivel = binding.seekBarDesgaste.progress
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val avaliacaoData = mapOf(
            "desgasteNivel" to desgasteNivel,
            "dataHora" to currentDate
        )

        db.push().setValue(avaliacaoData)
            .addOnSuccessListener {
                Log.d("Firebase", "Avaliação de desgaste salva!")
                Toast.makeText(this, "Avaliação salva com sucesso!", Toast.LENGTH_SHORT).show()
                irParaMenu()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Erro ao salvar avaliação: ${e.message}")
                Toast.makeText(this, "Erro ao salvar avaliação: ${e.message}", Toast.LENGTH_LONG).show()
                isSaving = false  // Libera o botão para tentar novamente
            }
    }

    private fun irParaMenu() {
        startActivity(Intent(this, MenuPrincipal::class.java))
        finish()
    }
}
