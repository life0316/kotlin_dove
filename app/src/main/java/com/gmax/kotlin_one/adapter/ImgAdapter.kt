package com.gmax.kotlin_one.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gmax.kotlin_one.ImageActivity
import com.gmax.kotlin_one.R
import kotlinx.android.synthetic.main.item_img.view.*
import android.util.Pair

class ImgAdapter (var list: ArrayList<String>, var context: Context) : RecyclerView.Adapter<ImgAdapter.ViewHolder>() {
    val pairs= arrayOfNulls<Pair<View, String>>(list.size)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        pairs[position] = Pair<View, String>(holder.itemView.item_imgview, "img" + position)
        Glide.with(context).load(list[position]).placeholder(R.mipmap.default_pic).into(holder.itemView.item_imgview)
        holder.itemView.item_imgview.setOnClickListener {
            ImageActivity.startActivity(context, pairs, position, list)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_img, parent, false))

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
}