package com.hetic.cheers.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hetic.cheers.model.Tag
import com.hetic.cheers.utils.inflate
import kotlinx.android.synthetic.main.cocktail_card_item.view.*

class TagCardAdapter(private val mTemplate : Int,val listener: (Tag) -> Unit) : RecyclerView.Adapter<TagCardAdapter.ViewHolder>() {

    private var mItems: List<Tag> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(mTemplate))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    fun swapItems(items: List<Tag>) { mItems = items }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Tag, listener: (Tag) -> Unit) = with(itemView) {
            name.text = item.name
            setOnClickListener { listener(item) }
        }
    }
}
