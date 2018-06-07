package com.hetic.cheers.model

import java.io.Serializable

class Tags(
         var alcool: MutableList<Int>?,
         var caracteristique: MutableList<Int>?,
         var context: MutableList<Int>?,
         var ingredients: MutableList<Int>?
) : Serializable {
    fun setActivated(type : String,value : MutableList<Int>){
        when(type){
            "alcool" -> this.alcool = if(value.size > 0) value else null
            "caracteristique" -> this.caracteristique = if(value.size > 0) value else null
            "context" -> this.context = if(value.size > 0) value else null
            "ingredients" -> this.ingredients = if(value.size > 0) value else null
        }
    }

    fun toString(value : MutableList<Int>?) : String{
        if(value == null || value.size == 0){ return "" }
        return value.joinToString(",")
    }
}
