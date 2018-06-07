package com.hetic.cheers.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hetic.cheers.R
import com.hetic.cheers.model.Step
import kotlinx.android.synthetic.main.activity_steps.*

class StepFragment() : Fragment() {

    private lateinit var mItem: Step

    /**
     * Create new instance of Fragment and set params from activity
     */
    companion object {

        private const val STEP = "step"

        fun newInstance(step: Step): StepFragment {
            val args = Bundle()
            args.putSerializable(STEP,step)
            val fragment = StepFragment()
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
        val view = inflater.inflate(R.layout.fragment_step, container, false) // Layout

        mItem = arguments?.getSerializable(STEP) as Step //Retrieve Slide via Intent extra

        listener?.fragmentStepInit(mItem.image) //Set image to parent

        val text = view.findViewById<TextView>(R.id.step_text)
        text.text = mItem.description //Populate text

        val image = view.findViewById<ImageView>(R.id.step_image)
        Glide.with(this).load(mItem.image).into(image)

        return view
    }

    /**
     * Create listener to send data to activity
     */
    var listener: StepFragment.Listener? = null
    interface Listener { fun fragmentStepInit(image: String) }

    /**
     * Set listener onAttach
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) { listener = context }
        else { throw RuntimeException(context.toString() + " must implement Listener") }
    }

    /**
     * Remove listener onDetach
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
