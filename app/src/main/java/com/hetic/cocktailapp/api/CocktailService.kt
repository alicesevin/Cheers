package com.hetic.cheers.api
import com.hetic.cheers.model.Cocktail
import com.hetic.cheers.model.CocktailDetail
import com.hetic.cheers.model.CocktailSource
import retrofit2.Call
import retrofit2.http.*

interface CocktailService {

    @GET("cocktail/{id}")
    fun getCocktail(
            @Path("id") id: Int): Call<CocktailDetail>

    @POST("tags")
    @FormUrlEncoded
    fun getAllCocktails(
            @Field("ingredients") ingredients : String,
            @Field("context") context : String,
            @Field("caracteristique") caracteristique : String,
            @Field("alcool") alcool : String,
            @Field("search") search : String,
            @Field("get_tags") get_tags : String) : Call<CocktailSource>


    @POST("tags")
    @FormUrlEncoded
    fun getCocktails(
            @Field("ingredients") ingredients : String,
            @Field("context") context : String,
            @Field("caracteristique") caracteristique : String,
            @Field("alcool") alcool : String,
            @Field("search") search : String,
            @Field("get_tags") get_tags : String) : Call<ArrayList<Cocktail>>

    //RATE
    @POST("cocktail/{id}/rate")
    @FormUrlEncoded
    fun setRate(
            @Path("id") id: String,
            @Field("rate") context : String) : Call<CocktailDetail>

}
