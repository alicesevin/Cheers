package com.hetic.cheers.model

class Filter (
        price_rate_filter : Int,
        time_rate_filter : Int,
        difficulty_rate_filter : Int,
        global_rate_filter : Float
) {
    public val price_rate_filter = price_rate_filter
    public val time_rate_filter = time_rate_filter
    public val difficulty_rate_filter = difficulty_rate_filter
    public val global_rate_filter = global_rate_filter

    fun filter(list : ArrayList<Cocktail>) : ArrayList<Cocktail>{
        if(price_rate_filter != null){
            list.forEachIndexed{index, element ->
                println("index = $index, element = $element")
            }
        }
        return arrayListOf()
    }

    fun getString() : String {
        return "PRICE : " + price_rate_filter +
                ", DIFFICULTY : " + time_rate_filter +
                ", TIME : " + difficulty_rate_filter +
                ", GLOBAL_RATE : "+ global_rate_filter
    }
}
