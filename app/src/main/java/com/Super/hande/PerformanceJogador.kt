package com.Super.hande

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Super.hande.databinding.ActivityPerformanceJogadorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PerformanceJogador : AppCompatActivity() {

    private lateinit var binding: ActivityPerformanceJogadorBinding
    private lateinit var db:DatabaseReference


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
        //Inicia o Firebase
        db = FirebaseDatabase.getInstance().getReference("performance")

        inciarPerformance()
    }

    private fun inciarPerformance(){
        db.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val velocidade = snapshot.child("velocidade").getValue(Double::class.java)?.toFloat()?:0.0
                    val precisao = snapshot.child("precisao").getValue(Double::class.java)?.toFloat()?:0.0
                    val resistencia = snapshot.child("resistencia").getValue(Double::class.java)?.toFloat()?:0.0

                    binding.speedText.text ="$velocidade"
                    binding.speedText.text ="$precisao"
                    binding.speedText.text ="$resistencia"

                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Toast.makeText(aplicationContext, "Erro ao conectar com o Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}