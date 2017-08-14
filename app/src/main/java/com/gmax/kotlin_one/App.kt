package com.gmax.kotlin_one

import android.app.Application
import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.common.DaggerApiCompoent
import com.gmax.kotlin_one.retrofit.RetrofitManager
import javax.inject.Inject
import kotlin.properties.*

@Suppress("DEPRECATION")
class App : Application(){

    companion object{
        var instance : App by Delegates.notNull()
    }

    @Inject lateinit var apiCompoent:ApiCompoent

    override fun onCreate() {
        super.onCreate()
        instance = this

        RetrofitManager.initRetrofit()

        DaggerApiCompoent.builder()
                .apiModule(ApiModule(this))
                .build().inject(this)
    }

}