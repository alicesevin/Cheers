package com.hetic.cheers.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hetic.cheers.R
import com.hetic.cheers.api.CocktailService
import com.hetic.cheers.api.RetrofitHelper
import com.hetic.cheers.model.*
import com.hetic.cheers.utils.displayMessage
import com.hetic.cheers.utils.loadFragment
import com.hetic.cheers.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class DetailActivity : AppCompatActivity(),
        DetailIntroFragment.Listener,
        DetailEndFragment.Listener{

    private val TAG: String = "ITEM"
    private lateinit var mItem : CocktailDetail
    private var IS_RATED : Boolean = false

    companion object {
        val COCKTAIL_KEY = "cocktail_key"
        val END = "show_end_fragment"

        @JvmStatic
        fun getIntent(context: Context, id: Int?): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(COCKTAIL_KEY, id)
            return intent
        }

        @JvmStatic
        fun getStepIntent(context: Context, showEnd : Boolean?): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(END,showEnd)
            return intent
        }
    }


    private fun loadById(id: Int) {
        val itemsService = RetrofitHelper.retrofit.create(CocktailService::class.java)
        val item = itemsService.getCocktail(id)
        item.enqueue(object : Callback<CocktailDetail> {
            override fun onResponse(call: Call<CocktailDetail>?, response: Response<CocktailDetail>?) {
                Log.d("Cocktail",response.toString())
                response?.body()?.let { source ->
                    mItem = source
                    loadFragment(supportFragmentManager, DetailIntroFragment.newInstance(mItem), R.id.fragments)
                }
            }

            override fun onFailure(call: Call<CocktailDetail>?, t: Throwable?) { Log.d(TAG, "Unable") }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getIntExtra(COCKTAIL_KEY, 0)
        loadById(id)
        back_button.setOnClickListener{ this.onBackPressed() }
    }

    private fun goToHome(){
        val intent = MainActivity.getIntent(this)
        startActivity(intent)
        finish()
    }

    private fun goToSteps(){
        val intent = StepsActivity.getIntent(this,mItem)
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val root = data?.getBooleanExtra(END,false)
        Log.d("GO END ?",root.toString())
        getFragment(root)
    }

    private fun getFragment(root : Boolean?){
        if(root == true){
            replaceFragment(supportFragmentManager, DetailEndFragment.newInstance(mItem,IS_RATED), R.id.fragments)
        }else{
            replaceFragment(supportFragmentManager, DetailIntroFragment.newInstance(mItem), R.id.fragments)
        }
    }


    private fun setRate(root : String, rate : Float){
        if(IS_RATED || rate == 0F) {  setEndRoot(root) }else {
            val service = RetrofitHelper.retrofit.create(CocktailService::class.java)
            val rateService = service.setRate(mItem.id.toString(), rate.toString())
            rateService.enqueue(object : Callback<CocktailDetail> {
                override fun onResponse(call: Call<CocktailDetail>?, response: Response<CocktailDetail>?) {
                    Log.d("RATE", response.toString())
                    response?.body()?.let { source ->
                        displayMessage("Votre note a été prise en compte", this@DetailActivity)
                        mItem = source
                        IS_RATED = true
                        setEndRoot(root)
                    }
                }

                override fun onFailure(call: Call<CocktailDetail>?, t: Throwable?) {
                    displayMessage("Une erreur s'est produite, merci de réessayer.", this@DetailActivity)
                }
            })
        }
    }

    private fun setEndRoot(destination: String){
        if (destination == "intro") { replaceFragment(supportFragmentManager,DetailIntroFragment.newInstance(mItem),R.id.fragments) }
        else if (destination == "steps") { goToSteps() }
        else { goToHome() }
    }

    override fun fragmentDetailIntroCallback(launchSteps: Boolean) { goToSteps() }
    override fun fragmentDetailEndCallback(destination: String, value : Float?) { setRate(destination,value ?: 0F) }

}
