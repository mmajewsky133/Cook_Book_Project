package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.databinding.ItemIngredienteDetailsBinding

class IngredientsDetailsCardAdapter(private val onIngredienteClicked: (Ingrediente) -> Unit) :
    ListAdapter<Ingrediente, IngredientsDetailsCardAdapter.IngredienteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val binding = ItemIngredienteDetailsBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return IngredienteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: IngredienteViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onIngredienteClicked(current)
        }
        holder.bind(current)
    }

    class IngredienteViewHolder(private val binding: ItemIngredienteDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingrediente: Ingrediente) {
            binding.apply {
                tvIngrediente.text = ingrediente.nombreIngrediente
                tvCantidadIngrediente.text =
                    parseCant(ingrediente.cantIngrediente, ingrediente.medidaIngrediente)
                tvCaloriasIngrediente.text = parseCalorias(ingrediente.caloriasIngrediente)
            }
        }

        private fun parseCalorias(kcal: Int): String {
            if (kcal.equals(0))
                return "Calorias estimadas: Pendiente"

            return "Calorias estimadas: $kcal kcal"
        }

        private fun parseCant(cantIngrediente: Int, medidaIngrediente: String): String {
            if (medidaIngrediente.equals("al gusto")) {
                return medidaIngrediente
            }
            return "$cantIngrediente - $medidaIngrediente"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingrediente>() {
            override fun areItemsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
                return oldItem.nombreIngrediente == newItem.nombreIngrediente
            }
        }
    }

}