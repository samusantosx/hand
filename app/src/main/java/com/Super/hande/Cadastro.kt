package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Cadastro : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnSalvarCadastro.setOnClickListener {
            val nome = binding.etNome.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val senha = binding.etSenha.text.toString().trim()
            val confirmarSenha = binding.etConfirmarSenha.text.toString().trim()

            when {
                nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty() ->
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                senha != confirmarSenha ->
                    Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                else -> registrarUsuario(nome, email, senha)
            }
        }
    }

    private fun registrarUsuario(nome: String, email: String, senha: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val usuario = hashMapOf(
                            "id" to userId,
                            "nome" to nome,
                            "email" to email
                        )

                        db.collection("usuarios").document(userId).set(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                                // Redirecionar para a tela de Informações Pessoais e passar nome e e-mail
                                val intent = Intent(this, InformacoesPessoais::class.java)
                                intent.putExtra("USER_ID", userId)
                                intent.putExtra("nome", nome)  // Adicionando o nome
                                intent.putExtra("email", email) // Adicionando o e-mail
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Erro ao salvar dados no banco!", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Erro ao obter ID do usuário!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val erro = task.exception?.message ?: "Erro desconhecido"
                    when {
                        erro.contains("email address is already in use", ignoreCase = true) ->
                            Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show()
                        else ->
                            Toast.makeText(this, "Erro ao cadastrar: $erro", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}