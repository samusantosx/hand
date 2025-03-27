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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class InformacoesPessoais : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesPessoaisBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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

        // Inicializando Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Configurando evento de clique no botão salvar
        binding.btnSalvar.setOnClickListener {
            salvarInformacoes()
        }
    }

    private fun salvarInformacoes() {
        val usuarioAtual = auth.currentUser
        if (usuarioAtual == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = usuarioAtual.uid
        val nome = intent.getStringExtra("nome") ?: usuarioAtual.displayName ?: "Desconhecido"
        val email = intent.getStringExtra("email") ?: usuarioAtual.email ?: "Sem email"


        val idade = binding.etIdade.text.toString()
        val altura = binding.etAltura.text.toString()
        val peso = binding.etPeso.text.toString()

        // Pegando a mão dominante selecionada
        val idMaoDominante = binding.rgMaoDominante.checkedRadioButtonId
        val maoDominante = findViewById<RadioButton>(idMaoDominante)?.text?.toString() ?: ""

        // Pegando a posição em quadra selecionada
        val idPosicaoQuadra = binding.rgPosicaoQuadra.checkedRadioButtonId
        val posicaoQuadra = findViewById<RadioButton>(idPosicaoQuadra)?.text?.toString() ?: ""

        if (idade.isEmpty() || altura.isEmpty() || peso.isEmpty() || maoDominante.isEmpty() || posicaoQuadra.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        // Criando um mapa de dados para salvar no Firestore
        val usuario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "idade" to idade,
            "altura" to altura,
            "peso" to peso,
            "maoDominante" to maoDominante,
            "posicaoQuadra" to posicaoQuadra
        )

        db.collection("usuarios").document(userId).set(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()

                // Ir para a próxima tela após salvar
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}


