package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.databinding.ActivityHistoricoDeTreinoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoricoDeTreino : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoDeTreinoBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //crie ess linha em outras telas
        binding = ActivityHistoricoDeTreinoBinding.inflate(layoutInflater)

        //mude essa lina tambem
        setContentView(binding.root)

        setContentView(R.layout.activity_historico_de_treino)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Certifique-se de que as permissões de Bluetooth foram solicitadas e concedidas
        /*
        if (BluetoothManager.connect()) {
            Toast.makeText(this, "Conectado ao Arduino", Toast.LENGTH_SHORT).show()
            receiveTrainingData()  // Receber os dados do Arduino
        } else {
            Toast.makeText(this, "Erro ao conectar", Toast.LENGTH_SHORT).show()
        }

        private fun receiveTrainingData() {
            // Usar coroutines em vez de Thread.sleep para melhor desempenho
            Thread {
                try {
                    while (true) {
                        val data = BluetoothManager.receiveData()  // Recebe dados do Arduino
                        data?.let {
                            val parts = it.split(",")
                            if (parts.size == 3) {
                                // Criação de uma nova sessão de treino com os dados recebidos
                                val session = TrainingSession(
                                    date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                        Date()
                                    ),
                                    accuracy = parts[0].toInt(),
                                    speed = parts[1].toDouble(),
                                    goals = parts[2].toInt()
                                )
                                runOnUiThread {
                                    // Adiciona a nova sessão ao início da lista
                                    HistoricoDeTreinoList.add(0, session)
                                    // Aqui você pode atualizar a UI diretamente, se necessário
                                    // Por exemplo, você pode usar um LiveData ou StateFlow para observar as mudanças na lista
                                }
                            }
                        }
                        // Pausa de 2 segundos entre as leituras de dados
                        Thread.sleep(2000)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }

         */
    }
}