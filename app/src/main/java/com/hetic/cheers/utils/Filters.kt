package com.hetic.cheers.utils

import com.hetic.cheers.model.Cocktail

fun filterByPrice(list: List<Cocktail>,value : Int) : List<Cocktail>{
    var sortedList = list.sortedWith(compareBy({ it.GlobalRate }))
    return sortedList
}

fun filterByTime(list: List<Cocktail>,value : Int) : List<Cocktail>{
    var sortedList = list.sortedWith(compareBy({ it.GlobalRate }))
    return sortedList
}

fun filterByDifficulty(list: List<Cocktail>,value : Int) : List<Cocktail>{
    return list.filter{ i ->
        i.getRating().toInt() >= value
    }
}

fun filterByGlobalRate(list: ArrayList<Cocktail>,value : Int) : List<Cocktail>{
    return list.filter{ i -> i.getRating().toInt() >= value }
}
