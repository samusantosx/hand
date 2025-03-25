package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa o Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Verifica se o usuário está logado
        val usuarioAtual: FirebaseUser? = auth.currentUser
        if (usuarioAtual == null) {
            redirecionarParaLogin()
        } else {
            carregarDadosUsuario(usuarioAtual)
        }
/*
        // Configura os listeners dos botões
        binding.btnPlayerProfile.setOnClickListener {
            irTelaPerfilJogador()
        }

        binding.trainingModesButton.setOnClickListener {
            startActivity(Intent(this, ModoDeTreino::class.java))
        }

        binding.performanceButton.setOnClickListener {
            startActivity(Intent(this, PerformanceJogador::class.java))
        }

        binding.historyButton.setOnClickListener {
            startActivity(Intent(this, HistoricoDeTreino::class.java))
        }

        binding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
        
 */
    }

    private fun carregarDadosUsuario(usuario: FirebaseUser) {
        // Exibe o e-mail do usuário na interface (pode ser modificado para exibir nome, foto, etc.)
        binding.userEmailTextView.text = "Bem-vindo, ${usuario.email}"
    }

    private fun redirecionarParaLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sair")
            .setMessage("Tem certeza de que deseja sair?")
            .setPositiveButton("Sim") { _, _ ->
                auth.signOut()
                redirecionarParaLogin()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun irTelaPerfilJogador() {
        startActivity(Intent(this, PerfilDoJogador::class.java))
    }
}