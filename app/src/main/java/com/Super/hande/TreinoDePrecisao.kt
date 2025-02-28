package com.Super.hande

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityTreinoDePrecisaoBinding

class TreinoDePrecisao : AppCompatActivity() {

    private lateinit var binding: ActivityTreinoDePrecisaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityTreinoDePrecisaoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*
        // Recebendo dados do Arduino
        val accuracyData = BluetoothManager.receiveData() ?: "0"
        binding.accuracyValue.text = "Precisão: $accuracyData%"

        // Configura o listener para o botão de voltar ao menu
        binding.backToMenuButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

 */
    }
}