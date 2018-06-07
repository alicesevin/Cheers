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


class CocktailCardAdapter(val listener: (Cocktail) -> Unit) : RecyclerView.Adapter<CocktailCardAdapter.ViewHolder>() {

    private var mItems: ArrayList<Cocktail> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.cocktail_card_item_horizontal))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    fun swapItems(items: ArrayList<Cocktail>) { mItems = items }

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
            val adapter = TagCardAdapter(R.layout.tag_card_item) {}
            tags.adapter = adapter
            tags.layoutManager = LinearLayoutManager(itemView.context,  LinearLayoutManager.HORIZONTAL ,false)
            adapter.swapItems(mItem.getFilteredTags(4))
            adapter.notifyDataSetChanged()

            //RATES
            rating_text.text = "%.1f".format(mItem.getRating())
            rating.rating = mItem.getRating()
        }
    }
}
