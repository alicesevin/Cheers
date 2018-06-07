package com.hetic.cheers.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hetic.cheers.R
import com.hetic.cheers.model.Cocktail
import com.hetic.cheers.utils.inflate
import kotlinx.android.synthetic.main.cocktail_card_item.view.*

class ResultAdapter(
        val listener: (Cocktail) -> Unit) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var mItems: List<Cocktail> = emptyList()

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    fun swapItems(items: List<Cocktail>) { mItems = items }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.cocktail_card_item))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var mItem : Cocktail

        fun bind(item: Cocktail, listener: (Cocktail) -> Unit) = with(itemView) {
            mItem = item
            setContent()
            setOnClickListener { listener(item) }
        }

        private fun setContent() = with(itemView) {

            //IMAGE
            Glide.with(this).load(mItem.image).into(itemView.findViewById<ImageView>(R.id.image))

            //NAME
            name.text = mItem.name

            //TAGS
            val adapter = TagCardAdapter(R.layout.tag_card_item) {}
            tags.adapter = adapter
            tags.layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL ,
                    false)

            adapter.swapItems(mItem.getFilteredTags(3))
            adapter.notifyDataSetChanged()

            //RATES
            rating_text.text = "%.1f".format(mItem.getRating())
            rating_shake.rating = mItem.difficultyRate.toFloat()
            rating_money.rating = mItem.PriceRate.toFloat()
            rating_time.rating = mItem.speedRate.toFloat()

        }
    }
}

