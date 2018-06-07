package com.hetic.cheers.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.hetic.cheers.R
import com.hetic.cheers.model.Tag
import com.hetic.cheers.utils.inflate
import kotlinx.android.synthetic.main.cocktail_card_item.view.*

class TagCardAdapter(val listener: (Tag) -> Unit) : RecyclerView.Adapter<TagCardAdapter.ViewHolder>() {

    private var mItems: List<Tag> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.tag_card_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Tag, listener: (Tag) -> Unit) = with(itemView) {
            setContent(item)
            setOnClickListener { listener(item) }
        }

        private fun setContent(item : Tag) = with(itemView) {
            //val button = itemView.findViewById<ToggleButton>(R.id.name)
            name.text = item.name
            /*button.textOn = item.name
            button.textOff = item.name*/
        }
    }

    fun swapItems(items: List<Tag>) {
        mItems = items
    }
}
