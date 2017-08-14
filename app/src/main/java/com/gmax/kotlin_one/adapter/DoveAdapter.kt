package com.gmax.kotlin_one.adapter

import android.view.View
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.bean.InnerDoveData
import com.gmax.kotlin_one.getMonth
import kotlinx.android.synthetic.main.item_my_dove.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Administrator on 2017\8\8 0008.
 */
class DoveAdapter
    @Inject constructor() :BaseOpenAdapter() {
    override fun getLayout(): Int {
        return R.layout.item_my_dove
    }

    internal var datas: List<InnerDoveData>? = ArrayList<InnerDoveData>()

    override fun getItemCount(): Int {
        return if (datas == null && datas?.size == 0) 0 else datas!!.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val doveBean:InnerDoveData = datas!![position]
        //长按显示/隐藏
        if (isshowBox) {
            holder.itemView?.item_cb?.visibility = View.VISIBLE
        } else {
            holder.itemView?.item_cb?.visibility = View.GONE
        }

        holder.itemView?.tag = position

        holder.itemView?.item_cb?.setOnCheckedChangeListener {
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
        holder.itemView?.item_cb?.isChecked = map[position]!!

            if ("" == doveBean.gender)
                doveBean.gender = "1"

            when (doveBean.gender) {
                "1", "公" -> {
                    holder.itemView!!.item_rv_pigeonsex.setImageResource(R.mipmap.icon_male)
                    holder.itemView.item_iv.setImageResource(R.mipmap.icon_img_2)
                }
                "2", "母" -> {
                    holder.itemView!!.item_rv_pigeonsex.setImageResource(R.mipmap.icon_female)
                    holder.itemView.item_iv.setImageResource(R.mipmap.icon_img_3)
                }
            }
        if ("" != doveBean.ring_code&& "-1" != doveBean.ring_code) {

            holder.itemView!!.item_ismate.visibility = View.GONE
            holder.itemView.item_color.visibility = View.GONE
            holder.itemView.item_rv_doveage.text = "匹配：" + doveBean.ring_code
            //holder.mPegionOld.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        } else {
            holder.itemView!!.item_color.visibility = View.VISIBLE
            holder.itemView.item_color.text = doveBean.color

            if ("" != doveBean.create_time) {
                //
                val pigeonBirthday = doveBean.create_time

                val date = Date()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val dateNowStr = sdf.format(date)

                val getMonth = getMonth(dateNowStr, pigeonBirthday.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])

                val year = getMonth / 12
                val month = getMonth % 12

                holder.itemView.item_rv_doveage.text = if (year == 0) if (month == 0) "1个月" else "${month}个月" else if (month == 0) "${year}年" else "${year}年${month}个月"

            } else {
                holder.itemView.item_rv_doveage.text = "1个月"
            }
        }
        holder.itemView.item_circleid.text = doveBean.foot_ring

    }

    //初始化map集合,默认为不选中
    override fun initMap() {
        for (i in datas!!.indices) {
            map.put(i, false)
        }
        count = 0
    }

    fun addData(datas: List<InnerDoveData>) {
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