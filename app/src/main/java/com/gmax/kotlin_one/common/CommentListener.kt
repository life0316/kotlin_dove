package com.gmax.kotlin_one.common

import android.view.View


interface CommentListener {

    interface MyItemClickListener : MyItemCheckListener {
         fun onItemClick(view: View, position: Int)
    }
    interface MyItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    interface MyItemCheckListener {
        fun itemChecked(view: View?, count: Int)
    }

    interface RecyclerViewOnItemClickListener {
        //点击事件
        fun onItemClickListener(view: View, position: Int, longClickTag: Boolean)

        //长按事件
        fun onItemLongClickListener(view: View, position: Int, longClickTag: Boolean): Boolean
    }

}