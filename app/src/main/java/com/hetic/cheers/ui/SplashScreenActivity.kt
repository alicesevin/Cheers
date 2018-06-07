package com.hetic.cheers.ui

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hetic.cheers.R
import com.hetic.cheers.api.CocktailService
import com.hetic.cheers.api.RetrofitHelper
import com.hetic.cheers.model.Cocktail
import com.hetic.cheers.model.CocktailSource
import com.hetic.cheers.model.Tag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenActivity: AppCompatActivity() {

    private val TAG : String = "ERROR"

    private lateinit var mItemsService : CocktailService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mItemsService = RetrofitHelper.retrofit.create(CocktailService::class.java)
        getRecommandations()
        getData()
        removeIfissue()
    }

    private var DATAS : Int = 0
    private var mRecommandations : List<Cocktail> = arrayListOf()
    private fun getRecommandations(){
        val items = mItemsService.getCocktails("","","","","","")
        items.enqueue(object : Callback<ArrayList<Cocktail>> {
            override fun onResponse(call: Call<ArrayList<Cocktail>>?, response: Response<ArrayList<Cocktail>>?) {
                response?.body()?.let { source ->
                    mRecommandations = source
                    DATAS += 1
                    if(DATAS == 2){ scheduleSplashScreen() }
                }
            }

            override fun onFailure(call: Call<ArrayList<Cocktail>>?, t: Throwable?) { Log.d(TAG, "Unables : "+t.toString()) }
        })
    }

    private var mTags : List<Tag> = arrayListOf()
    private var mCocktails : List<Cocktail> = arrayListOf()
    private fun getData(){
        val items = mItemsService.getAllCocktails("","","","","","1")
        items.enqueue(object : Callback<CocktailSource> {
            override fun onResponse(call: Call<CocktailSource>?, response: Response<CocktailSource>?) {
                response?.body()?.let { source ->
                    mTags = source.tags
                    mCocktails = source.cocktails
                    DATAS += 1
                    if(DATAS == 2){ scheduleSplashScreen() }
                }
            }

            override fun onFailure(call: Call<CocktailSource>?, t: Throwable?) { Log.d(TAG, "Unables : "+t.toString()) }
        })
    }
    private fun getSplashScreenDuration() = 2000L
    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed({launchApp()}, splashScreenDuration)
    }

    private fun getIssueDuration() = 5000L
    private fun removeIfissue() {
        val getIssueDuration = getIssueDuration()
        Handler().postDelayed({finish()}, getIssueDuration)
    }

    fun launchApp(){
        val intent = MainActivity.getFirstIntent(this,mTags,mCocktails,mRecommandations)
        startActivity(intent)
        finish()
    }

}
