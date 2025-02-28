package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth

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

        // Configura os listeners dos botões
        binding.btnPlayerProfile.setOnClickListener {
            irTelaPerfilJogador()
        }

        binding.trainingModesButton.setOnClickListener {
            val intent = Intent(this, TrainingModesActivity::class.java)
            startActivity(intent)
        }

        binding.performanceButton.setOnClickListener {
            val intent = Intent(this, PerformanceActivity::class.java)
            startActivity(intent)
        }

        binding.historyButton.setOnClickListener {
            val intent = Intent(this, TrainingHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sair")
            .setMessage("Tem certeza de que deseja sair?")
            .setPositiveButton("Sim") { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun irTelaPerfilJogador() {
        val intent = Intent(this, PerfilDoJogador::class.java)
        startActivity(intent)
    }
}