package com.hetic.cheers.model

class Rate (
        rate : String,
        nbRate : String
) {

    var rate : Float = 0F
    var nbRate : Float = 0F

    init{
        this.rate = if (rate != null) rate.toFloat() else 0F
        this.nbRate = if (nbRate != null) nbRate.toFloat() else 0F
    }

    val moneyLabels : ArrayList<String> = arrayListOf("Honereux","Abordable","Moyen")
    val shakeLabels : ArrayList<String> = arrayListOf("Lent","Rapide","Moyen")
    val timeLabels : ArrayList<String> = arrayListOf("Difficile","Facile","Moyen")

    fun getLabelText(type : String) : String {
            if(this.getSimpleRating() == 0F){
                return "NC";
            }
            var labels = timeLabels
            if(type == "money"){
                labels = moneyLabels
            }else if(type == "shake"){
                labels = shakeLabels
            }
            var label = labels[0]
            if (this.getSimpleRating() <= 1) {
                label = labels[1]
            } else if (this.getSimpleRating() <= 2.5) {
                label = labels[2]
            }
        return label;

    }

    fun getRating() : Float{
        return if (this.nbRate == 0F) 0F else this.rate/this.nbRate
    }


    fun getSimpleRating() : Float{
        return this.rate
    }

    fun getRatingString() : String{
        return this.getRating().toString()
    }
}
