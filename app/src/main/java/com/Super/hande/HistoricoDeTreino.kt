package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.adapter.AdapterHistorico
import com.Super.hande.databinding.ActivityHistoricoDeTreinoBinding
import com.Super.hande.model.SessaoDeTreino
import com.google.firebase.database.*


class HistoricoDeTreino : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoDeTreinoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterHistorico
    private lateinit var treinoList: MutableList<SessaoDeTreino>
    private lateinit var db: DatabaseReference

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

        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        treinoList = mutableListOf()
        adapter = AdapterHistorico(treinoList)
        recyclerView.adapter = adapter

        // Conectar ao Firebase para buscar os treinos salvos
        db = FirebaseDatabase.getInstance().getReference("historicoTreinos")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                treinoList.clear()
                for (treinoSnapshot in snapshot.children) {
                    val treino = treinoSnapshot.getValue(SessaoDeTreino::class.java)
                    if (treino != null) {
                        treinoList.add(0, treino) // Adiciona no início para manter a ordem cronológica
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    }
