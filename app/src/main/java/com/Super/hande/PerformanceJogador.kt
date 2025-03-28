package com.Super.hande

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerformanceJogadorBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*

class PerformanceJogador : AppCompatActivity() {

    private lateinit var binding: ActivityPerformanceJogadorBinding

    private lateinit var database: DatabaseReference
    private val precisaoList = mutableListOf<Entry>()
    private val velocidadeList = mutableListOf<Entry>()
    private val golsList = mutableListOf<BarEntry>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPerformanceJogadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa Firebase
        database = FirebaseDatabase.getInstance().getReference("performance")

        // Observa dados em tempo real
        observarPerformanceEmTempoReal()
    }

    private fun observarPerformanceEmTempoReal() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(this@PerformanceJogador, "Nenhum dado encontrado!", Toast.LENGTH_SHORT).show()
                    return
                }

                val precisao = snapshot.child("precisao").getValue(Int::class.java) ?: 0
                val velocidade = snapshot.child("velocidade").getValue(Int::class.java) ?: 0
                val gols = snapshot.child("gols").getValue(Int::class.java) ?: 0

                Log.d("FirebaseData", "Precisão: $precisao")
                Log.d("FirebaseData", "Velocidade: $velocidade")
                Log.d("FirebaseData", "Gols: $gols")

                // Atualiza as listas de histórico com novo dado e índice
                precisaoList.add(Entry(precisaoList.size.toFloat(), precisao.toFloat()))
                velocidadeList.add(Entry(velocidadeList.size.toFloat(), velocidade.toFloat()))
                golsList.add(BarEntry(golsList.size.toFloat(), gols.toFloat()))

                updateChart(binding.precisionChart, precisaoList, "Precisão")
                updateChart(binding.speedChart, velocidadeList, "Velocidade")
                updateBarChart(binding.goalChart, golsList, "Gols")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Erro ao buscar dados", error.toException())
                Toast.makeText(this@PerformanceJogador, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateChart(chart: LineChart, entries: List<Entry>, label: String) {
        val dataSet = LineDataSet(entries, label).apply {
            color = resources.getColor(R.color.purple_500, null)
            valueTextSize = 12f
            setDrawValues(true)
            setDrawCircles(true)  // Exibir pontos nos dados
            setCircleColor(resources.getColor(R.color.black, null))
            lineWidth = 2f
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    private fun updateBarChart(chart: BarChart, entries: List<BarEntry>, label: String) {
        val dataSet = BarDataSet(entries, label).apply {
            color = resources.getColor(R.color.verde_suave, null)
            valueTextSize = 12f
        }

        chart.data = BarData(dataSet)
        chart.invalidate()
    }
}

