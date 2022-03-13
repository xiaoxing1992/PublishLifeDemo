package com.rzw.publish.bean

data class RemoteImage(val uid: String, var index :Int = 0):Comparable<RemoteImage>{
    override fun compareTo(other: RemoteImage): Int {
        return if(this.index>other.index) 1 else -1
    }
}