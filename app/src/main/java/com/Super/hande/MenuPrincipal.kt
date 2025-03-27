package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


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

        // Inicializa Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Buscar e exibir os dados do usuário
        carregarDadosDoUsuario()
    }

    private fun carregarDadosDoUsuario() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid  // Obtém o ID do usuário autenticado

            db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        binding.userEmailTextView.text = document.getString("nome")
                        binding.txtIdadeJogador.text = document.getString("idade")
                        binding.txtAlturaJogador.text = document.getString("altura")
                        binding.txtPesoJogador.text = document.getString("peso")
                    } else {
                        Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao carregar dados!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
        }
    }
}
