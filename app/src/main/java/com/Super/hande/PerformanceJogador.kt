package com.Super.hande

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityDashboardBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PerformanceJogador : AppCompatActivity() {

    private lateinit var accuracyText: TextView
    private lateinit var speedText: TextView
    private lateinit var goalFrequencyText: TextView
    private lateinit var accuracyChart: LineChart
    private lateinit var speedChart: LineChart
    private lateinit var goalChart: BarChart
    private lateinit var updatePerformanceButton: Button

    private lateinit var bluetoothConnection: BluetoothConnection // Comunicação com o Arduino

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_performance_jogador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

            accuracyText = findViewById(R.id.accuracyText)
            speedText = findViewById(R.id.speedText)
            goalFrequencyText = findViewById(R.id.goalFrequencyText)
            accuracyChart = findViewById(R.id.accuracyChart)
            speedChart = findViewById(R.id.speedChart)
            goalChart = findViewById(R.id.goalChart)
            updatePerformanceButton = findViewById(R.id.updatePerformanceButton)

            bluetoothConnection = BluetoothConnection() // Inicia conexão com o Arduino

            updatePerformanceButton.setOnClickListener {
                atualizarDados()
            }
        }

        private fun atualizarDados() {
            val dadosRecebidos = bluetoothConnection.receberDadosDoArduino()

            if (dadosRecebidos != null) {
                accuracyText.text = "Precisão: ${dadosRecebidos.precisao}%"
                speedText.text = "Velocidade da Bola: ${dadosRecebidos.velocidade} m/s"
                goalFrequencyText.text = "Frequência de Gols: ${dadosRecebidos.frequenciaGols} gols/min"

                atualizarGraficos(dadosRecebidos)
            } else {
                Toast.makeText(this, "Erro ao obter dados!", Toast.LENGTH_SHORT).show()
            }
        }

        private fun atualizarGraficos(dados: PerformanceJogador) {
            // Configuração do gráfico de precisão
            val accuracyEntries = ArrayList<Entry>()
            accuracyEntries.add(Entry(System.currentTimeMillis().toFloat(), dados.precisao.toFloat()))
            val accuracyDataSet = LineDataSet(accuracyEntries, "Precisão (%)")
            accuracyChart.data = LineData(accuracyDataSet)
            accuracyChart.invalidate()

            // Configuração do gráfico de velocidade
            val speedEntries = ArrayList<Entry>()
            speedEntries.add(Entry(System.currentTimeMillis().toFloat(), dados.velocidade.toFloat()))
            val speedDataSet = LineDataSet(speedEntries, "Velocidade (m/s)")
            speedChart.data = LineData(speedDataSet)
            speedChart.invalidate()

            // Configuração do gráfico de frequência de gols
            val goalEntries = ArrayList<BarEntry>()
            goalEntries.add(BarEntry(System.currentTimeMillis().toFloat(), dados.frequenciaGols.toFloat()))
            val goalDataSet = BarDataSet(goalEntries, "Gols/min")
            goalChart.data = BarData(goalDataSet)
            goalChart.invalidate()
        }
    }

    private fun atualizarDados() {
        TODO("Not yet implemented")
    }
}