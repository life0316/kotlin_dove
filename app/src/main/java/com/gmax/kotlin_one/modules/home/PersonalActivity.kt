package com.gmax.kotlin_one.modules.home

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutCompat
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gmax.kotlin_one.*
import com.gmax.kotlin_one.base.BaseBindingActivity
import com.gmax.kotlin_one.bean.UserInfoInner
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.databinding.ActivityPersonalBinding
import com.gmax.kotlin_one.inject.DaggerImageComponent
import com.gmax.kotlin_one.inject.ImageModule
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.CodesPresenter
import com.gmax.kotlin_one.mvp.ImageContract
import com.gmax.kotlin_one.mvp.ImagePresenter
import com.gmax.kotlin_one.retrofit.MethodConstant
import com.gmax.kotlin_one.retrofit.MethodParams
import com.gmax.kotlin_one.retrofit.MethodType
import com.gmax.kotlin_one.utils.SpUtils
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.personal_headciv_dialog.view.*
import me.iwf.photopicker.utils.ImageCaptureManager
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class PersonalActivity : BaseBindingActivity<ActivityPersonalBinding>(),CodesContract.View, ImageContract.ImageView {

    internal var methodType = MethodType.METHOD_TYPE_IMAGE_UPLOAD
    internal var updatePamas = 0
    internal val UPDATE_BIRTH = 0
    internal val UPDATE_NAME = 1
    internal val UPDATE_GENDER = 2
    internal val UPDATE_HEADPIC = 3
    internal val UPDATE_LOFTNAME = 4
    internal val UPDATE_EXPE = 5

    internal val updateBirth = ""
    internal var updateName = ""
    internal var updateGender = ""
    internal var updateLoftName = ""
    internal var updateHeadpic = ""
    internal var updateExper = ""
    internal var updatePhotoPath: String = ""
    private var tagMsg = "拍照权限"
    private var isNeedCheck = true


    @Inject lateinit var mCodesPresenter:CodesPresenter
    @Inject lateinit var mImagePresenter:ImagePresenter
    internal var captureManager: ImageCaptureManager? = null
    internal var mScanBitmap: Bitmap? = null

    var needPermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA)

    companion object{
        val PERMISSION_REQUESTCODE_1 = 100
        val PERMISSION_REQUESTCODE_2 = 200
    }
    override fun createDataBinding(savedInstanceState: Bundle?): ActivityPersonalBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_personal)
    }
    override fun initView() {

        DaggerImageComponent.builder()
                .apiCompoent(getApiCompoent())
                .apiModule(ApiModule(this))
                .imageModule(ImageModule(this,this))
                .build().inject(this)
        captureManager = ImageCaptureManager(this)

        tl_custom.setNavigationIcon(R.mipmap.back_press)
        custom_toolbar_tv.text = getString(R.string.user_update)
        setupToolbar(tl_custom)

        var userInfo:UserInfoInner = intent!!.getParcelableExtra(Constant.USER_INFO)

        if (userInfo != null) {
            per_nickname.text = userInfo.nickname
            per_dovname.text = userInfo.loftname
            per_usercode.text = userInfo.userid
            per_phone.text = userInfo.telephone
            per_year.text = userInfo.experience + "年"
            if ("" != userInfo.gender) {
                when (userInfo.gender) {
                    "1", "男" -> persex.text = "男"
                    "2", "女" -> persex.text = "女"
                }
            }
            per_age.text = userInfo.age.toString() + "岁"
            if ("" != userInfo.headpic) {

                if (userInfo.headpic.startsWith("http")) {
                    Glide.with(this)
                            .load(userInfo.headpic)
                            .asBitmap()
                            .placeholder(R.mipmap.btn_img_photo_default)
                            .error(R.mipmap.btn_img_photo_default)
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                    per_civ.setImageBitmap(resource)
                                }
                            })
                } else {
                    if (userInfo.headpic != "") {
                        val byteArray = Base64.decode(userInfo.headpic, Base64.DEFAULT)
                        val byteArrayInputStream = ByteArrayInputStream(byteArray)
                        //第三步:利用ByteArrayInputStream生成Bitmap
                        val bitmap = BitmapFactory.decodeStream(byteArrayInputStream)
                        per_civ.setImageBitmap(bitmap)
                    }
                }
            }
        }

        clickView()
    }

    private fun clickView() {
        per_civ.setOnClickListener{ showHeadDialog() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUESTCODE_1 ->

                //没有授权

                if (!verifyPermissions(grantResults)) {

                    isNeedCheck = true
                } else {
                    takePhoto()
                    isNeedCheck = false
                }
            PERMISSION_REQUESTCODE_2 ->
                //没有授权

                if (!verifyPermissions(grantResults)) {

                    isNeedCheck = true
                } else {
                    choosePhoto()
                    isNeedCheck = false
                }
        }
    }

    fun  showHeadDialog(){
        val mDialog = Dialog(this, R.style.DialogTheme)
        mDialog.setCancelable(false)
        val perNull:ViewGroup? = null
        val view = layoutInflater.inflate(R.layout.personal_headciv_dialog, perNull)
        mDialog.setContentView(view, LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        view.headciv_cancle.setOnClickListener { mDialog.dismiss() }
        view.headciv_takephoto.setOnClickListener {
            tagMsg = "拍照权限"

            if (isNeedCheck) {
                if (!checkPermissions(this@PersonalActivity,PERMISSION_REQUESTCODE_1, needPermissions)) {
                    takePhoto()
                }
            } else {
                takePhoto()
            }
            mDialog.dismiss()
        }
        view.headciv_fromphoto.setOnClickListener {
            tagMsg = "相册权限"

            if (isNeedCheck) {
                if (!checkPermissions(this@PersonalActivity,PERMISSION_REQUESTCODE_2, needPermissions)) {
                    choosePhoto()
                }
            } else {
                choosePhoto()
            }
            mDialog.dismiss()
        }

       setDialogWindow(mDialog)
        mDialog.show()

    }

    private fun takePhoto() {

        try {
            val intent = captureManager!!.dispatchTakePictureIntent()
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun choosePhoto() {
        val innerIntent = Intent(Intent.ACTION_PICK, null)
        innerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        innerIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(innerIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        Log.e("requestCode", (resultCode == RESULT_OK).toString() + "-----")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
            //                case 100:
                ImageCaptureManager.REQUEST_TAKE_PHOTO -> {

                    captureManager!!.galleryAddPic()

                    val takePath = captureManager!!.currentPhotoPath

                    Log.e("takephoto", takePath + "------path")

                    updatePhotoPath = takePath
                    updatePamas = UPDATE_HEADPIC
                    methodType = MethodType.METHOD_TYPE_IMAGE_UPLOAD
                    mImagePresenter.getData(getParaMap2(),methodType)
                }

                200 ->

                    try {
                        Log.e("requestCode", (intent != null).toString() + "-----1")
                        if (intent != null) {
                            val uri1 = intent.data
                            val absolutePath = getAbsolutePath(this@PersonalActivity, uri1)
                            //setPicToView(absolutePath);
                            updatePhotoPath = absolutePath!!
                            updatePamas = UPDATE_HEADPIC

                            Log.e("requestCode", updatePhotoPath + "---1--1")
                            methodType = MethodType.METHOD_TYPE_IMAGE_UPLOAD
                            mImagePresenter.getData(getParaMap2(),methodType)
                        }
                    } catch (e: Exception) {
                        Log.e("requestCode",e.message+ "-----2")
                        e.printStackTrace()
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    override fun getMethod(): String {
        var method = ""
        when (methodType) {
            MethodType.METHOD_TYPE_IMAGE_UPLOAD -> method = MethodConstant.IMAGE_UPLOAD
            MethodType.METHOD_TYPE_USER_UPDATE -> method = MethodConstant.USER_INFO_UPDATE
        }
        return method
    }
    fun getParaMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(MethodParams.PARAMS_METHOD, getMethod())
        map.put(MethodParams.PARAMS_SIGN, getSign())
        map.put(MethodParams.PARAMS_TIME, getTime())
        map.put(MethodParams.PARAMS_VERSION, getVersion())
        map.put(MethodParams.PARAMS_TOKEN, getToken())
        map.put(MethodParams.PARAMS_USER_OBJ, getUserObjId())

        when (methodType) {
            MethodType.METHOD_TYPE_USER_UPDATE -> when (updatePamas) {
                UPDATE_BIRTH -> map.put(MethodParams.PARAMS_BIRTHDAY, updateBirth)
                UPDATE_GENDER -> map.put(MethodParams.PARAMS_GENDER, updateGender)
                UPDATE_NAME -> map.put(MethodParams.PARAMS_NICKNAME, updateName)
                UPDATE_HEADPIC -> map.put(MethodParams.PARAMS_HEADPIC, updateHeadpic)
                UPDATE_LOFTNAME -> map.put(MethodParams.PARAMS_LOFTNAME, updateLoftName)
                UPDATE_EXPE -> map.put(MethodParams.PARAMS_EXPER, updateExper)
            }
        }
        return map
    }

    fun getParaMap2(): HashMap<String, RequestBody> {

        val methodBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getMethod())
        val signBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getSign())
        val timeBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getTime())
        val versionBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getVersion())
        val objIdBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getUserObjId())
        val tokenBody:RequestBody = RequestBody.create(MediaType.parse("text/plain"), getToken())

        val map = HashMap<String, RequestBody>()
        map.put(MethodParams.PARAMS_METHOD, methodBody)
        map.put(MethodParams.PARAMS_SIGN, signBody)
        map.put(MethodParams.PARAMS_TIME, timeBody)
        map.put(MethodParams.PARAMS_VERSION, versionBody)
        map.put(MethodParams.PARAMS_USER_OBJ, objIdBody)
        map.put(MethodParams.PARAMS_TOKEN, tokenBody)
        val file = File(updatePhotoPath)

        val fileBody:RequestBody = RequestBody.create(MediaType.parse("image/png"), file)
        map.put("file\"; filename=\"" + file.name + "", fileBody)
        Log.e("requestCode", map.toString() + "---2--1")
        return map
    }
    override fun successToDo() {

        SpUtils.putBoolean(this,Constant.CHANGE_USER,true)
        when (updatePamas) {
            UPDATE_BIRTH -> {
            }
            UPDATE_GENDER -> {
                persex.text = if ("1" == updateGender) "男" else "女"
//                editor.putString("user_sex", if ("1" == updateGender) "男" else "女")
            }
            UPDATE_NAME -> {
                per_nickname.text = updateName
//                editor.putString("nick_name", updateName)
            }
            UPDATE_HEADPIC -> {
                setPicToView(updatePhotoPath)

//                editor.putString("user_headpic", updatePhotoPath)
            }
            UPDATE_LOFTNAME -> {
                per_dovname.text = updateLoftName
//                editor.putString("user_dovecote", updateLoftName)
            }
            UPDATE_EXPE -> {
                per_year.text = updateExper
//                editor.putString("user_year", updateExper)
            }
        }
    }

    override fun uploadImage(data: String) {
        Log.e("uoploadImage",data+"-------image")
        methodType = MethodType.METHOD_TYPE_USER_UPDATE
        updatePamas = UPDATE_HEADPIC
        updateHeadpic = data
        mCodesPresenter.getData(getParaMap(),methodType)
    }

    internal fun setPicToView(mPhotoPath: String) {

        Thread(Runnable {
            if (mPhotoPath.isEmpty()) {
                return@Runnable
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true//先获取原大小
            mScanBitmap = BitmapFactory.decodeFile(mPhotoPath, options)

            options.inJustDecodeBounds = false//获取新的大小
            var sampleSize = (options.outHeight / 200.toFloat()).toInt()
            if (sampleSize <= 0)
                sampleSize = 1
            options.inSampleSize = sampleSize
            mScanBitmap = BitmapFactory.decodeFile(mPhotoPath, options)

            runOnUiThread {
                per_civ.setImageBitmap(mScanBitmap)
                saveToPreference(mScanBitmap!!)
            }
        }).start()
    }

    private fun saveToPreference(src: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        src.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        val byteArray = byteArrayOutputStream.toByteArray()
        val imageString = Base64.encodeToString(byteArray, Base64.DEFAULT)

//        val editor = preferences.edit()
//        editor.putString("user_headpic", imageString)
//        editor.commit()
        SpUtils.putString(this, Constant.USER_HEADPIC,imageString)

    }
}