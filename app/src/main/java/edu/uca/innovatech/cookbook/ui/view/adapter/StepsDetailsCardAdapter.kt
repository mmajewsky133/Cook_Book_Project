package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.databinding.ItemPasoDetailsBinding

class StepsDetailsCardAdapter(private val onPasoClicked: (Paso) -> Unit) :
    ListAdapter<Paso, StepsDetailsCardAdapter.PasoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasoViewHolder {
        val binding = ItemPasoDetailsBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return PasoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PasoViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onPasoClicked(current)
        }
        holder.bind(current)
    }

    class PasoViewHolder(private val binding: ItemPasoDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paso: Paso) {
            binding.apply {
                ivPaso.setImageBitmap(paso.imagenPaso)
                tvPasoNum.text = parseNumPaso(paso.numPaso)
                tvTiempoPrepPaso.text = parseTiempoPrep(paso.tiempo)
                tvDetallesPaso.text = paso.detalle
            }
        }

        private fun parseNumPaso(numPaso: Int): String {
            return "Paso $numPaso:"
        }

        private fun parseTiempoPrep(tiempoPrep: Int): String {
            if (tiempoPrep.equals(0))
                return "Tiempo estimado:"

            return "Tiempo estimado: $tiempoPrep m"
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Paso>() {
            override fun areItemsTheSame(oldItem: Paso, newItem: Paso): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Paso, newItem: Paso): Boolean {
                return oldItem.numPaso == newItem.numPaso
            }
        }
    }

}