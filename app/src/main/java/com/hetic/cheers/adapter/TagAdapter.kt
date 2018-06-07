package com.hetic.cheers.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.hetic.cheers.R
import com.hetic.cheers.model.Tag
import com.hetic.cheers.utils.inflate
import kotlinx.android.synthetic.main.tag_item.*
import kotlinx.android.synthetic.main.tag_item.view.*

class TagAdapter(val listener: (Tag) -> Unit) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.tag_item))

    private var mItems: List<Tag> = emptyList()

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(mItems[position], listener)

    override fun getItemCount() = mItems.size

    fun swapItems(items: List<Tag>) { mItems = items }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Populate view with datas
         */
        fun bind(item: Tag, listener: (Tag) -> Unit) = with(itemView) {
            name.text = item.name
            name.textOn = item.name
            name.textOff = item.name
            name.setOnCheckedChangeListener { _, isChecked -> listener(item) } //Should send item to fragment on checkbox event
        }
    }
}
