package com.hetic.cheers.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hetic.cheers.R
import com.hetic.cheers.adapter.IngredientAdapter
import com.hetic.cheers.adapter.TagCardAdapter
import com.hetic.cheers.model.CocktailDetail
import com.hetic.cheers.model.Rate
import kotlinx.android.synthetic.main.toolbar_detail.*
import java.io.Serializable

class DetailIntroFragment : Fragment() {

    private lateinit var mItem: CocktailDetail

    /**
     * Create new instance of Fragment and set params from activity
     */
    companion object {
        private const val COCKTAIL = "cocktail"

        fun newInstance(cocktail: CocktailDetail?): DetailIntroFragment {
            val args = Bundle()
            if(cocktail != null){ args.putSerializable(COCKTAIL,cocktail as Serializable) }
            val fragment = DetailIntroFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Init and populate view with data
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_intro, container, false)

        mItem = arguments?.getSerializable(COCKTAIL) as CocktailDetail
        if(mItem != null){
            loadContent(view)
            //Actions
            val start_button = view.findViewById<Button>(R.id.start_button)
            start_button.setOnClickListener{
                listener?.fragmentDetailIntroCallback(true)
            }
            if(mItem.steps.size == 0){ start_button.setText("Noter") }
        }
        return view
    }


    private fun loadContent(view : View){
        //TAGS
        val recyclerView = view.findViewById<RecyclerView>(R.id.tags);
        val adapter = TagCardAdapter(R.layout.tag_card_item_white){}
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(recyclerView?.context,  LinearLayoutManager.HORIZONTAL ,false)
        adapter.swapItems(mItem.getFilteredTags())
        adapter.notifyDataSetChanged()

        //INGREDIENTS
        val ingredientsRecyclerView = view.findViewById<RecyclerView>(R.id.ingredients);
        val ingredientsAdapter = IngredientAdapter {}
        ingredientsRecyclerView?.adapter = ingredientsAdapter
        ingredientsRecyclerView?.layoutManager = LinearLayoutManager(ingredientsRecyclerView?.context,  LinearLayoutManager.VERTICAL ,false)
        ingredientsAdapter.swapItems(mItem.ingredients ?: arrayListOf())
        ingredientsAdapter.notifyDataSetChanged()

        //Content
        val image = view.findViewById<ImageView>(R.id.image)
        Glide.with(this).load(mItem.image).into(image)

        view.findViewById<TextView>(R.id.name)?.text = mItem.name
        view.findViewById<TextView>(R.id.content)?.text = mItem.description

        //RATES

        val globalRate = Rate(mItem._global_rate,mItem.global_rate_votes)
        val speedRate = Rate(mItem.speed_rate,mItem.speed_rate_votes)
        val moneyRate = Rate(mItem._price_rate,mItem.price_rate_votes)
        val shakeRate = Rate(mItem.difficulty_rate,mItem.difficulty_rate_votes)

        //SIMPLE
        view.findViewById<RatingBar>(R.id.rating)?.rating = globalRate.getRating()
        view.findViewById<TextView>(R.id.rating_text)?.text = "%.1f".format(globalRate.getRating())

        //ADVANCED RATING
        val shakeRatingBar = view.findViewById<RatingBar>(R.id.rating_shake)
        val moneyRatingBar = view.findViewById<RatingBar>(R.id.rating_money)
        val timeRatingBar = view.findViewById<RatingBar>(R.id.rating_time)
        shakeRatingBar?.rating = shakeRate.getSimpleRating()
        moneyRatingBar?.rating = moneyRate.getSimpleRating()
        timeRatingBar?.rating = speedRate.getSimpleRating()

        //ADVANCED RATING TEXT
        val shakeRatingText = view.findViewById<TextView>(R.id.rating_shake_text)
        val moneyRatingText = view.findViewById<TextView>(R.id.rating_money_text)
        val timeRatingText = view.findViewById<TextView>(R.id.rating_time_text)
        shakeRatingText?.text = shakeRate.getLabelText("shake")
        moneyRatingText?.text = moneyRate.getLabelText("money")
        timeRatingText?.text = speedRate.getLabelText("time")
    }

    /**
     * Create listener to send data to activity
     */
    var listener: DetailIntroFragment.Listener? = null
    interface Listener { fun fragmentDetailIntroCallback(launchSteps: Boolean) }

    /**
     * Set listener onAttach
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement Listener")
        }
    }

    /**
     * Remove listener onDetach
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
