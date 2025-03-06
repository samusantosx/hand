package com.Super.hande.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.databinding.ActivityLoginBinding
import com.Super.hande.databinding.HistoricoItemBinding
import com.Super.hande.model.SessaoDeTreino

class AdapterHistorico(private val context: Context, private val listaHistorico: MutableList<SessaoDeTreino>): RecyclerView.Adapter<AdapterHistorico.SessaoDeTreinoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessaoDeTreinoViewHolder {
       val itemLista = HistoricoItemBinding.inflate(LayoutInflater.from(context), parent,false)
        return SessaoDeTreinoViewHolder(itemLista)
    }

    override fun getItemCount() = listaHistorico.size

    override fun onBindViewHolder(holder: SessaoDeTreinoViewHolder, position: Int) {
        val historico = listaHistorico[position]
        holder.historicoData.text = historico.date
        holder.velocidadeData.text = historico.speed.toString()
        holder.precisaoData.text = historico.precision.toString()
        holder.golsData.text = historico.goals.toString()


    }

    inner class SessaoDeTreinoViewHolder(binding:HistoricoItemBinding ): RecyclerView.ViewHolder(binding.root){
        val historicoData = binding.txtDataHistorico
        val velocidadeData = binding.txtTipoTreinoHistorico
        val precisaoData = binding.txtTipoTreinoPrecisaoHistorico
        val golsData = binding.txtTipoTreinoGolsHistorico
    }

}