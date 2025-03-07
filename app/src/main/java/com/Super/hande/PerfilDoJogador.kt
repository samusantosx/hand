package com.Super.hande

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerfilDoJogadorBinding
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class PerfilDoJogador : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilDoJogadorBinding
    private val dbFirestore = FirebaseFirestore.getInstance()
    private val dbRealtime = FirebaseDatabase.getInstance().getReference("performance")

    private lateinit var performanceChart: LineChart
    private val performanceEntries = ArrayList<Entry>()
    private var timeIndex = 0 // Índice para o eixo X do gráfico

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

        // Inicializar gráfico
        performanceChart = binding.performanceChart
        configurarGrafico()

        buscarIdJogadorDoFirestore()
        observarPerformanceEmTempoReal()
    }

    private fun buscarIdJogadorDoFirestore() {
        dbFirestore.collection("jogador").document("01")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nome = document.getString("nome") ?: "Desconhecido"
                    binding.playerName.text = nome
                }
            }
            .addOnFailureListener {
                binding.playerName.text = "Erro ao carregar nome"
            }
    }

    private fun observarPerformanceEmTempoReal() {
        dbRealtime.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val gols = snapshot.child("gols").getValue(Int::class.java) ?: 0
                    val precisao = snapshot.child("precisao").getValue(Double::class.java) ?: 0.0
                    val velocidade = snapshot.child("velocidade").getValue(Double::class.java) ?: 0.0

                    // Cálculo da performance
                    val performance = (gols * 3) + (precisao * 2) + (velocidade * 1)

                    // Atualizar UI
                    binding.goalsText.text = gols.toString()
                    binding.accuracyText.text = String.format("%.2f", precisao)
                    binding.speedText.text = String.format("%.2f", velocidade)
                    binding.performanceText.text = String.format("%.2f", performance)

                    // Adicionar novo ponto ao gráfico
                    adicionarValorNoGrafico(performance)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.goalsText.text = "Erro ao carregar"
            }
        })
    }

    private fun configurarGrafico() {
        performanceChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.BLACK) // Define o fundo preto
            setDrawGridBackground(false)
        }

        val xAxis = performanceChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = Color.WHITE
            textSize = 12f
        }

        val leftAxis = performanceChart.axisLeft
        leftAxis.apply {
            textColor = Color.WHITE
            textSize = 12f
            setDrawGridLines(true)
            gridColor = Color.GRAY
            enableGridDashedLine(10f, 5f, 0f)
        }

        performanceChart.axisRight.isEnabled = false
    }


    private fun adicionarValorNoGrafico(valor: Double) {
        performanceEntries.add(Entry(timeIndex.toFloat(), valor.toFloat()))
        timeIndex++

        val dataSet = LineDataSet(performanceEntries, "Performance")
        dataSet.apply {
            color = Color.YELLOW
            setCircleColor(Color.RED)
            valueTextSize = 14f
            setDrawFilled(true)
            fillColor = Color.CYAN
            lineWidth = 3f
            mode = LineDataSet.Mode.CUBIC_BEZIER // Suavização da curva
            enableDashedLine(10f, 5f, 0f)
        }

        val lineData = LineData(dataSet)
        performanceChart.data = lineData
        performanceChart.animateX(1000) // Animação ao atualizar
        performanceChart.invalidate()
    }
}