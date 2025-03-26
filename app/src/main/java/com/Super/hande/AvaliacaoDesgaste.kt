package com.Super.hande

import android.content.Intent
import android.os.Bundle
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
            val desgasteNivel = binding.seekBarDesgaste.progress

            if (desgasteNivel == 0) {
                Toast.makeText(this, "Selecione um nível de desgaste!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val avaliacaoData = mapOf(
                "desgasteNivel" to desgasteNivel,
                "dataHora" to currentDate
            )

            db.push().setValue(avaliacaoData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Avaliação salva com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuPrincipal::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar avaliação!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
