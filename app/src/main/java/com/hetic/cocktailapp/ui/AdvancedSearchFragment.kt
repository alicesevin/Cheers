package com.hetic.cheers.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hetic.cheers.R
import com.hetic.cheers.adapter.TagAdapter
import com.hetic.cheers.model.Slide
import com.hetic.cheers.model.Tag

class AdvancedSearchFragment : Fragment() {

    private lateinit var mItem: Slide

    /**
     * Create new instance of Fragment and set params from activity
     */
    companion object {
        private const val SLIDE = "slide"

        fun newInstance(slide: Slide): AdvancedSearchFragment {
            val args = Bundle()
            args.putSerializable(SLIDE,slide)
            val fragment = AdvancedSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Init and populate view with data
     * @param  it  Tag being toggled
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_advanced_search, container, false) // Layout

        mItem = arguments?.getSerializable(SLIDE) as Slide //Retrieve Slide via Intent extra

        val slideLabel = view.findViewById<TextView>(R.id.slideLabel)
        slideLabel.text = getSlideTitle(mItem.type) //Set Slide label

        val slideList = view.findViewById<RecyclerView>(R.id.slideList)
        val adapter = TagAdapter { toggleTags(it) } //Init Recycler View with Slide tags
        slideList.adapter = adapter
        slideList.layoutManager = GridLayoutManager(slideList.context, 2)
        adapter.swapItems(mItem.tags)
        adapter.notifyDataSetChanged()

        return view
    }

    /**
     * Add/Remove tags from slide ActivatedTags depending on recyclerView event then send it to activity
     * @param  it  Tag being toggled
     */
    private fun toggleTags(it : Tag){
        if(!mItem.activatedTags.contains(it.id)){
            mItem.activatedTags.add(it.id)
        }else{
            mItem.activatedTags.removeAt(mItem.activatedTags.indexOf(it.id))
        }
        listener?.fragmentCallback(mItem) //Should send data to activity
    }

    /**
     * Generate Slide Title
     * @param  type  Slide type
     */
    fun getSlideTitle(type : String) : String{
        val slideTitles = resources.getStringArray(R.array.slide_titles).asList()
        val tagsTypes = resources.getStringArray(R.array.tag_types).asList()
        return if(tagsTypes.contains(type)){ slideTitles[tagsTypes.indexOf(type)] } else ""
    }

    /**
     * Create listener to send data to activity
     */
    var listener: AdvancedSearchFragment.Listener? = null
    interface Listener { fun fragmentCallback(item: Slide) }

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
