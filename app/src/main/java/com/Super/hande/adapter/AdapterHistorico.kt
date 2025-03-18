package com.Super.hande.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.databinding.HistoricoItemBinding
import com.Super.hande.model.SessaoDeTreino


class AdapterHistorico(private val context: Context, private val listaHistorico: MutableList<SessaoDeTreino>):
    RecyclerView.Adapter<AdapterHistorico.HistoricoTreinoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoTreinoViewHolder {
        val itemLista = HistoricoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoricoTreinoViewHolder(itemLista)
    }

    override fun getItemCount() = listaHistorico.size

    override fun onBindViewHolder(holder: HistoricoTreinoViewHolder, position: Int) {
        val treino = listaHistorico[position]
        holder.txtDataHistorico.text = "Data: ${treino.data}"
        holder.txtGols.text = "Gols: ${treino.gols}"
        holder.txtPrecisao.text = "Precisão: ${treino.precisao}%"
        holder.txtVelocidade.text = "Velocidade: ${treino.velocidade} km/h"
    }

    inner class HistoricoTreinoViewHolder(binding: HistoricoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val txtDataHistorico = binding.txtDataHistorico
        val txtGols = binding.txtTipoTreinoGolsHistorico
        val txtPrecisao = binding.txtTipoTreinoPrecisaoHistorico
        val txtVelocidade = binding.txtTipoTreinoHistorico
    }

}
