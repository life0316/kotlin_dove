package com.gmax.kotlin_one.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.base.MyBaseAdapter
import java.util.ArrayList

/**
 * Created by Administrator on 2017\8\10 0010.
 */
abstract class BaseComAdapter<T> : MyBaseAdapter<BaseHolder>(){

    private var data:MutableList<T> = ArrayList<T>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseHolder {
        val view : View =  LayoutInflater.from(parent?.context).inflate(getLayout(), null)
        val holder:BaseHolder = BaseHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: BaseHolder?, position: Int) {
        bindHolder(holder,position,data[position])
    }

    abstract fun  bindHolder(holder: BaseHolder?, position: Int, data: T)

    fun addData(dataE:T){
        this.data.add(dataE)
        notifyDataSetChanged()
    }

    fun addDatas(dataList:MutableList<T>){
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }

    fun updateDatas(dataList:MutableList<T>){
        this.data.clear()
        this.data.addAll(dataList)
        notifyDataSetChanged()
    }

    abstract fun getLayout():Int

}