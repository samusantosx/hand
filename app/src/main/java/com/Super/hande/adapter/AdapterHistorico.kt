package com.Super.hande.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.Super.hande.databinding.HistoricoItemBinding
import com.Super.hande.model.SessaoDeTreino

class AdapterHistorico(private var listaHistorico: List<SessaoDeTreino>) :
    RecyclerView.Adapter<AdapterHistorico.SessaoDeTreinoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessaoDeTreinoViewHolder {
        val binding = HistoricoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessaoDeTreinoViewHolder(binding)
    }

    override fun getItemCount() = listaHistorico.size

    override fun onBindViewHolder(holder: SessaoDeTreinoViewHolder, position: Int) {
        val historico = listaHistorico[position]
        holder.bind(historico)
    }

    fun atualizarLista(novaLista: List<SessaoDeTreino>) {
        val diffCallback = TreinoDiffCallback(listaHistorico, novaLista)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listaHistorico = novaLista
        diffResult.dispatchUpdatesTo(this)
    }

    inner class SessaoDeTreinoViewHolder(private val binding: HistoricoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historico: SessaoDeTreino) {
            binding.txtDataHistorico.text = historico.date
            binding.txtTipoTreinoHistorico.text = historico.speed.toString()
            binding.txtTipoTreinoPrecisaoHistorico.text = historico.precision.toString()
            binding.txtTipoTreinoGolsHistorico.text = historico.goals.toString()
        }
    }

    class TreinoDiffCallback(
        private val oldList: List<SessaoDeTreino>,
        private val newList: List<SessaoDeTreino>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].date == newList[newItemPosition].date
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
