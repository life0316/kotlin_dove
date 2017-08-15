package com.gmax.kotlin_one.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.adapter.DoveAdapter
import com.gmax.kotlin_one.base.BaseBindingFragment
import com.gmax.kotlin_one.bean.InnerDoveData
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.common.CommentListener
import com.gmax.kotlin_one.databinding.SmartRefrashBinding
import com.gmax.kotlin_one.inject.DaggerDoveComponent
import com.gmax.kotlin_one.inject.DoveModule
import com.gmax.kotlin_one.modules.dove.DoveInfoActivity
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.mvp.DoveContract
import com.gmax.kotlin_one.mvp.DovePresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.RxBus
import com.gmax.kotlin_one.utils.SpUtils
import com.gmax.kotlin_one.widget.CustomDialog
import kotlinx.android.synthetic.main.fragment_base_tab.*
import kotlinx.android.synthetic.main.smart_refrash.*
import javax.inject.Inject
import rx.Observable
import rx.functions.Action1

class DoveListFragment : BaseBindingFragment<SmartRefrashBinding>(), DoveContract.View,CodesContract.View{

    var methodType:Int = MethodType.METHOD_TYPE_DOVE_SEARCH
    internal var mHandler : Handler? = null
    @Inject lateinit var mDovePresenter: DovePresenter
    @Inject lateinit var mCodePresenter: CodesPresenter
    @Inject lateinit var mDoveAdapter: DoveAdapter
    @Inject lateinit var mRxbus: RxBus

    var isLoad:Boolean = true
    internal var longClickTag1: Boolean = false
    internal var countTemp: Int = 0
    private var doveBeans = ArrayList<InnerDoveData>()
    private var doveTemps = ArrayList<InnerDoveData>()
    private var mateList: MutableList<String>? =  ArrayList()
    var numMap: MutableMap<String, Boolean> = HashMap()


