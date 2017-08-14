package com.gmax.kotlin_one.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.adapter.RingAdapter
import com.gmax.kotlin_one.base.BaseBindingFragment
import com.gmax.kotlin_one.bean.RingBean
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.common.CommentListener
import com.gmax.kotlin_one.databinding.SmartRefrashBinding
import com.gmax.kotlin_one.getApiCompoent
import com.gmax.kotlin_one.inject.DaggerRingComponent
import com.gmax.kotlin_one.inject.RingModule
import com.gmax.kotlin_one.isNetworkConnected
import com.gmax.kotlin_one.modules.dove.RingInfoActivity
import com.gmax.kotlin_one.mvp.*
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.showToast
import com.gmax.kotlin_one.widget.CustomDialog
import kotlinx.android.synthetic.main.smart_refrash.*
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class RingListFragment : BaseBindingFragment<SmartRefrashBinding>(), RingContract.View,CodesContract.View{

    var methodType:Int = MethodType.METHOD_TYPE_RING_SEARCH
    internal var mHandler : Handler? = null
    @Inject lateinit var mRingPresenter: RingPresenter
    @Inject lateinit var mCodePresenter: CodesPresenter
    @Inject lateinit var mRingAdapter: RingAdapter

    var isLoad:Boolean = true
    internal var longClickTag1: Boolean = false
    internal var countTemp: Int = 0
    private var ringBeans = ArrayList<RingBean>()
    private var ringTemps = ArrayList<RingBean>()
    private var mateList: MutableList<String>? =  ArrayList()
    var numMap: MutableMap<String, Boolean> = HashMap()

    internal var dialog: CustomDialog? = null


    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): SmartRefrashBinding {
        return SmartRefrashBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        mHandler = Handler()

        DaggerRingComponent.builder()
                .apiCompoent(activity.getApiCompoent())
                .apiModule(ApiModule(activity))
                .ringModule(RingModule(this,this))
                .build().inject(this)

        swiprv.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?

        swiprv.adapter = mRingAdapter

        show_add_tv1.text = getString(R.string.need_add_ring)

        refreshLayout.isEnableLoadmore = false
        refreshLayout.setOnRefreshListener {

            if (!activity.isNetworkConnected()) {
//                mRingPresenter.getDatas()
//                setRefrash(false)
            } else {

                methodType = MethodType.METHOD_TYPE_RING_SEARCH
                mRingPresenter.getData(getParaMap(), methodType)
            }
        }
        myring_select_cb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val map = mRingAdapter.map
                for (i in 0..map.size - 1) {
                    map.put(i, true)
                    mRingAdapter.notifyDataSetChanged()
                }
            } else {

                if (countTemp > 0 && countTemp < ringBeans.size) {

                } else {
                    val m = mRingAdapter.map

                    for (i in 0..m.size - 1) {
                        m.put(i, false)
                        mRingAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        mRingAdapter.setItemCheckListener(object : CommentListener.MyItemCheckListener{
           override fun itemChecked(view: View?, count: Int) {
               countTemp = count
               if (count >= ringBeans.size) {
                   myring_select_cb.isChecked = true
                   countTemp = count
               } else {
                   myring_select_cb.isChecked = false
               }
           }
       })
        mRingAdapter.setRecyclerViewOnItemClickListener(object : CommentListener.RecyclerViewOnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, longClickTag: Boolean) {

                if (!longClickTag) {
                    val ringBean = ringBeans[position]
                    val intent = Intent(activity, RingInfoActivity::class.java)
                    intent.putExtra(RingInfoActivity.RING_BEAN, ringBean)
                    intent.putExtra("matelist_size", mateList!!.size)
                    startActivity(intent)

                } else {
                    mRingAdapter.setSelectItem(position)
                }
            }

            override fun onItemLongClickListener(view: View, position: Int, longClickTag: Boolean): Boolean {
                longClickTag1 = longClickTag

                if (!longClickTag) {

//                    mRxBus.post("cancle", 100)

                    //长按事件
                    mRingAdapter.setShowBox()
                    //设置选中的项
                    mRingAdapter.setSelectItem(position)
                    mRingAdapter.notifyDataSetChanged()
                    myring_select.visibility = View.VISIBLE
                    refreshLayout.isEnableRefresh = false
                    mRingAdapter.setLongClickTag(true)
//                    mRxBus.post("exit", 10)
                }
                return true
            }
        })
        myring_select_delete.setOnClickListener {

            ringTemps.clear()

            //获取你选中的item
            val map = mRingAdapter.map
            (0..map.size - 1)
                    .filter { map[it]!! }
                    .forEach { ringTemps.add(ringBeans[it]) }

            if (ringTemps.size > 0) {

                dialog = CustomDialog(activity, getString(R.string.delete_ring_code), getString(R.string.confirm_delete_ring_code), getString(R.string.confirm), getString(R.string.cancle))
                dialog!!.setCancelable(true)
                dialog!!.show()
                dialog!!.setClickListenerInterface(object : CustomDialog.ClickListenerInterface{
                    override fun doConfirm() {

                        if (!activity.isNetworkConnected()) {

                            activity.showToast(getString(R.string.net_conn_2))
                            return
                        }

                        for (i in ringTemps.indices) {

                            if ("" != ringTemps[i].doveid && "-1" != ringTemps[i].doveid) {
                                activity.showToast(getString(R.string.delete_ringid_no))
                                return
                            }
                        }

                        methodType = MethodType.METHOD_TYPE_RING_DELETE
                        mCodePresenter.getData(getParaMap(),methodType)
                        dialog!!.dismiss()
                    }

                    override fun doCancel() {
                        dialog!!.dismiss()
                    }
                })
            } else {
                activity.showToast(getString(R.string.no_ringid))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLoad) {
            methodType = MethodType.METHOD_TYPE_RING_SEARCH
            mRingPresenter.getData(getParaMap(),methodType)
            isLoad = false
        }
    }

    override fun getMethod(): String {
        var method = ""
        when (methodType) {
            MethodType.METHOD_TYPE_RING_SEARCH -> method = MethodConstant.RING_SEARCH
            MethodType.METHOD_TYPE_RING_DELETE -> method = MethodConstant.RING_DELETE
        }
        return method
    }

    internal fun getDeleteObjIds(): String {
        val sb = StringBuffer()
        for (i in ringTemps.indices) {
            if (i == ringTemps.size - 1) {
                sb.append(ringTemps[i].ringid)
            } else {
                sb.append(ringTemps[i].ringid).append(",")
            }
        }
        return sb.toString()
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()

        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        map.put(MethodParams.PARAMS_TOKEN, getToken())

        when (methodType) {
            MethodType.METHOD_TYPE_RING_SEARCH -> map.put(MethodParams.PARAMS_PLAYER_ID, getUserObjId())
            MethodType.METHOD_TYPE_RING_DELETE -> map.put(MethodParams.PARAMS_RING_ID, getDeleteObjIds())
        }
        return map
    }
    override fun successToDo() {
        methodType = MethodType.METHOD_TYPE_RING_SEARCH
        mRingPresenter.getData(getParaMap(),1)
    }
    override fun setData(ringData :  List<RingBean>) {
        ringBeans.clear()
        mateList!!.clear()
        refreshLayout.finishRefresh(false)
        myring_select.visibility = View.GONE
        myring_select_cb.isChecked = false
        mRingAdapter.isshowBox = false
        mRingAdapter.setLongClickTag(false)
        this.longClickTag1 = false

        if (ringData.isNotEmpty()) {

            if (ringData.size >= 15) {
                numMap.put("ring_num", true)
            } else {
                numMap.put("ring_num", false)
            }

            ringBeans.clear()
            ringBeans.addAll(ringData)
            mRingAdapter.addData(ringData)
            refreshLayout.isEnableRefresh = true
            mypigeon_show_add.visibility = View.GONE
//            mRxBus.post("cancle", 200)
        } else {
            refreshLayout.isEnableRefresh = false
            mRingAdapter.addData(ringBeans)
            mypigeon_show_add.visibility = View.VISIBLE
//            mRxBus.post("cancle", 200)
//            mPigeonCodes.clear()
        }
    }
}