package com.hetic.cheers.ui

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
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
        Glide.with(this).load(R.mipmap.loader).into(loader)
        loader.visibility = View.VISIBLE

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
                    if(source != null){
                        mAdapter.swapItems(source)
                        mAdapter.notifyDataSetChanged()
                        mItems = source
                        loader.visibility = View.GONE
                        Log.d("Results",source.toString())
                        Log.d("Tags",tags.alcool.toString())
                    }
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
                        getRadioVal(time_filter),
                        getRadioVal(price_filter),
                        getRadioVal(difficulty_filter),
                        global_rate_filter.rating)
                filterItems(mItems, filter)
            }
        }

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
        return ele.indexOfChild(btn)
    }

    fun filterItems(mItems : List<Cocktail>, filter : Filter){
        loader.visibility = View.VISIBLE
        Log.d("Filtre",filter.getString())

        //Filter by Rate
        var filteredList = mItems.filter { i -> i.getRating().toInt() == filter.global_rate_filter.toInt() }
        //Filter by time
        filteredList = filteredList.filter { i -> i.getTimeRating().toInt() ==  filter.time_rate_filter }
        //Filter by difficulty
        filteredList = filteredList.filter { i -> i.getDifficultyRating().toInt() ==  filter.difficulty_rate_filter }
        //Filter by price
        filteredList = filteredList.filter { i -> i.getPriceRating().toInt() ==  filter.price_rate_filter }

        Log.d("ITEMS FILTERED ("+ filteredList.size + ") : ",filteredList.toString())

        mAdapter.swapItems(filteredList)
        mAdapter.notifyDataSetChanged()
        if(filteredList.size != 0){
            loader.visibility = View.GONE
        }
    }
}
