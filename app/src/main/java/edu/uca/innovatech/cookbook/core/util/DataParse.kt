package edu.uca.innovatech.cookbook.core.util

import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import java.math.RoundingMode
import java.text.DecimalFormat

fun parseTiempoPrep(tiempoPrep: Int): String{
    if (tiempoPrep.equals(0)){
        return "Tiempo estimado: Pendiente"
    } else if (tiempoPrep > 60){
        val tiempoPrepH: Double = ((tiempoPrep).toDouble())/60
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING

        return "Tiempo estimado: ${df.format(tiempoPrepH).toDouble()} h"
    }
    return "Tiempo estimado: $tiempoPrep m"
}

fun parseCalorias(kcal: Int): String {
    if (kcal.equals(0))
        return "Calorias estimadas: Pendiente"

    return "Calorias estimadas: $kcal kcal"
}

fun parseIngredientes(ingredientes: List<Ingrediente>): String {
    var ingredientesFormatted: String = ""

    for (ing in ingredientes) {
        if (ing.medidaIngrediente.equals("Al gusto")) {
            ingredientesFormatted += """
                ${ing.nombreIngrediente} - ${ing.medidaIngrediente}
                
                """.trimIndent()
        } else {
            ingredientesFormatted += """
                ${ing.nombreIngrediente} - ${ing.cantIngrediente} ${ing.medidaIngrediente}
                
                """.trimIndent()
        }
    }
    return ingredientesFormatted
}

fun parseCantIngredientes(cantIngrediente: Int, medidaIngrediente: String): String {
    if (medidaIngrediente.equals("Al gusto")) {
        return medidaIngrediente
    }
    return "$cantIngrediente - $medidaIngrediente"
}

fun parseNumPaso(numPaso: Int): String {
    if (numPaso == 0) return "Preparación de Ingredientes:"
    return "Paso $numPaso:"
}

fun parsePasoTODO(numPaso: Int): String {
    if (numPaso == 0) return "Ingredientes:"
    return "Paso a hacer:"
}

fun parsePasoTip(paso: Int): String{
    if (paso.equals(0)) return "Presione para comenzar a cocinar!"
    return "Presione para seguir cocinando!"
}