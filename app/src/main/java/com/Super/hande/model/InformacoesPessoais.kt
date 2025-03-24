package com.Super.hande.model

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.R
import com.Super.hande.databinding.ActivityInformacoesPessoaisBinding
import com.google.firebase.database.DatabaseReference
import android.widget.RadioButton
import android.widget.Toast
import com.Super.hande.R.id
import com.google.firebase.database.FirebaseDatabase


class InformacoesPessoais : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesPessoaisBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_informacoes_pessoais)
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

         */

        // Referência ao Firebase
        db = FirebaseDatabase.getInstance().getReference("jogadores")

        // Botão de salvar
        binding.btnSalvar.setOnClickListener {
            salvarInformacoes()
        }

        }

        private fun salvarInformacoes() {
            val idade = binding.etIdade.text.toString().trim()
            val altura = binding.etAltura.text.toString().trim()
            val peso = binding.etPeso.text.toString().trim()

            // Pegando o texto da opção selecionada em Mão Dominante
            val maoDominanteId = binding.rgMaoDominante.checkedRadioButtonId
            val maoDominante = if (maoDominanteId != -1) {
                findViewById<RadioButton>(maoDominanteId).text.toString()
            } else {
                ""
            }

            // Pegando o texto da opção selecionada em Posição em Quadra
            val posicaoQuadraId = binding.rgPosicaoQuadra.checkedRadioButtonId
            val posicaoQuadra = if (posicaoQuadraId != -1) {
                findViewById<RadioButton>(posicaoQuadraId).text.toString()
            } else {
                ""
            }

            // Verifica se os campos obrigatórios estão preenchidos
            if (idade.isEmpty() || altura.isEmpty() || peso.isEmpty() || maoDominante.isEmpty() || posicaoQuadra.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return
            }

            // Criando um objeto com os dados
            val jogador = mapOf(
                "idade" to idade.toInt(),
                "altura" to altura.toFloat(),
                "peso" to peso.toFloat(),
                "maoDominante" to maoDominante,
                "posicaoQuadra" to posicaoQuadra
            )

            // Salvando no Firebase
            db.child("jogador1").setValue(jogador).addOnSuccessListener {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar os dados!", Toast.LENGTH_SHORT).show()
            }
        }
    }

