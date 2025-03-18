package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.Super.hande.adapter.AdapterHistorico
import com.Super.hande.databinding.ActivityHistoricoDeTreinoBinding
import com.Super.hande.model.SessaoDeTreino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HistoricoDeTreino : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoDeTreinoBinding
    private lateinit var adapterHistorico: AdapterHistorico
    private val treinoList: MutableList<SessaoDeTreino> = mutableListOf()
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHistoricoDeTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // **Adiciona dados manuais para teste**
        adicionarDadosManuais()


        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtém o ID do usuário autenticado
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não autenticado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Configuração do RecyclerView
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistory.setHasFixedSize(true)
        adapterHistorico = AdapterHistorico(this, treinoList)
        binding.recyclerViewHistory.adapter = adapterHistorico

        // Referência ao Firebase Database
        db = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("historico")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                treinoList.clear()
                for (treinoSnapshot in snapshot.children) {
                    val treino = treinoSnapshot.getValue(SessaoDeTreino::class.java)
                    if (treino != null) {
                        treinoList.add(treino)
                    }
                }
                treinoList.reverse() // Mostra os treinos mais recentes primeiro
                adapterHistorico.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun adicionarDadosManuais() {
        treinoList.add(SessaoDeTreino("Treino de Força", "2025-03-15", "45 minutos", "Intensidade alta"))
        treinoList.add(SessaoDeTreino("Treino Cardio", "2025-03-14", "30 minutos", "Corrida moderada"))
        treinoList.add(SessaoDeTreino("Treino de Resistência", "2025-03-13", "40 minutos", "Treino funcional"))
    }
}
