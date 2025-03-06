package com.Super.hande

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.databinding.ActivityTreinoAdapterBinding

class TreinoAdapter(private val treinoList: List<SessaoDeTreino>) :
    RecyclerView.Adapter<TreinoAdapter.TreinoViewHolder>() {

    class TreinoViewHolder(val binding: ActivityTreinoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val binding = ActivityTreinoAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TreinoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        val treino = treinoList[position]
        holder.binding.apply {
            textDate.text = "Data: ${treino.date}"
            textPrecision.text = "Precisão: ${treino.precision}%"
            textSpeed.text = "Velocidade: ${treino.speed} m/s"
            textGoals.text = "Gols: ${treino.goals}"
        }
    }

    override fun getItemCount(): Int = treinoList.size
}
