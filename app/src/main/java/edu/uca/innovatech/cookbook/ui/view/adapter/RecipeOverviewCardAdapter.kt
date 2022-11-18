package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.databinding.ItemRecipeOverviewBinding


class RecipeOverviewCardAdapter(private val onReceteClicked: (Receta) -> Unit) :
    ListAdapter<Receta, RecipeOverviewCardAdapter.RecetaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val binding = ItemRecipeOverviewBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return RecetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onReceteClicked(current)
        }
        holder.bind(current)
    }

    class RecetaViewHolder(private val binding: ItemRecipeOverviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(receta: Receta) {
            binding.apply {
                ivReceta.setImageURI(receta.uriImagen)
                tvNombreReceta.text = receta.nombre
                tvTiempoPrepReceta.text = "Tiempo de preparacion:" + receta.tiempo + "m"
                tvCaloriesReceta.text = "Calorias estimadas: " + receta.calorias + "kcal"
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return oldItem.nombre == newItem.nombre
            }
        }
    }

}
