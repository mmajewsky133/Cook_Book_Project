package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.data.database.entities.CookingWReceta
import edu.uca.innovatech.cookbook.databinding.ItemCookingOverviewBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class CookingOverviewCardAdapter(private val onCookingClicked: (CookingWReceta) -> Unit) :
    ListAdapter<CookingWReceta, CookingOverviewCardAdapter.CocinandoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocinandoViewHolder {
        val binding = ItemCookingOverviewBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return CocinandoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CocinandoViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onCookingClicked(current)
        }
        holder.bind(current)
    }

    class CocinandoViewHolder(private val binding: ItemCookingOverviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cookingWReceta: CookingWReceta) {
            binding.apply {
                tvNombreReceta.text = cookingWReceta.receta.nombre
                tvTiempoPrepReceta.text = parseTiempoPrep(cookingWReceta.receta.tiempoPrep)
                tvCurrentPaso.text = parseCurrentPaso(cookingWReceta.cookingReceta.currentStep)
                tvTipPaso.text = parsePasoTip(cookingWReceta.cookingReceta.currentStep)
            }
        }

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

        private fun parseCurrentPaso(paso: Int): String{
            if (paso.equals(0)) return "Preparación de Ingredientes"
            return "Paso $paso:"
        }

        private fun parsePasoTip(paso: Int): String{
            if (paso.equals(0)) return "Presione para comenzar a cocinar!"
            return "Presione para seguir cocinando!"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CookingWReceta>() {
            override fun areItemsTheSame(oldItem: CookingWReceta, newItem: CookingWReceta): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: CookingWReceta, newItem: CookingWReceta): Boolean {
                return oldItem.cookingReceta.idCooking == newItem.cookingReceta.idCooking
            }
        }
    }
}