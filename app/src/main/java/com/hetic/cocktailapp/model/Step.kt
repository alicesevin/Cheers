package com.hetic.cheers.model

import java.io.Serializable

data class Step (
        val id : Int,
        val name : String,
        val description : String,
        val url : String,
        val image : String,
        val ingredients : ArrayList<Ingredient> = arrayListOf(),
        val n_step : String
) : Serializable {
}
