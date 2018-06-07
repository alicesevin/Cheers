package com.hetic.cheers.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.hetic.cheers.R
import com.hetic.cheers.model.CocktailDetail

class DetailEndFragment : Fragment() {

    private lateinit var mItem: CocktailDetail
    private var IS_RATED : Boolean = false

    /**
     * Create new instance of Fragment and set params from activity
     */
    companion object {
        private const val COCKTAIL = "cocktail"
        private const val RATED = "rated"

        fun newInstance(cocktail: CocktailDetail?, rated: Boolean): DetailEndFragment {
            val args = Bundle()
            args.putSerializable(COCKTAIL,cocktail)
            args.putBoolean(RATED,rated)
            val fragment = DetailEndFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Init and populate view with data
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_end, container, false) // Layout

        mItem = arguments?.getSerializable(COCKTAIL) as CocktailDetail //Retrieve Slide via Intent extra
        IS_RATED = arguments?.getBoolean(RATED) ?: false

        val image = view.findViewById<ImageView>(R.id.image)
        Glide.with(this).load(mItem.image).into(image) //IMAGE

        val name = view.findViewById<TextView>(R.id.name) //NAME
        name.text = mItem.name

        val ratingBar = view.findViewById<RatingBar>(R.id.rating_cocktail) //RATING
        if(IS_RATED){
            ratingBar.setIsIndicator(true)
            ratingBar.rating = mItem.getRating(mItem._global_rate,mItem.global_rate_votes)
            val ratingText = view.findViewById<TextView>(R.id.rating_text)
            ratingText.text = resources.getString(R.string.voted)
        }

        //BUTTONS
        val introBtn = view.findViewById<Button>(R.id.go_detail_btn)
        introBtn.setOnClickListener{ listener?.fragmentDetailEndCallback("intro",ratingBar.rating)}

        val homeBtn = view.findViewById<Button>(R.id.go_home_btn)
        homeBtn.setOnClickListener{ listener?.fragmentDetailEndCallback("home",ratingBar.rating)}

        val stepsBtn = view.findViewById<Button>(R.id.restart_btn)
        stepsBtn.setOnClickListener{ listener?.fragmentDetailEndCallback("steps",ratingBar.rating)}
        if(mItem.steps.size == 0){ stepsBtn.setVisibility(View.GONE) }
        return view
    }

    /**
     * Create listener to send data to activity
     */
    var listener: DetailEndFragment.Listener? = null
    interface Listener { fun fragmentDetailEndCallback(destination: String, value : Float?) }

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
