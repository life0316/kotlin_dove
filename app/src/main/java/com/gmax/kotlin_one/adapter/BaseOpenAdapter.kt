package com.gmax.kotlin_one.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.base.MyBaseAdapter
import com.gmax.kotlin_one.common.CommentListener
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Administrator on 2017\8\9 0009.
 */
abstract class BaseOpenAdapter : MyBaseAdapter<BaseHolder>() {
    internal var holder:BaseHolder? = null
    internal var checkPosList = ArrayList<Int>()
    internal var unCheckPosList = ArrayList<Int>()
    internal var map: MutableMap<Int, Boolean> = HashMap()

    //接口实例
    protected var onItemClickListener: CommentListener.RecyclerViewOnItemClickListener? = null
    protected var mItemCheckListener: CommentListener.MyItemCheckListener? = null


    //是否显示单选框,默认false
    var isshowBox = false
    protected var longClickTag: Boolean? = false
    protected var count: Int = 0
    internal var longIntTag = 0


    fun setLongClickTag(flag: Boolean) {
        this.longClickTag = flag

        if (flag) {
            longIntTag = 1
        } else {
            longIntTag = 0
        }
    }

    //设置是否显示CheckBox
    fun setShowBox() {
        //取反
        isshowBox = !isshowBox
    }

    //点击item选中CheckBox
    fun setSelectItem(position: Int) {
        //对当前状态取反
        if (map[position]!!) {
            map.put(position, false)
        } else {
            map.put(position, true)
        }

        count = 0

        for (i in 0..map.size - 1) {

            if (map[i]!!) {
                count++
            }
        }
        if (mItemCheckListener != null) {

            mItemCheckListener!!.itemChecked(null, count)
        }
        notifyItemChanged(position)
    }

    //设置点击事件
    fun setRecyclerViewOnItemClickListener(onItemClickListener: CommentListener.RecyclerViewOnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setItemCheckListener(itemCheckListener: CommentListener.MyItemCheckListener) {
        this.mItemCheckListener = itemCheckListener
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseHolder {
        val view = LayoutInflater.from(parent?.context).inflate(getLayout(), null)

        val holder:BaseHolder = BaseHolder(view)
        this.holder = holder

        view.setOnClickListener{
            v ->
            if (onItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                onItemClickListener!!.onItemClickListener(v, v.tag as Int, longClickTag!!)
            }
        }
        view.setOnLongClickListener{
            v ->
            if (longClickTag!!) {
                longIntTag = 1
            }
            if (longIntTag == 1) {
                return@setOnLongClickListener false
            }
            //不管显示隐藏，清空状态
            initMap()
            //longClickTag = true;
            return@setOnLongClickListener if (longClickTag!!) false else onItemClickListener != null && onItemClickListener!!.onItemLongClickListener(v, v.getTag() as Int, longClickTag!!)
        }

        return holder
    }

    abstract fun initMap()
    abstract fun getLayout():Int

}