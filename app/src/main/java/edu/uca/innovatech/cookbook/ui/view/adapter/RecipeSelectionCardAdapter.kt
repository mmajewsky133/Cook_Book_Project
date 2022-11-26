package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.data.database.entities.Receta
import edu.uca.innovatech.cookbook.databinding.ItemRecipeSelectionBinding
import java.math.RoundingMode
import java.text.DecimalFormat

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

        //Obtiene el tiempo en minutos y lo pasa a horas si es mas de 60 minutos
        private fun parseTiempoPrep(tiempoPrep: Int): String{
            if (tiempoPrep.equals(0)){
                return "Tiempo de preparacion: Pendiente"
            } else if (tiempoPrep > 60){
                val tiempoPrepH: Double = ((tiempoPrep).toDouble())/60
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.CEILING

                return "Tiempo de preparacion: ${df.format(tiempoPrepH).toDouble()} h"
            }
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