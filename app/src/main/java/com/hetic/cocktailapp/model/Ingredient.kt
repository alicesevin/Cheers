package com.hetic.cheers.model

import java.io.Serializable

data class Ingredient(
        var id: Int,
        var name: String,
        var quantity: Int,
        var unity: String = "") : Serializable
{
    fun getLabel() : String{
        return "${this.quantity} ${this.unity}"
    }
}
