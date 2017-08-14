package com.gmax.kotlin_one.base

import android.support.v7.widget.RecyclerView
import com.gmax.kotlin_one.common.CommentListener

/**
 * Created by Administrator on 2017\8\8 0008.
 */
abstract class MyBaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(){

    //条目点击
    var mItemClickListener : CommentListener.MyItemClickListener? = null

    var MyItemLongClickListener : CommentListener.MyItemLongClickListener? = null
}