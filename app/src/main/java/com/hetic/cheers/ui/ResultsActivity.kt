package com.hetic.cheers.ui

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.hetic.cheers.R
import com.hetic.cheers.api.CocktailService
import com.hetic.cheers.api.RetrofitHelper
import com.hetic.cheers.model.Cocktail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.transition.TransitionManager
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.bumptech.glide.Glide
import com.hetic.cheers.adapter.ResultAdapter
import com.hetic.cheers.model.Filter
import com.hetic.cheers.model.Tags
import kotlinx.android.synthetic.main.filters.*
import kotlinx.android.synthetic.main.activity_results.*
import java.io.Serializable


class ResultsActivity() : Activity() {

    private val TAG : String = "RESULTS"

    //INIT RECYCLER VIEW WITH CHOSEN ITEM LAYOUT AND ORIENTATION FOR TAGS
    private val mOrientation : Int = LinearLayoutManager.VERTICAL
    private lateinit var mAdapter : ResultAdapter

    companion object {
        val TAGS = "Tags"

        @JvmStatic
        fun getIntent(context: Context, tags: Tags): Intent {
            val intent = Intent(context, ResultsActivity::class.java)
            intent.putExtra(TAGS, tags as Serializable)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        //initRecyclerView
        mAdapter = ResultAdapter {
            val detailIntent = DetailActivity.getIntent(this,it.id)
            startActivity(detailIntent)
        }
        initList()

        //getDatas
        var mItems : List<Cocktail> = listOf()
        val tags = intent.getSerializableExtra(TAGS) as Tags
        val service = RetrofitHelper.retrofit.create(CocktailService::class.java)
        val data = service.getCocktails(
                tags.toString(tags.ingredients),
                tags.toString(tags.context),
                tags.toString(tags.caracteristique),
                tags.toString(tags.alcool),"","")

        data.enqueue(object : Callback<ArrayList<Cocktail>> {
            override fun onResponse(call: Call<ArrayList<Cocktail>>?, response: Response<ArrayList<Cocktail>>?) {
                response?.body()?.let { source ->
                    mAdapter.swapItems(source)
                    mAdapter.notifyDataSetChanged()
                    mItems = source
                    removeLoader()
                    toggleEmpty(mItems)
                }
            }

            override fun onFailure(call: Call<ArrayList<Cocktail>>?, t: Throwable?) { Log.d(TAG, "Unables : "+t.toString()) }
        })

        //Filters
        var isVisible : Boolean = false

        show_filters.setOnClickListener {
            TransitionManager.beginDelayedTransition(mainLayout)
            isVisible = !isVisible
            filters.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        valid_filters.setOnClickListener {
            TransitionManager.beginDelayedTransition(mainLayout)
            if(isVisible){
                isVisible = !isVisible
                filters.visibility = View.GONE

                //Filter recycler view with RadioGroup values
                val filter = Filter(
                        getRadioVal(time_filter) ,
                        getRadioVal(price_filter)  ,
                        getRadioVal(difficulty_filter),
                        global_rate_filter.rating)
                filterItems(mItems, filter)
            }
        }
        back_button.setOnClickListener{ this.onBackPressed() }
        search_button.setOnClickListener{ this.onBackPressed() }

    }

    //init RecyclerView
    fun initList(){
        cocktail_list.adapter = mAdapter
        cocktail_list.layoutManager = LinearLayoutManager(this, mOrientation,false)
    }

    //Retrieve RadioGroup value
    fun getRadioVal(ele : RadioGroup) : Int {
        val active = ele.getCheckedRadioButtonId()
        val btn = ele.findViewById<RadioButton>(active)
        return ele.indexOfChild(btn) + 1
    }

    private fun getLoaderDuration() = 2000L
    fun removeLoader(){
        val loaderDuration = getLoaderDuration()
        Handler().postDelayed({ loader.visibility = View.GONE }, loaderDuration)
    }

    fun toggleEmpty(items : List<Any>){ empty.visibility = if(items.size == 0){ View.VISIBLE }else{ View.GONE } }

    fun filterItems(mItems : List<Cocktail>, filter : Filter){
        loader.visibility = View.VISIBLE
        Log.d("Filtre",filter.getString())

        //Filter by Rate
        var filteredList =  mItems
        if(filter.global_rate_filter.toInt() > 0){
            filteredList = filteredList.filter { i -> i.getRating().toInt() == filter.global_rate_filter.toInt() }
        }
        //Filter by time
        if(filter.time_rate_filter > 0) {
            filteredList = filteredList.filter { i -> i.speedRate == filter.time_rate_filter }
        }
        //Filter by difficulty
        if(filter.difficulty_rate_filter > 0) {
            filteredList = filteredList.filter { i -> i.difficultyRate == filter.difficulty_rate_filter }
        }
        //Filter by price
        if(filter.price_rate_filter > 0) {
            filteredList = filteredList.filter { i -> i.PriceRate == filter.price_rate_filter }
        }
        Log.d("ITEMS FILTERED ("+ filteredList.size + ") : ",filteredList.toString())
        Log.d("ITEMS ("+ mItems.size + ") : ",mItems.toString())

        mAdapter.swapItems(filteredList)
        mAdapter.notifyDataSetChanged()
        removeLoader()
        toggleEmpty(filteredList)
    }
}
