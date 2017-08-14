package com.gmax.kotlin_one.adapter

import android.graphics.Color
import android.view.View
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.bean.RingBean
import kotlinx.android.synthetic.main.item_myring.view.*
import org.jetbrains.anko.textColor
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Administrator on 2017\8\8 0008.
 */
class RingAdapter
    @Inject constructor() :BaseOpenAdapter() {
    override fun getLayout(): Int {
        return R.layout.item_myring
    }

    internal var datas: List<RingBean>? = ArrayList<RingBean>()

    override fun getItemCount(): Int {
        return if (datas == null && datas?.size == 0) 0 else datas!!.size
    }

    override fun onBindViewHolder(holder: BaseHolder?, position: Int) {
        val ringBean:RingBean = datas!![position]
        //长按显示/隐藏
        if (isshowBox) {
            holder?.itemView?.ch?.visibility = View.VISIBLE
        } else {
            holder?.itemView?.ch?.visibility = View.GONE
        }

        holder?.itemView?.tag = position

        holder?.itemView?.ch?.setOnCheckedChangeListener {
            buttonView, isChecked ->

            map.put(position,isChecked)
            count = 0
            for (i in 0..map.size - 1) {

                if (map[i] != null && map[i]!!) {
                    count++
                }
            }

            if (mItemCheckListener != null) {
                mItemCheckListener!!.itemChecked(buttonView, count)
            }
        }

        if (!map.containsKey(position)) {
            map.put(position, false)
        }
        holder?.itemView?.ch?.isChecked = map[position]!!
        if ("-1" != ringBean.ring_code) {
            holder?.itemView?.item_circle_id?.setText(ringBean.ring_code)
        }


        if ("" != ringBean.doveid && "-1" != ringBean.doveid ) {

            holder?.itemView?.item__mate?.setText("匹配:" + ringBean.doveid)
            holder?.itemView?.item__mate?.setTextColor(Color.GREEN)
//            if (!mateList.contains(ringBean.doveid)) {
//                mateList.add(ringBean.doveid)
//            }


        } else {
            holder?.itemView?.item__mate?.text = "未匹配"
            holder?.itemView?.item__mate?.textColor = Color.GREEN
        }
    }

    //初始化map集合,默认为不选中
    override fun initMap() {
        for (i in datas!!.indices) {
            map.put(i, false)
        }
        count = 0
    }

    fun addData(datas: List<RingBean>) {
        this.datas = datas
        count = datas.size
        unCheckPosList.clear()
        map.clear()
        notifyDataSetChanged()
    }

    fun setOpen(isopen: Boolean) {
        if (datas != null) {
            for (i in datas!!.indices) {
                datas!!.get(i).open = isopen
            }
            notifyDataSetChanged()
        }
    }
}