package com.example.kaju

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class WaterRecyclerAdapter: RecyclerView.Adapter<WaterRecyclerAdapter.ViewHolder>() {

    private val emptyGlassImages = intArrayOf(R.mipmap.emptyglass,R.mipmap.emptyglass,R.mipmap.emptyglass,R.mipmap.emptyglass)
    private val fullGlassImages = intArrayOf(R.mipmap.fullglass,R.mipmap.fullglass,R.mipmap.fullglass,R.mipmap.fullglass)
    private val greenTicks = intArrayOf(R.mipmap.greentick,R.mipmap.greentick,R.mipmap.greentick,R.mipmap.greentick)
    private val plusImageButtons = intArrayOf(R.mipmap.plusbutton,R.mipmap.plusbutton,R.mipmap.plusbutton,R.mipmap.plusbutton)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterRecyclerAdapter.ViewHolder {
        val layoutInf = LayoutInflater.from(parent.context).inflate(R.layout.watercard_layout,parent,false)
        return ViewHolder(layoutInf)
    }

    override fun getItemCount(): Int {
        return emptyGlassImages.size
    }

    override fun onBindViewHolder(holder: WaterRecyclerAdapter.ViewHolder, position: Int) {
        holder.imageEmptyGlass.setImageResource(emptyGlassImages[position])
        holder.imageFullGlass.setImageResource(fullGlassImages[position])
        holder.imageGreenTicks.setImageResource(greenTicks[position])
        holder.imagePlusImageButtons.setImageResource(plusImageButtons[position])
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var imageEmptyGlass: ImageView
        var imageFullGlass: ImageView
        var imageGreenTicks: ImageView
        var imagePlusImageButtons: ImageView
        var counter = 0

        init {
            imageEmptyGlass = itemView.findViewById(R.id.emptyGlass)
            imageFullGlass = itemView.findViewById(R.id.fullGlass)
            imageGreenTicks = itemView.findViewById(R.id.greenTick)
            imagePlusImageButtons = itemView.findViewById(R.id.plusButtonItem)
            itemView.setOnClickListener{
                counter++
                if(counter%2==0)
                {
                    imageEmptyGlass.visibility = View.VISIBLE
                    imageFullGlass.visibility = View.INVISIBLE
                    imagePlusImageButtons.visibility = View.VISIBLE
                    imageGreenTicks.visibility = View.INVISIBLE
                }
                else
                {
                    imageEmptyGlass.visibility = View.INVISIBLE
                    imageFullGlass.visibility = View.VISIBLE
                    imagePlusImageButtons.visibility = View.INVISIBLE
                    imageGreenTicks.visibility = View.VISIBLE
                }
            }
        }
    }

}