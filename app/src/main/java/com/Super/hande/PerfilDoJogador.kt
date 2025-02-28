package com.Super.hande

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerfilDoJogadorBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.io.InputStream
import java.security.KeyStore
import java.util.UUID

class PerfilDoJogador : AppCompatActivity() {
    //crie essa linha em outras paginas
    private lateinit var binding: ActivityPerfilDoJogadorBinding
    private lateinit var db: FirebaseDatabase

    private lateinit var playerImage: ImageView
    private lateinit var playerName: TextView
    private lateinit var accuracyText: TextView
    private lateinit var speedText: TextView
    private lateinit var goalsText: TextView
    private lateinit var performanceChart: LineChart

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null

    private val deviceAddress = "00:11:22:33:44:55" // 🔹  módulo Bluetooth HC-05
    private val dataEntries = mutableListOf<KeyStore.Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //crie ess linha em outras telas
        binding = ActivityPerfilDoJogadorBinding.inflate(layoutInflater)

        //mude essa lina tambem
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playerImage = findViewById(R.id.playerImage)
        playerName = findViewById(R.id.playerName)
        accuracyText = findViewById(R.id.accuracyText)
        speedText = findViewById(R.id.speedText)
        goalsText = findViewById(R.id.goalsText)
        performanceChart = findViewById(R.id.performanceChart)


        playerName.text = ""
        /*
        // Tentar conectar ao Arduino via Bluetooth
        if (connectToBluetooth()) {
            receiveData()
        } else {
            accuracyText.text = "Erro ao conectar ao Bluetooth"
        }
    }

    private fun connectToBluetooth(): Boolean {
        bluetoothAdapter ?: return false

        val device: BluetoothDevice? = bluetoothAdapter.bondedDevices.find { it.address == deviceAddress }
        device ?: return false

        return try {
            val uuid: UUID = device.uuids[0].uuid
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            inputStream = bluetoothSocket?.inputStream
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun receiveData() {
        Thread {
            while (true) {
                val data = readFromBluetooth()
                data?.let {
                    val parts = it.split(",")
                    if (parts.size == 3) {
                        val accuracy = parts[0].toInt()
                        val speed = parts[1].toDouble()
                        val goals = parts[2].toInt()

                        runOnUiThread {
                            accuracyText.text = "Precisão Média: $accuracy%"
                            speedText.text = "Velocidade Média: $speed m/s"
                            goalsText.text = "Total de Gols: $goals"

                            updateChart(accuracy.toFloat(), speed.toFloat())
                        }
                    }
                }
                Thread.sleep(2000) // Atualiza os dados a cada 2 segundos
            }
        }.start()
    }

    private fun readFromBluetooth(): String? {
        return try {
            val buffer = ByteArray(256)
            val bytes = inputStream?.read(buffer) ?: 0
            String(buffer, 0, bytes)
        } catch (e: Exception) {
            null
        }
    }

    private fun updateChart(accuracy: Float, speed: Float) {
        val newEntry = KeyStore.Entry(dataEntries.size.toFloat(), accuracy)
        dataEntries.add(newEntry)

        val dataSet = LineDataSet(dataEntries, "Precisão")
        val lineData = LineData(dataSet)
        performanceChart.data = lineData

        val description = Description()
        description.text = "Evolução da Precisão"
        performanceChart.description = description

        performanceChart.invalidate()
    }

 */
    }
}