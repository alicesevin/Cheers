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
import com.hetic.cheers.model.Filter
import com.hetic.cheers.model.Rate
import com.hetic.cheers.utils.inflate
import kotlinx.android.synthetic.main.cocktail_card_item.view.*

class ResultAdapter(val listener: (Cocktail) -> Unit) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var mItems: List<Cocktail> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.cocktail_card_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var mItem : Cocktail

        fun bind(item: Cocktail, listener: (Cocktail) -> Unit) = with(itemView) {
            mItem = item
            setContent()
            setOnClickListener { listener(item) }
        }

        private fun setContent() = with(itemView) {
            Glide.with(this).load(mItem.image).into(itemView.findViewById<ImageView>(R.id.image))
            name.text = mItem.name

            //TAGS
            val adapter = TagCardAdapter {}
            tags.adapter = adapter
            tags.layoutManager = LinearLayoutManager(itemView.context,  LinearLayoutManager.HORIZONTAL ,false)
            adapter.swapItems(mItem.getFilteredTags(3))
            adapter.notifyDataSetChanged()

            //RATES
            rating_text.text = "%.1f".format(mItem.getRating())
            rating_shake.rating = mItem.getRate("0","0").getRating()
            rating_money.rating = mItem.getRate("0","0").getRating()
            rating_time.rating = mItem.getRate("0","0").getRating()

        }
    }

    fun swapItems(items: List<Cocktail>) {
        mItems = items
    }
}

