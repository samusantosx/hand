package com.Super.hande

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDeVelocidadeBinding
import com.github.anastr.speedviewlib.SpeedView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class TreinoDeVelocidade : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDeVelocidadeBinding
    private lateinit var db: DatabaseReference
    private lateinit var velocimetro: SpeedView  // Adicionando referência ao velocímetro
    private lateinit var bottom: BottomNavigationView

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


        // Configurando BottomNavigationView
        bottom = findViewById(R.id.botton_navigation) // Verifique se o ID está correto no XML
        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MenuPrincipal::class.java))
                    true
                }
                R.id.modo_treino -> {
                    startActivity(Intent(this, ModoDeTreino::class.java))
                    true
                }
                R.id.historico -> {
                    startActivity(Intent(this, HistoricoDeTreino::class.java))
                    true
                }
                R.id.desempenho -> {
                    startActivity(Intent(this, PerformanceJogador::class.java))
                    true
                }
                R.id.perfil -> {
                    startActivity(Intent(this, PerfilDoJogador::class.java))
                    true
                }
                else -> false
            }
        }

        // Inicializa o Firebase Database
        db = FirebaseDatabase.getInstance().reference

        // Obtém a referência do velocímetro do layout
        velocimetro = binding.velocimetro

        // Atualiza o velocímetro em tempo real com dados do Firebase
        db.child("performance").child("velocidade").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val velocidade = snapshot.getValue(Float::class.java) ?: 0f
                velocimetro.speedTo(velocidade) // Atualiza o velocímetro
            }

            override fun onCancelled(error: DatabaseError) {
                // Trate possíveis erros aqui, como exibir um Toast ou Log
            }
        })

        /*
        // Configura o botão para voltar ao menu principal
        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            startActivity(intent)
            finish()  // Fecha a tela atual para evitar sobreposição de atividades
        }

         */
    }
}