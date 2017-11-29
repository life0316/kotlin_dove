package com.gmax.kotlin_one.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.base.MyBaseAdapter
import com.gmax.kotlin_one.bean.CircleBean
import com.gmax.kotlin_one.common.CommentListener
import com.gmax.kotlin_one.common.OnHolderListener
import java.util.ArrayList


class CircleAdapter<T>(private val context: Context    // 上下文Context
                       , private val holdType: Int) : MyBaseAdapter<CircleAdapter<T>.MyRefrashHolder>(){
    private var datas: MutableList<T> = ArrayList()
    private val normalType = 0

    // 获取条目数量，之所以要加1是因为增加了一条footView
    override fun getItemCount(): Int {
        return datas.size
    }

    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    override fun getItemViewType(position: Int): Int {
        return normalType
    }

    fun getDatas(): MutableList<T> {
        return datas
    }

    fun getItem(position: Int): T {
        return datas[position]
    }

    // 正常item的ViewHolder，用以缓存findView操作
    inner class MyRefrashHolder(itemView: View) : BaseHolder(itemView){
        init {
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRefrashHolder {
        val view = LayoutInflater.from(context).inflate(if (layoutRes == 0) R.layout.item_friend_circle else layoutRes, null)
            return MyRefrashHolder(view)
    }

    internal var layoutRes = 0

    fun setLayout(res: Int) {
        layoutRes = res
    }

    override fun onBindViewHolder(holder: MyRefrashHolder, position: Int) {
        // 如果是正常的imte，直接设置TextView的值
        if (holder is BaseHolder) {
            onHolderListener!!.toInitHolder(holder, position, datas[position])
        }
    }

    fun updateList(newDatas: MutableList<T>?) {
        if (newDatas != null) {
            this.datas = newDatas
        }
        notifyDataSetChanged()
    }

    fun addData(newDatas: List<T>?) {
        if (newDatas != null) {
            datas.addAll(newDatas)
        }
        notifyDataSetChanged()
    }
    var onHolderListener: OnHolderListener<T, MyRefrashHolder>? = null
}
