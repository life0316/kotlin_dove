package com.gmax.kotlin_one.modules.dove

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityAddDoveBinding
import com.gmax.kotlin_one.inject.CodeModule
import com.gmax.kotlin_one.inject.DaggerCodeComponent
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.widget.wheelview.OnWheelChangedListener
import com.gmax.kotlin_one.widget.wheelview.WheelView
import com.gmax.kotlin_one.widget.wheelview.adapters.ArrayWheelAdapter
import kotlinx.android.synthetic.main.activity_add_dove.*
import kotlinx.android.synthetic.main.addpigeon_eyes_dialog.view.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.personal_sex_dialog.view.*
import kotlinx.android.synthetic.main.pigeon_name_dialog.view.*
import java.util.HashMap
import javax.inject.Inject

class AddDoveActivity:BaseBindingActivity<ActivityAddDoveBinding>(), CodesContract.View{


    @Inject lateinit var mCodePresenter: CodesPresenter
//    internal var isAdd = false
    internal var mValue: String = "桃红纱"

    companion object{
        val ADD_DOVE_COLOR_TAG = "color"
        val ADD_DOVE_BLOOD_TAG = "blood"
    }

    override fun initView() {
        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getString(R.string.add_dove)
        setupToolbar(tl_custom)

        DaggerCodeComponent.builder()
                .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
                .codeModule(CodeModule(this))
                .build().inject(this)

        sex.setOnClickListener {

            setDoveSex()
        }

        add_color.setOnClickListener { showEvDialog(ADD_DOVE_COLOR_TAG) }
        add_blood.setOnClickListener { showEvDialog(ADD_DOVE_BLOOD_TAG) }

        addconfirm.setOnClickListener {

            if (isFastDoubleClick()) return@setOnClickListener
            if (isVelidated()) mCodePresenter.getData(getParaMap(),MethodType.METHOD_TYPE_DOVE_ADD)

        }

        add_eyes.setOnClickListener {
            showEyesDialog()
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityAddDoveBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_add_dove)
    }
    fun setDoveSex(){
        val mDialog = Dialog(this, R.style.DialogTheme)
        val view = layoutInflater.inflate(R.layout.personal_sex_dialog, null)

        view.dialog_sex_ll_male.setOnClickListener {
            sex.text = view.dialog_sex_male.text
            mDialog.dismiss()
        }
        view.dialog_sex_ll_female.setOnClickListener {
            sex.text = view.dialog_sex_female.text
            mDialog.dismiss()
        }
        mDialog.setContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setDialogWindow(mDialog)
        mDialog.show()
    }

    fun showEyesDialog() {

        val mDialog = Dialog(this, R.style.DialogTheme)
        val view = layoutInflater.inflate(R.layout.addpigeon_eyes_dialog, null)
        mDialog.setCancelable(false)
        view.addpigeon_cancle.setOnClickListener { mDialog.dismiss() }
        view.addpigeon_confirm.setOnClickListener {
            add_eyes.text = mValue
            mDialog.dismiss()
        }

        val mDatas = arrayOf("云砂", "桃红砂", "云桃红砂", "蓝水桃花", "土红砂", "黄底红砂", "黄底飘红砂", "红砂", "紫砂", "粗红砂", "油眼砂", "乌眼砂", "酱砂")

        mValue = getDoveEye()

        view.addpigeon_wv.viewAdapter = ArrayWheelAdapter<String>(this, mDatas)

        mDatas.indices
                .filter { mValue == mDatas[it] }
                .forEach { view.addpigeon_wv.currentItem = it }

        view.addpigeon_wv.addChangingListener { _, _, newValue -> mValue = mDatas[newValue] }

        mDialog.setContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setDialogWindow(mDialog)
        mDialog.show()
    }

    internal fun showEvDialog(name: String) {

        val mDialog_et = Dialog(this, R.style.DialogTheme)
        val view = layoutInflater.inflate(R.layout.pigeon_name_dialog
                , null)

        when (name) {
            ADD_DOVE_COLOR_TAG -> {
                view.name_title.text = getString(R.string.add_pigeon_color)
                view.pigeon_name.setText(add_color.text.toString().trim())
            }
            ADD_DOVE_BLOOD_TAG -> {
                view.name_title.text = getString(R.string.add_pigeon_blood)
                view.pigeon_name.setText(add_blood.text.toString().trim())
            }
        }

//        mTv.isFocusable = true
//        mTv.requestFocus()

        view.pigeon_name.setSelection(view.pigeon_name.text.length)

        view.name_cancle.setOnClickListener { mDialog_et.dismiss() }
        view.name_confirm.setOnClickListener {
            val met = view.pigeon_name.text.toString().trim ()
                if (ADD_DOVE_COLOR_TAG == name) {
                    add_color.text = met
                } else {
                    add_blood.text = met
                }
                mDialog_et.dismiss()
        }
        mDialog_et.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        mDialog_et.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        mDialog_et.setContentView(view, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setDialogWindow(mDialog_et)
        mDialog_et.show()
    }

    private fun isVelidated(): Boolean {

        if ("" == getRingCode()) {
            showToast(getString(R.string.add_ringcode_null))
            return false
        }

//        if (mPigeonCodes.contains(getRingCode())) {
//            ApiUtils.showToast(this, getString(R.string.add_pigeon_code_exist))
//            return false
//        }

        if ("" == getDoveSex()) {
            showToast(getString(R.string.add_pigeon_sex_null))
            return false
        }
        if ("" == getDoveColor()) {
            showToast(getString(R.string.add_pigeon_color_null))
            return false
        } else if (!checkHanZi(getDoveColor())) {
            showToast(getString(R.string.add_pigeon_color_check))
            return false
        }

        if ("" == getDoveOld()) {
            showToast(getString(R.string.add_pigeon_old_null))
            return false
        }

        if ("" == getDoveBlood()) {
            showToast(getString(R.string.add_pigeon_blood_null))
            return false
        } else if (!checkHanZi(getDoveBlood())) {
           showToast(getString(R.string.add_pigeon_blood_check))
            return false
        }
        return true
    }

    fun getRingCode():String = ringcode.text.toString().trim()
    fun getDoveOld():String = add_old.text.toString().trim()
    fun getDoveColor():String = add_color.text.toString().trim()
    fun getDoveBlood():String = add_blood.text.toString().trim()
    fun getDoveEye():String = add_eyes.text.toString().trim()
    fun getDoveSex():String {
        val doveSex = sex.text.toString().trim()
        when(doveSex){
            "公" -> return "1"
            "母" -> return "2"
        }
        return "1"
    }

    override fun successToDo() {
        this.finish()
    }

    override fun getMethod(): String {
        return MethodConstant.DOVE_ADD
    }

    fun getParaMap(): Map<String, String> {

        val map = HashMap<String, String>()

        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_TOKEN, getToken())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())
        map.put(MethodParams.PARAMS_PLAYER_ID, getUserObjId())
        map.put(MethodParams.PARAMS_FOOT_RING, getRingCode())
        map.put(MethodParams.PARAMS_GENDER, getDoveSex())
        map.put(MethodParams.PARAMS_AGE, "1")
        map.put(MethodParams.PARAMS_COLOR, getDoveColor())
        map.put(MethodParams.PARAMS_EYE, getDoveEye())
        map.put(MethodParams.PARAMS_ANCESTRY, getDoveBlood())

        return map
    }
}