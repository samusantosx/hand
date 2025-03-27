package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        binding = ActivityHistoricoDeTreinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
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

        db = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("historico")

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                treinoList.clear()
                if (!snapshot.exists()) {
                    Toast.makeText(applicationContext, "Nenhum histórico encontrado!", Toast.LENGTH_SHORT).show()
                    return
                }

                for (treinoSnapshot in snapshot.children) {
                    val treino = treinoSnapshot.getValue(SessaoDeTreino::class.java)
                    if (treino != null) {
                        treinoList.add(treino)
                    }
                }

                if (treinoList.isEmpty()) {
                    Toast.makeText(applicationContext, "Histórico vazio!", Toast.LENGTH_SHORT).show()
                }

                treinoList.sortByDescending { it.data } // Organiza por data
                adapterHistorico.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
