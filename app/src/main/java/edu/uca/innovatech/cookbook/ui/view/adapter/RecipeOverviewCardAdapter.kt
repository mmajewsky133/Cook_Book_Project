package edu.uca.innovatech.cookbook.ui.view.adapter

/*
class RecipeOverviewCardAdapter()
    : RecyclerView.Adapter<RecipeOverviewCardAdapter.RecipeOverviewCardViewHolder>() {

    //TODO: private val recipeList = recipesDao

    /**
     * View Holder del rcv dentro del Adapter como un "inner class"
    */
    inner class RecipeOverviewCardViewHolder(val binding: ItemRecipeOverviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //Declaracion y inicializacion de los componentes list item del UI
        fun cargar(
            receta: Receta, onClickListener: (Receta) -> Unit
        ) {
            with(binding) {
                ivReceta.setImageResource(receta.imageResourceId)
                tvNombreReceta.text = receta.nombre
                tvTiempoPrepReceta.text = receta.tiempo.toString()
                tvCaloriesReceta.text = receta.calorias.toString()

                itemView.setOnClickListener {onClickListener(receta) }
            }
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): RecipeOverviewCardViewHolder {
        //Crea un nuevo view
        val binding = ItemRecipeOverviewBinding.inflate(
           LayoutInflater.from(parent.context), parent, false
        )
        return RecipeOverviewCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeOverviewCardViewHolder, position: Int) {
        holder.cargar()

        //TODO: (after implementing el DataSource) val item = dataset[position]

        //holder.ivRecipe.setImageResource()                                        TODO: recipeList.imageResourceId
        //holder.tvRecipe?.text = ""                                                TODO: recipeList.nombre
        //holder.tvRecipeTiempoPrep?.text = "Tiempo de preparacion " + "" + "min."  TODO: recipeList.tiempo
        //holder.tvRecipeCalorias?.text = "Calorias " + "" + "kcal"                 TODO: recipeList.calorias
    }

    override fun getItemCount() = recipeList.size

}

*/