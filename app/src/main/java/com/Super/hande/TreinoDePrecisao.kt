package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDePrecisaoBinding
import com.google.firebase.database.*

class TreinoDePrecisao : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDePrecisaoBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTreinoDePrecisaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Conectar ao Firebase para receber dados do treino de precisão
        db = FirebaseDatabase.getInstance().getReference("treinoPrecisao")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val precisao = snapshot.getValue(Int::class.java)
                if (precisao != null) {
                    binding.precisionValue.text = "Precisão: $precisao%"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase", Toast.LENGTH_SHORT).show()
            }
        })

        // Configura o botão para voltar ao menu principal
        binding.backToMenuButton.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()
        }
    }
}
