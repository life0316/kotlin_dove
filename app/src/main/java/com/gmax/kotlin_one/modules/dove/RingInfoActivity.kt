package com.gmax.kotlin_one.modules.dove

import android.annotation.SuppressLint
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.adapter.BaseComAdapter
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.base.BaseHolder
import com.gmax.kotlin_one.bean.InnerDoveData
import com.gmax.kotlin_one.bean.RingBean
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityRingInfoBinding
import com.gmax.kotlin_one.inject.DaggerDoveComponent
import com.gmax.kotlin_one.inject.DoveModule
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.mvp.DoveContract
import com.gmax.kotlin_one.mvp.DovePresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.RxBus
import com.gmax.kotlin_one.widget.RecycleviewDaviding
import kotlinx.android.synthetic.main.activity_ring_info.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.item_rv_mate_pigeon.view.*
import kotlinx.android.synthetic.main.mate_pigeon_dialog.view.*
import java.util.HashMap
import javax.inject.Inject

class RingInfoActivity : BaseBindingActivity<ActivityRingInfoBinding>(),DoveContract.View, CodesContract.View{

    @Inject lateinit var mDovePresenter: DovePresenter
    @Inject lateinit var mCodePresenter: CodesPresenter
    @Inject lateinit var mRxBus: RxBus
    lateinit var ringBean:RingBean
    private var methodType: Int = 0
    private var updateRing: Int = 0

    lateinit var update_onoff_status: String
    lateinit var update_lfq: String
    lateinit var update_rfq: String
    lateinit var update_ontime: String
    lateinit var update_offtime: String
    internal var dialog: Dialog? = null
    internal var mateAdapter: BaseComAdapter<InnerDoveData>? = null


    companion object{

        val RING_BEAN:String = "ringBean"
        private val UPDATE_RING_ONOFF_STATUS = 1
        private val UPDATE_RING_LFQ = 2     //定位频率
        private val UPDATE_RING_RFQ = 3  //上报频率
        private val UPDATE_RING_ON_TIME = 4  //开机时间
        private val UPDATE_RING_OFF_TIME = 5  //关机时间

    }

    override fun initView() {

        DaggerDoveComponent.builder()
                .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
                .doveModule(DoveModule(this,this))
                .build().inject(this)

        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getString(R.string.ring_info)
        setupToolbar(tl_custom)

        ringBean = intent!!.getParcelableExtra(RING_BEAN)
        ringcode.text = ringBean.ring_code
        ring_startdate.text = ringBean.create_time

        if ("2"== ringBean.on_off_status || "关"== ringBean.on_off_status)
            ring_startstatus.text = getString(R.string.off_time_state) else ring_startstatus.text = getString(R.string.on_time_state)

        if ("" == ringBean.dove_code || "-1" == ringBean.dove_code)
            ring_footcode.text = getString(R.string.pigeon_mate_no) else ring_footcode.text = ringBean.dove_code

        if ("" != ringBean.off_time) schedule_off.text = ringBean.off_time
        if (""!= ringBean.on_time) schedule_on.text = ringBean.on_time

            when(ringBean.lfq){
                1 -> ring_position.text = getString(R.string.one_min_once)
                2 -> ring_position.text = getString(R.string.two_min_once)
                3 -> ring_position.text = getString(R.string.third_min_once)
                4 -> ring_position.text = getString(R.string.four_min_once)
                5 -> ring_position.text = getString(R.string.five_min_once)
                else -> ring_position.text = getString(R.string.five_min_once)
            }

        when(ringBean.rfq){
            1 -> reported_freq.text = getString(R.string.one_min_once)
            2 -> reported_freq.text = getString(R.string.two_min_once)
            3 -> reported_freq.text = getString(R.string.third_min_once)
            4 -> reported_freq.text = getString(R.string.four_min_once)
            5 -> reported_freq.text = getString(R.string.five_min_once)
            else -> reported_freq.text = getString(R.string.five_min_once)
        }

        ring_footcode.setOnClickListener {
            if (getString(R.string.pigeon_mate_no) == ring_footcode.text.toString())
                setMateDove()
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityRingInfoBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_ring_info)
    }

    fun setMateDove(){
        dialog = Dialog(this, R.style.DialogTheme)
        val nullParent : ViewGroup? = null
        val view = layoutInflater.inflate(R.layout.mate_pigeon_dialog, nullParent)
        dialog!!.setContentView(view, LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        view.mate_rv.layoutManager = LinearLayoutManager(this)
        view.mate_rv.addItemDecoration(RecycleviewDaviding(this, R.drawable.daviding))

        mateAdapter = object : BaseComAdapter<InnerDoveData>(){
            override fun getLayout(): Int {

                return R.layout.item_rv_mate_pigeon
            }
            @SuppressLint("SetTextI18n")
            override fun bindHolder(holder: BaseHolder?, position: Int, data: InnerDoveData) {
                holder!!.itemView.item_mate_tv.text = "环号: ${data.foot_ring}"

                holder.itemView.setOnClickListener {

                }
            }
        }

        view.mate_rv.adapter = mateAdapter
        getDoveData()
        setDialogWindow(dialog!!)
    }

    internal fun getDoveData() {
        if (!isNetworkConnected()) {
//            infoPresenter.getDatasFromDao(getUserObjId())
        } else {
            methodType = MethodType.METHOD_TYPE_DOVE_SEARCH
            mDovePresenter.getData(getParaMap(),methodType)
        }
    }

    override fun getMethod(): String {
        var method = ""

        when (methodType) {
            MethodType.METHOD_TYPE_DOVE_SEARCH -> method = MethodConstant.DOVE_SEARCH
            MethodType.METHOD_TYPE_RING_UPDATE -> method = MethodConstant.RING_INFO_UPDATE
            MethodType.METHOD_TYPE_RING_MATCH -> method = MethodConstant.RING_MATCH
        }
        return method
    }

    internal fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()

        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        map.put(MethodParams.PARAMS_TOKEN, getToken())

        when (methodType) {
            MethodType.METHOD_TYPE_DOVE_SEARCH -> map.put(MethodParams.PARAMS_PLAYER_ID, getUserObjId())
            MethodType.METHOD_TYPE_RING_UPDATE -> {
                map.put(MethodParams.PARAMS_RING_ID,ringBean.ringid)
                when (updateRing) {
                    UPDATE_RING_ONOFF_STATUS -> map.put(MethodParams.PARAMS_ONOFF_STATUS, update_onoff_status)
                    UPDATE_RING_LFQ -> map.put(MethodParams.PARAMS_LFQ, update_lfq)
                    UPDATE_RING_RFQ -> map.put(MethodParams.PARAMS_RFQ, update_rfq)
                    UPDATE_RING_ON_TIME -> map.put(MethodParams.PARAMS_ON_TIME, update_ontime)
                    UPDATE_RING_OFF_TIME -> map.put(MethodParams.PARAMS_OFF_TIME, update_offtime)
                }
            }
        }
        return map
    }
    override fun successToDo() {

    }

    override fun setData(doveData: List<InnerDoveData>) {
        if (doveData.isEmpty()) {
            showToast("没有可匹配的信鸽")
            return
        }

        dialog!!.show()
        mateAdapter!!.updateDatas(doveData as MutableList<InnerDoveData>)

    }
}