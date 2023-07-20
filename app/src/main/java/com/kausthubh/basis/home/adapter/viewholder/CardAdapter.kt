package com.kausthubh.basis.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kausthubh.basis.R
import com.kausthubh.basis.home.adapter.viewholder.CardViewHolder.CardViewHolder
import com.kausthubh.basis.home.adapter.viewholder.CardViewHolder.MyData

class CardAdapter : PagingDataAdapter<MyData, CardViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MyData>() {
            override fun areItemsTheSame(oldItem: MyData, newItem: MyData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: MyData, newItem: MyData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_item_layout, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CardViewHolder, position: Int) {
        val item = getItem(position)
        with(viewHolder) {
            author.text = item?.subText.orEmpty()
            quote.text = item?.title.orEmpty()
            quote.isVisible = !(item?.split ?: false)
        }
    }

    fun getMyData(position: Int) = getItem(position)
}
