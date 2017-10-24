package com.gmax.kotlin_one.common

import android.support.v7.widget.RecyclerView

interface OnHolderListener<T, VH : RecyclerView.ViewHolder> {
    fun toInitHolder(holder: VH, position: Int, data: T)
}