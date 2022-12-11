package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.core.util.parseCalorias
import edu.uca.innovatech.cookbook.core.util.parseTiempoPrep
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.databinding.ItemRecipeSelectionBinding

class RecipeSelectionCardAdapter(private val onRecetaBtnClicked: (Receta) -> Unit) :
    ListAdapter<Receta, RecipeSelectionCardAdapter.RecetaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val binding = ItemRecipeSelectionBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return RecetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeSelectionCardAdapter.RecetaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class RecetaViewHolder(private val binding: ItemRecipeSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(receta: Receta) {
            binding.apply {
                ivReceta.setImageBitmap(receta.bitmapImagen)
                ivReceta.scaleType = ImageView.ScaleType.CENTER_CROP
                tvNombreReceta.text = receta.nombre
                tvAutorReceta.text = receta.autor
                tvTiempoPrepReceta.text = parseTiempoPrep(receta.tiempoPrep)
                tvCaloriesReceta.text = parseCalorias(receta.calorias)
                btnComenzarCooking.setOnClickListener{ onRecetaBtnClicked(receta) }
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