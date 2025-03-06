package com.Super.hande

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerfilDoJogadorBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*

class PerfilDoJogador : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilDoJogadorBinding
    private lateinit var db: DatabaseReference

    private lateinit var playerImage: ImageView
    private lateinit var playerName: TextView
    private lateinit var accuracyText: TextView
    private lateinit var speedText: TextView
    private lateinit var goalsText: TextView
    private lateinit var performanceChart: LineChart

    private val dataEntries = mutableListOf<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPerfilDoJogadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playerImage = binding.playerImage
        playerName = binding.playerName
        accuracyText = binding.accuracyText
        speedText = binding.speedText
        goalsText = binding.goalsText
        performanceChart = binding.performanceChart

        // Nome do jogador (pode ser recuperado do Firebase Authentication no futuro)
        playerName.text = "Jogador 1"

        // Conectar ao Firebase
        db = FirebaseDatabase.getInstance().getReference("performance")

        // Atualizar os dados em tempo real
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dados = snapshot.getValue(PerformanceJogadorData::class.java)
                if (dados != null) {
                    atualizarUI(dados)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Erro ao acessar Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun atualizarUI(dados: PerformanceJogadorData) {
        accuracyText.text = "Precisão Média: ${dados.precisao}%"
        speedText.text = "Velocidade Média: ${dados.velocidade} m/s"
        goalsText.text = "Total de Gols: ${dados.frequenciaGols}"

        atualizarGrafico(dados.precisao.toFloat())
    }

    private fun atualizarGrafico(accuracy: Float) {
        val newEntry = Entry(dataEntries.size.toFloat(), accuracy)
        dataEntries.add(newEntry)

        val dataSet = LineDataSet(dataEntries, "Precisão")
        val lineData = LineData(dataSet)
        performanceChart.data = lineData

        val description = Description()
        description.text = "Evolução da Precisão"
        performanceChart.description = description

        performanceChart.invalidate()
    }
}