    internal var dialog: CustomDialog? = null
    internal var exitOb:Observable<String>? = null

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): SmartRefrashBinding {
        return SmartRefrashBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        mHandler = Handler()
        DaggerDoveComponent.builder()
                .apiCompoent(activity.getApiCompoent())
                .apiModule(ApiModule(activity))
                .doveModule(DoveModule(this,this))
                .build().inject(this)

        swiprv.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?

        swiprv.adapter = mDoveAdapter

        show_add_tv1.text = getString(R.string.need_add_ring)

        refreshLayout.isEnableLoadmore = false
        refreshLayout.setOnRefreshListener {

            if (!activity.isNetworkConnected()) {
//                mRingPresenter.getDatas()
//                setRefrash(false)
            } else {

                methodType = MethodType.METHOD_TYPE_DOVE_SEARCH
                mDovePresenter.getData(getParaMap(), methodType)
            }
        }
        select_cb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val map = mDoveAdapter.map
                for (i in 0..map.size - 1) {
                    map.put(i, true)
                    mDoveAdapter.notifyDataSetChanged()
                }
            } else {

                if (countTemp > 0 && countTemp < doveBeans.size) {

                } else {
                    val m = mDoveAdapter.map

                    for (i in 0..m.size - 1) {
                        m.put(i, false)
                        mDoveAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        mDoveAdapter.setItemCheckListener(object : CommentListener.MyItemCheckListener{
            override fun itemChecked(view: View?, count: Int) {
                countTemp = count
                if (count >= doveBeans.size) {
                    select_cb.isChecked = true
                    countTemp = count
                } else {
                    select_cb.isChecked = false
                }
            }
        })

        mDoveAdapter.setRecyclerViewOnItemClickListener(object : CommentListener.RecyclerViewOnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, longClickTag: Boolean) {

                if (!longClickTag) {
                    val doveBean = doveBeans[position]
                    val intent = Intent(activity, DoveInfoActivity::class.java)
                    intent.putExtra("ringBean", doveBean)
                    intent.putExtra("matelist_size", mateList!!.size)
                    startActivity(intent)

                } else {
                    mDoveAdapter.setSelectItem(position)
                }
            }

            override fun onItemLongClickListener(view: View, position: Int, longClickTag: Boolean): Boolean {
                longClickTag1 = longClickTag

                if (!longClickTag) {

//                    mRxBus.post("cancle", 100)
                    mRxbus.post("cancle",true)

                    //长按事件
                    mDoveAdapter.setShowBox()
                    //设置选中的项
                    mDoveAdapter.setSelectItem(position)
                    mDoveAdapter.notifyDataSetChanged()
                    mypigeon_select.visibility = View.VISIBLE
                    refreshLayout.isEnableRefresh = false
                    mDoveAdapter.setLongClickTag(true)
                    SpUtils.putBoolean(activity, Constant.MAIN_EXIT,false)
                    SpUtils.putString(activity,Constant.OTHER_NOT_EXIT,"dove")
                }
                return true
            }
        })

        select_delete.setOnClickListener {

            doveTemps.clear()

            //获取你选中的item
            val map = mDoveAdapter.map
            (0..map.size - 1)
                    .filter { map[it]!! }
                    .forEach { doveTemps.add(doveBeans[it]) }

            if (doveTemps.size > 0) {

                dialog = CustomDialog(activity, getString(R.string.delete_dove_code), getString(R.string.confirm_delete_dove_code), getString(R.string.confirm), getString(R.string.cancle))
                dialog!!.setCancelable(true)
                dialog!!.show()
                dialog!!.setClickListenerInterface(object : CustomDialog.ClickListenerInterface{
                    override fun doConfirm() {

                        if (!activity.isNetworkConnected()) {

                            activity.showToast(getString(R.string.net_conn_2))
                            return
                        }

                        for (i in doveTemps.indices) {

                            if ("" != doveTemps[i].ringid && "-1" != doveTemps[i].ringid) {
                                activity.showToast(getString(R.string.delete_doveid_no))
                                return
                            }
                        }

                        methodType = MethodType.METHOD_TYPE_DOVE_DELETE
                        mCodePresenter.getData(getParaMap(),methodType)
                        dialog!!.dismiss()
                    }

                    override fun doCancel() {
                        dialog!!.dismiss()
                    }
                })
            } else {
                activity.showToast(getString(R.string.no_doveid))
            }
        }

        exitOb = mRxbus.register("exit",String::class.java)

        exitOb!!.subscribe({
            exit ->
            Log.e("exit","dove------1")
            if (exit == "dove"){
                Log.e("exit","dove----2")
                mypigeon_select.visibility = View.GONE
                select_cb.isChecked = false
                mDoveAdapter.isshowBox = false
                mDoveAdapter.setLongClickTag(false)
                mDoveAdapter.notifyDataSetChanged()
                this.longClickTag1 = false
                SpUtils.putBoolean(activity, Constant.MAIN_EXIT,true)
                SpUtils.putString(activity,Constant.OTHER_NOT_EXIT,"")
                mRxbus.post("cancle",false)
            }
        })
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
            MethodType.METHOD_TYPE_DOVE_SEARCH -> map.put(MethodParams.PARAMS_PLAYER_ID, getUserObjId())
            MethodType.METHOD_TYPE_DOVE_DELETE -> map.put(MethodParams.PARAMS_DOVE_ID, getDeleteObjIds())
        }
        return map
    }

    override fun onResume() {
        super.onResume()
        if (isLoad) {
            methodType = MethodType.METHOD_TYPE_DOVE_SEARCH
            mDovePresenter.getData(getParaMap(),methodType)
            isLoad = false
        }
    }

    override fun getMethod(): String {
        var method = ""

        when (methodType) {
            MethodType.METHOD_TYPE_DOVE_SEARCH -> method = MethodConstant.DOVE_SEARCH
            MethodType.METHOD_TYPE_DOVE_DELETE -> method =  MethodConstant.DOVE_DELETE
        }
        return method
    }

    fun getDeleteObjIds(): String {

        val sb = StringBuffer()
            for (i in doveTemps.indices) {

                if (i == doveTemps.size - 1) {

                    sb.append(doveTemps[i].doveid)
                } else {
                    sb.append(doveTemps[i].doveid).append(",")
                }
            }
        return sb.toString()
    }


    override fun successToDo() {
        methodType = MethodType.METHOD_TYPE_DOVE_SEARCH
        mDovePresenter.getData(getParaMap(),1)
    }
    override fun setData(doveData: List<InnerDoveData>) {
        doveBeans.clear()
        mateList!!.clear()
        refreshLayout.finishRefresh(false)
        mypigeon_select.visibility = View.GONE
        select_cb.isChecked = false
        mDoveAdapter.isshowBox = false
        mDoveAdapter.setLongClickTag(false)
        this.longClickTag1 = false
        mRxbus.post("cancle",false)

        if (doveData.isNotEmpty()) {

            if (doveData.size >= 15) {
                numMap.put("ring_num", true)
            } else {
                numMap.put("ring_num", false)
            }

            doveBeans.clear()
            doveBeans.addAll(doveData)
            mDoveAdapter.addData(doveData)
            refreshLayout.isEnableRefresh = true
            mypigeon_show_add.visibility = View.GONE
//            mRxBus.post("cancle", 200)
        } else {
            refreshLayout.isEnableRefresh = false
            mDoveAdapter.addData(doveBeans)
            mypigeon_show_add.visibility = View.VISIBLE
//            mRxBus.post("cancle", 200)
//            mPigeonCodes.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRxbus.unregister("exit", exitOb!!)
    }
}