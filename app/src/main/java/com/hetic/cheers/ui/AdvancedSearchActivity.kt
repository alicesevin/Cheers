package com.hetic.cheers.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.hetic.cheers.R
import com.hetic.cheers.model.Slide
import com.hetic.cheers.model.Tag
import kotlinx.android.synthetic.main.activity_advanced_search.*
import java.util.ArrayList
import android.support.v4.view.ViewPager.OnPageChangeListener
import java.io.Serializable
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.LinearLayout
import com.hetic.cheers.model.Tags


class AdvancedSearchActivity : AppCompatActivity(), AdvancedSearchFragment.Listener {

    private var mSections = ArrayList<Slide>()

    /**
     * GET INTENT WITH PREDEFINED PARAMS FROM OTHER ACTIVITY
     */
    companion object {
        val ITEMS = "items"

        @JvmStatic
        fun getIntent(context: Context, items: List<Tag>): Intent {
            val intent = Intent(context, AdvancedSearchActivity::class.java)
            intent.putExtra(ITEMS, items as Serializable)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_search) //Layout
        populateView()
    }

    /**
     * Populate view with datas
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private fun populateView(){
        //Create Slides data
        val items = intent.getSerializableExtra(ITEMS) as ArrayList<Tag>
        mSections = createTagGroups(items)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager) //Init page adapter to swipe between Slides
        container.adapter = mSectionsPagerAdapter

        //Navigations
        initPreviousButton()
        initNextButton()
        initFragmentSwipeListener()
        initDots()

    }

    /**
     * Generate Dots buttons
     */
    private fun initDots(){
        for(indice in mSections.size downTo 1 step 1){
            val dot = Button(this)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.rightMargin = resources.getDimensionPixelSize(R.dimen.padding_xs)
            lp.leftMargin = resources.getDimensionPixelSize(R.dimen.padding_xs)
            dot.layoutParams = lp
            dot.setBackgroundResource(R.drawable.dot)
            dot.setPadding(0,0,0,0)
            dot.height = resources.getDimensionPixelSize(R.dimen.dot)
            dot.width = resources.getDimensionPixelSize(R.dimen.dot)
            dot.minHeight = resources.getDimensionPixelSize(R.dimen.dot)
            dot.minWidth = resources.getDimensionPixelSize(R.dimen.dot)
            dot.setOnClickListener{
                container.currentItem = indice - 1
                setButtonText(indice - 1)
            }
            slide_dots.addView(dot,0)
        }
    }

    /**
     * Map datas to create TagGroups
     */
    private fun createTagGroups(items : ArrayList<Tag>) : ArrayList<Slide>{
        val groups = items.groupBy{ it -> it.type }
        val slideGroups = arrayListOf<Slide>()
        var i = 1
        for(entry in groups){
            slideGroups.add(Slide(i,entry.key,entry.value))
            i++
        }
        return slideGroups
    }

    /**
     * Back Navigation action depending on Slides position
     */
    private fun initPreviousButton(){
        previous_button.setOnClickListener{
            if(container.currentItem == 0){
                super.onBackPressed();
            }else{
                container.currentItem = container.currentItem - 1;
            }
            setButtonText(container.currentItem)
        }
    }

    /**
     * Next Navigation action depending on Slides position
     */
    private fun initNextButton(){
        back_button.setOnClickListener{
            if(container.currentItem < mSections.size - 1){
                container.currentItem = container.currentItem + 1;
            }else{
                val tags = Tags(arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
                mSections.map { i -> tags.setActivated(i.type,i.activatedTags) }
                val resultsIntent = ResultsActivity.getIntent(this,tags)
                startActivity(resultsIntent)
            }
        }
    }

    /**
     * Events during fragment swipe
     */
    private fun initFragmentSwipeListener(){
        container.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) { setButtonText(position) }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * Set button title depending on Slide index
     */
    private fun setButtonText(position : Int){
        previous_button.text = resources.getString(if(position == 0){R.string.destroy}else{R.string.back})
        back_button.text = resources.getString(if(position == (mSections.size - 1)){R.string.valid_text}else{R.string.next})
    }

    /**
     * Get data from fragment
     * @param  item  the Fragment get
     */
    override fun fragmentCallback(item: Slide) {
        mSections.find{ i -> item.id == i.id}?.activatedTags = item.activatedTags
    }

    //Page Adapter to swipe between Fragments
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return AdvancedSearchFragment.newInstance(mSections[position])
        }
        override fun getCount(): Int { return mSections.size }
    }
}
