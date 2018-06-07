package com.hetic.cheers.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.hetic.cheers.R
import com.hetic.cheers.model.CocktailDetail
import com.hetic.cheers.model.Step
import kotlinx.android.synthetic.main.activity_steps.*
import java.io.Serializable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.view.WindowManager
import java.util.*

class StepsActivity() : AppCompatActivity(), StepFragment.Listener, Parcelable {

    private lateinit var mItem: CocktailDetail
    private lateinit var mSteps : List<Step>
    private var timeStamp: Long = Date().getTime();
    private var oldSensorValue: Float = 0.0f
    private var isLocked : Boolean = false
    internal lateinit var mySensorManager: SensorManager
    internal var myProximitySensor: Sensor? = null
    private var currentRunnable: Runnable? = null
    var currentTimeStamp = 0L
    var currentTimeStamp2 = 0L

    /**
     * Init and populate view with data
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)
        mItem = intent.getSerializableExtra(COCKTAIL) as CocktailDetail
        mSteps = mItem.steps.sortedBy{ it.n_step.toInt() }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager) //Init page adapter to swipe between Slides
        steps_fragments.adapter = mSectionsPagerAdapter


        previous_step.setOnClickListener {goPrevious()}
        next_step.setOnClickListener {goNext()}
        initSensor()
        initLockButton()
        back_button.setOnClickListener{ this.onBackPressed() }
    }

    private fun initSensor(){
        mySensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (myProximitySensor == null) {}
        else {
            mySensorManager.registerListener(
                    proximitySensorEventListener,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun goNext(){
        if (steps_fragments.currentItem < mSteps.size - 1) {
            steps_fragments.currentItem = steps_fragments.currentItem + 1
        } else {
            goToDetail(true)
        }
        progress(steps_fragments.currentItem)

    }

    private fun goPrevious(){
        if (steps_fragments.currentItem == 0) { goToDetail(false) }
        else { steps_fragments.currentItem = steps_fragments.currentItem - 1 }
        progress(steps_fragments.currentItem)
    }


    private fun initLockButton(){
        isLocked != isLocked
        lock_button.setOnClickListener{
            if(isLocked){
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }else{
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    private fun progress(position : Int){
        val progress = (position.toFloat() / mSteps.size.toFloat() * 100)

        steps_progress.progress = progress.toInt()
        steps_progress_text.text = "%.0f".format(progress) + " %"

        var textColor = R.color.grayDark
        if(progress.toInt() > 50){ textColor = R.color.grayLighter }
        steps_progress_text.setTextColor(resources.getColor(textColor))
    }

    /**
     * Init detailIntent with fragment to show depending on Slide
     */
    private fun goToDetail(showEnd : Boolean?){
        mySensorManager.unregisterListener(proximitySensorEventListener)
        val detailActivity = DetailActivity.getStepIntent(this,showEnd)
        setResult(1000, detailActivity)
        finish()
    }

    /**
     * Init images by callback
     */
    override fun fragmentStepInit(image: String) {
        progress(steps_fragments.currentItem)
    }

    //Page Adapter to swipe between Fragments
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment { return StepFragment.newInstance(mSteps[position]) }
        override fun getCount(): Int { return mSteps.size }
    }

    //SENSOR

    internal var proximitySensorEventListener: SensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                Log.d("Event",event.values[0].toString())
                if (event.values[0] <= 10 && event.values[0] != oldSensorValue.toFloat()) {

                    if (event.values[0] != 0.0f) { oldSensorValue = event.values[0] }
                    if (currentTimeStamp != 0L) { currentTimeStamp2 = Date().time }

                    currentTimeStamp = Date().time


                    if(currentRunnable ===  null) {
                        currentRunnable = Runnable {
                            runOnUiThread {
                                if ((currentTimeStamp2 - currentTimeStamp) <= 1000 && currentTimeStamp2 != 0L) {
                                    goPrevious()
                                } else {
                                    goNext()
                                }

                                currentRunnable = null
                                currentTimeStamp = 0L
                                currentTimeStamp2 = 0L
                            }
                        }
                        Handler().postDelayed(currentRunnable, 1500)
                    }
                }

            }
        }

    }

    constructor(parcel: Parcel) : this() {
        timeStamp = parcel.readLong()
        oldSensorValue = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timeStamp)
        parcel.writeFloat(oldSensorValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StepsActivity> {

        private const val COCKTAIL = "cocktail"

        fun getIntent(context: Context, cocktail : CocktailDetail): Intent {
            val intent = Intent(context, StepsActivity::class.java)
            intent.putExtra(COCKTAIL, cocktail as Serializable)
            return intent
        }
        override fun createFromParcel(parcel: Parcel): StepsActivity {
            return StepsActivity(parcel)
        }

        override fun newArray(size: Int): Array<StepsActivity?> {
            return arrayOfNulls(size)
        }
    }
}
