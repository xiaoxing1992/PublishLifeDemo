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

class ImageGridAdapter(context: Context) :
    RecyclerView.Adapter<ImageGridAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var list: MutableList<LocalMedia> = mutableListOf()

    //最大选择数
    private var selectMax = 6


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mRvImage: RoundImageView = itemView.findViewById(R.id.rv_image)
        val mClAddImage: ConstraintLayout = itemView.findViewById(R.id.cl_add_image)
        val mIvDelete: ImageView = itemView.findViewById(R.id.iv_del)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_grid_filter_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //如果少于最大选择数，显示添加图标
        when (getItemViewType(position)) {
            TYPE_CAMERA -> {
                holder.mRvImage.setImageDrawable(null)
                holder.mClAddImage.visibility = View.VISIBLE
                holder.mRvImage.visibility = View.INVISIBLE
                holder.mIvDelete.visibility = View.INVISIBLE
                holder.mClAddImage.setOnClickListener {
                    mOnItemClickListener?.openPicture()
                }
            }
            else -> {
                holder.mClAddImage.visibility = View.INVISIBLE
                holder.mRvImage.visibility = View.VISIBLE
                holder.mIvDelete.visibility = View.VISIBLE
                holder.mRvImage.setImageResource(R.drawable.ic_launcher_background)

                holder.mIvDelete.setOnClickListener {
                    val index = holder.absoluteAdapterPosition
                    if(index !=RecyclerView.NO_POSITION && list.size>index) {
                        val newList = mutableListOf<LocalMedia>()
                        newList.addAll(list)
                        newList.removeAt(index)
                        updateListItems(newList)
                    }
                }


                val media = list[position]
                val path = ImageUtil.getImagePath(media)
                Glide.with(holder.itemView.context)
                    .load(
                        if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) Uri.parse(
                            path
                        ) else path
                    )
                    .centerCrop()
                    .placeholder(R.color.add_iv_bg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mRvImage)
            }
        }
    }

    override fun getItemCount(): Int {
        //为了判断
        return if (list.size < selectMax) {
            list.size + 1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size == position) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }

    interface OnItemClickListener {
        //条目点击事件
        fun onItemClick(v: View, position: Int)

        //添加选择图片
        fun openPicture()
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    fun getSelectMax(): Int {
        return selectMax;
    }

    fun getData(): MutableList<LocalMedia> {
        return list;
    }

    fun getArrayData(): ArrayList<LocalMedia> {
        return ArrayList(list);
    }

    //设置数据
    fun setDatas(mPicList: MutableList<LocalMedia>) {
        this.list.addAll(mPicList)
    }

    //更新数据
    fun updateListItems(data: MutableList<LocalMedia>) {
        val diffUtilCallBack  =DiffUtilCallBack(this.list,data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        this.list.clear()
        this.list.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }


    companion object {
        const val TYPE_CAMERA = 1
        const val TYPE_PICTURE = 2
    }
}