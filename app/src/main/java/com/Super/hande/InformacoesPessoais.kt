package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.Super.hande.databinding.ActivityInformacoesPessoaisBinding
import com.google.firebase.database.DatabaseReference
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityLoginBinding
import com.google.firebase.database.FirebaseDatabase


class InformacoesPessoais : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesPessoaisBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializando ViewBinding corretamente
        binding = ActivityInformacoesPessoaisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Inicializando Firebase Database
        db = FirebaseDatabase.getInstance().reference.child("Usuarios")

        // Recuperando os dados passados da tela de cadastro
        val nome = intent.getStringExtra("nome")
        val email = intent.getStringExtra("email")

        // Configurando evento de clique no botão salvar
        binding.btnSalvar.setOnClickListener {
            salvarInformacoes(nome, email)
        }
    }

    private fun salvarInformacoes(nome: String?, email: String?) {
        val idade = binding.etIdade.text.toString()
        val altura = binding.etAltura.text.toString()
        val peso = binding.etPeso.text.toString()

        // Pegando a mão dominante selecionada
        val idMaoDominante = binding.rgMaoDominante.checkedRadioButtonId
        val maoDominante = findViewById<RadioButton>(idMaoDominante)?.text.toString()

        // Pegando a posição em quadra selecionada
        val idPosicaoQuadra = binding.rgPosicaoQuadra.checkedRadioButtonId
        val posicaoQuadra = findViewById<RadioButton>(idPosicaoQuadra)?.text.toString()

        if (idade.isEmpty() || altura.isEmpty() || peso.isEmpty() || maoDominante.isEmpty() || posicaoQuadra.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        // Criando um mapa de dados para salvar no Firebase
        val usuario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "idade" to idade,
            "altura" to altura,
            "peso" to peso,
            "maoDominante" to maoDominante,
            "posicaoQuadra" to posicaoQuadra
        )

        db.child(nome ?: "Desconhecido").setValue(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()

                // Ir para a próxima tela após salvar
                val intent = Intent(this, Login::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("email", email)
                intent.putExtra("idade", idade)
                intent.putExtra("altura", altura)
                intent.putExtra("peso", peso)
                intent.putExtra("maoDominante", maoDominante)
                intent.putExtra("posicaoQuadra", posicaoQuadra)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar no Firebase!", Toast.LENGTH_SHORT).show()
            }
    }
}


