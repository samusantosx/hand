package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Define o layout da atividade
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa o Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializa os elementos da interface
        val emailInput = binding.emailInput.text
        val passwordInput = binding.passwordInput.text

        // Configura o listener para o botão de login
       binding.loginButton.setOnClickListener {
            val email = emailInput.toString().trim()
            val password = passwordInput.toString().trim()

            // Valida se os campos estão preenchidos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tenta fazer login com Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MenuPrincipal::class.java))
                        finish()  // Fecha a atividade atual para evitar sobreposição
                    } else {
                        Toast.makeText(
                            this,
                            "Erro no login: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Configura o listener para o texto de cadastro
       binding.registerText.setOnClickListener {
            //mudar o "Login" para a "Cadastro"
            startActivity(Intent(this, Cadastro::class.java))  // Navega para a tela de cadastro
        }
    }
}