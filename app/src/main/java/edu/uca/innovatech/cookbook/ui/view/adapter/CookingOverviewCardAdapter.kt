package edu.uca.innovatech.cookbook.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uca.innovatech.cookbook.core.util.parseNumPaso
import edu.uca.innovatech.cookbook.core.util.parsePasoTip
import edu.uca.innovatech.cookbook.core.util.parseTiempoPrep
import edu.uca.innovatech.cookbook.data.database.entities.CookingWReceta
import edu.uca.innovatech.cookbook.databinding.ItemCookingOverviewBinding

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
                tvCurrentPaso.text = parseNumPaso(cookingWReceta.cookingReceta.currentStep)
                tvTipPaso.text = parsePasoTip(cookingWReceta.cookingReceta.currentStep)
            }
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