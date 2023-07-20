package com.kausthubh.basis.home.adapter.viewholder.CardViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kausthubh.basis.databinding.CardItemLayoutBinding


class CardViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

    val binding = CardItemLayoutBinding.bind(view)
    val author = binding.author
    val quote = binding.quotes
}