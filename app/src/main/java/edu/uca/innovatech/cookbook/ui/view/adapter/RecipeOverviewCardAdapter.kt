package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
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
                ivReceta.setImageBitmap(receta.bitmapImagen)
                ivReceta.scaleType = ImageView.ScaleType.CENTER_CROP
                tvNombreReceta.text = receta.nombre
                tvTiempoPrepReceta.text = parseTiempoPrep(receta.tiempoPrep)
                tvCaloriesReceta.text = parseCalorias(receta.calorias)
            }
        }

        private fun parseTiempoPrep(tiempoPrep: Int): String{
            if (tiempoPrep.equals(0))
                return "Tiempo de preparacion: Pendiente"

            return "Tiempo de preparacion: $tiempoPrep m"
        }

        private fun parseCalorias(kcal: Int): String{
            if (kcal.equals(0))
                return "Calorias estimadas: Pendiente"

            return "Calorias estimadas: $kcal kcal"
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