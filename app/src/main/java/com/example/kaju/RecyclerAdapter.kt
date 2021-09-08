package com.example.kaju

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var title = arrayOf("KAHVALTI", "ARA ÖĞÜN", "ÖĞLE", "ARA ÖĞÜN", "AKŞAM", "ARA ÖĞÜN")

    private val images = intArrayOf(R.mipmap.eggfried,R.mipmap.fruit,R.mipmap.lunch,R.mipmap.fruit,R.mipmap.servingdish, R.mipmap.fruit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val cl = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(cl)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemImage.setImageResource(images[position])
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var itemImage: ImageView
        var itemTitle: TextView

        init {
            itemImage = itemView.findViewById(R.id.cardImage)
            itemTitle = itemView.findViewById(R.id.cardText)
        }
    }
}