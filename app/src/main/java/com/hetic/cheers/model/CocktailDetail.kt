package com.hetic.cheers.model

import java.io.Serializable

data class CocktailDetail  (
        val id: Int,
        val name: String,
        val description: String,
        val steps: ArrayList<Step> = arrayListOf(),
        val ingredients: ArrayList<Ingredient> = arrayListOf(),
        val image: String,
        val speed_rate: String,
        val speed_rate_votes: String,
        val difficulty_rate: String,
        val difficulty_rate_votes: String,
        val _price_rate: String,
        val price_rate_votes: String,
        var _global_rate: String,
        var global_rate_votes: String,
        val tags: ArrayList<Tag> = arrayListOf()) : Serializable {


    fun getFilteredTags() : List<Tag> {
        if(tags.size <= 0){ return arrayListOf() }
        val tags = tags.filter{ i -> i.type != "context"}
        return tags.subList(1,if(tags.size > 3){4}else{tags.size})
    }

    fun getRating(value : String,votes : String) : Float { return Rate(value,votes).getRating() }

}
