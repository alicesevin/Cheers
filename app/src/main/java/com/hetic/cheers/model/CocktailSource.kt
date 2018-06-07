package com.hetic.cheers.model

import java.io.Serializable

data class CocktailSource(
        val cocktails : List<Cocktail>,
        val tags : List<Tag>
) : Serializable
