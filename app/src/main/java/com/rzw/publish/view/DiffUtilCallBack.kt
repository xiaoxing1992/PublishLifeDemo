package com.rzw.publish.view

import androidx.recyclerview.widget.DiffUtil
import com.luck.picture.lib.entity.LocalMedia

class DiffUtilCallBack(
    private val oldList:MutableList<LocalMedia>?,
    private val newList:MutableList<LocalMedia>?): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return if(!oldList.isNullOrEmpty()) oldList.size else 0
    }

    override fun getNewListSize(): Int {
        return if(!newList.isNullOrEmpty()) newList.size else 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList!![oldItemPosition].id == newList!![newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if(oldList!![oldItemPosition].path != newList!![newItemPosition].path){
            return false
        }
       return true
    }
}