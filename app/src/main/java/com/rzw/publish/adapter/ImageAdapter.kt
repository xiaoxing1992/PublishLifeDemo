package com.rzw.publish.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.csdn.roundview.RoundImageView
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.rzw.publish.R
import com.rzw.publish.utils.ImageUtil
import com.rzw.publish.view.DiffUtilCallBack
import java.util.ArrayList

class ImageAdapter(context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var list: MutableList<String> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mRvImage: RoundImageView = itemView.findViewById(R.id.rv_image)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_grid_filter_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = list[position]
        Glide.with(holder.itemView.context)
            .load(url)
            .centerCrop()
            .placeholder(R.color.add_iv_bg)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.mRvImage)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    fun setList(adapterList: MutableList<String>) {
        list.clear()
        list.addAll(adapterList)
        notifyDataSetChanged()
    }

}