package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDeVelocidadeBinding
import com.google.firebase.database.*

class TreinoDeVelocidade : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeVelocidadeBinding
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando ViewBinding
        binding = ActivityTreinoDeVelocidadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener para aplicar os insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Conectar ao Firebase para receber os dados da velocidade da bola
        db = FirebaseDatabase.getInstance().getReference("treinoVelocidade")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val velocidade = snapshot.getValue(Double::class.java)
                if (velocidade != null) {
                    binding.speedValue.text = "Velocidade da bola: ${velocidade} m/s"
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
