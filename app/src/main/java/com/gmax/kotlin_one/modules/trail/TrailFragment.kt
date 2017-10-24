package com.gmax.kotlin_one.modules.trail

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.*
import com.amap.api.maps.model.MyLocationStyle
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.R
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.base.BaseBindingFragment
import com.gmax.kotlin_one.databinding.FragmentTrailBinding
import com.gmax.kotlin_one.showToast
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.custom_drawer_layout.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class TrailFragment : BaseBindingFragment<FragmentTrailBinding>(){


    companion object{
        fun newInstance(content: String): TrailFragment {
            val args = Bundle()
            args.putString("ARGS", content)
            val fragment = TrailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var mAMap: AMap
    lateinit var mUiset: UiSettings
    var mLocationClient: AMapLocationClient? = null
    lateinit var mLocationClientOption: AMapLocationClientOption
    var mChangedListener: LocationSource.OnLocationChangedListener? = null

    var isMapNormal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tral_map.onCreate(savedInstanceState)
    }
    override fun initView() {

        initDrawer()
        initMap()
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentTrailBinding {
        return  FragmentTrailBinding.inflate(inflater,container,false)
    }

    private fun initDrawer() {

        val mDrawerToggle = object : ActionBarDrawerToggle(activity, drawerlaout, tl_custom, 0, 0) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
            }
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }
        }

        mDrawerToggle.syncState()
        @Suppress("DEPRECATION")
        drawerlaout.setDrawerListener(mDrawerToggle)

        new_wei.setOnClickListener{
            if (isMapNormal) {
                mAMap.mapType = AMap.MAP_TYPE_NORMAL
                isMapNormal = false
                new_wei.setImageResource(R.mipmap.icon_map_2)
            } else {
                mAMap.mapType = AMap.MAP_TYPE_SATELLITE
                isMapNormal = true
                new_wei.setImageResource(R.mipmap.icon_map_1)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        tral_map.onResume()
        Log.e("dfaf","Trail-onResume")
//        val currentNum = SpUtils.getInt(activity, Constant.CLICK_NUM)


    }

    override fun onPause() {
        super.onPause()
        tral_map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tral_map.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tral_map.onDestroy()
    }

    fun initMap(){
        mAMap = tral_map.map
        mUiset = mAMap.uiSettings//设置ui控件

        mUiset.isMyLocationButtonEnabled = true

        mUiset.isZoomControlsEnabled = false
        mUiset.isScaleControlsEnabled = true//设置地图默认显示比例
        mUiset.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
        val cu = CameraUpdateFactory.zoomTo(15f)
        mAMap.moveCamera(cu)

        //自定义定位小蓝点
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0))// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))// 设置圆形的填充颜色

        mAMap.setLocationSource(object : LocationSource{
            override fun deactivate() {
                mChangedListener = null
                if (mChangedListener != null) {
                    mLocationClient!!.stopLocation()
                    mLocationClient!!.onDestroy()
                }
                mLocationClient = null
            }
            override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener) {
                mChangedListener = onLocationChangedListener
            }
        })

        mAMap.setMyLocationStyle(myLocationStyle)
        mAMap.isMyLocationEnabled = true
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW)
        mAMap.mapType = AMap.MAP_TYPE_NORMAL

        mAMap.clear()

        initClient()
        initOption()
    }

    internal fun initClient() {
        //初始化定位
        mLocationClient = AMapLocationClient(context)
        //设置定位回调监听
        mLocationClient!!.setLocationListener {
            aMapLocation ->
            Log.e("aMapLocation", aMapLocation.errorCode.toString() + "-------1")
            when (aMapLocation.errorCode) {
                0 -> if (mChangedListener != null) {
                    mChangedListener!!.onLocationChanged(aMapLocation)

//                    firstLatLng = LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())

                    val cu = CameraUpdateFactory.zoomTo(15f)
                    mAMap.moveCamera(cu)

                }
                4 -> activity.showToast("当前网络较差,定位失败")
                7 ->
                    activity.showToast("没有权限")
            }
        }

        val listener = AMap.OnMarkerClickListener { marker ->
//            showPop(marker)
            true
        }
        mAMap.setOnMarkerClickListener(listener)
    }
    internal fun initOption() {

        //初始化定位参数

        mLocationClientOption = AMapLocationClientOption()

        mLocationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving

        mLocationClientOption.isNeedAddress = true

        //设置是否只定位一次，
        mLocationClientOption.isOnceLocation = true
        mLocationClientOption.interval = 2000

        if (mLocationClientOption.isOnceLocationLatest) {
            mLocationClientOption.isOnceLocationLatest = true
        }
        mLocationClientOption.isWifiActiveScan = true
        mLocationClientOption.isMockEnable = false

        mLocationClient!!.setLocationOption(mLocationClientOption)
        mLocationClient!!.startLocation()

    }

}