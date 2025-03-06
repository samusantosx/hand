package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDeResistenciaBinding
import com.google.firebase.database.*

class TreinoDeResistencia : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeResistenciaBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityTreinoDeResistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Conectar ao Firebase para receber os dados de resistência (gols por minuto)
        db = FirebaseDatabase.getInstance().getReference("treinoResistencia")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val golsPorMinuto = snapshot.getValue(Int::class.java)
                if (golsPorMinuto != null) {
                    binding.goalsValue.text = "Gols por minuto: $golsPorMinuto"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase", Toast.LENGTH_SHORT).show()
            }
        })

        // Configura o botão para voltar ao menu principal
        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()  // Fecha a tela atual para evitar sobreposição de atividades
        }
    }
}
