package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.modules.circle.AllCircleFragment
import com.gmax.kotlin_one.modules.circle.FriendCircleFragment
import com.gmax.kotlin_one.modules.circle.MyCircleFragment
import com.gmax.kotlin_one.mvp.CircleContract
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.EachCircleContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(CircleModule::class))
interface CircleComponent {
    fun inject(fragment: AllCircleFragment)
    fun inject(fragment: FriendCircleFragment)
    fun inject(fragment: MyCircleFragment)
}

@Module(includes = arrayOf(ApiModule::class))
class CircleModule(private val view: CircleContract.View,
                   private val codeView: CodesContract.View,
                   private val eachView: EachCircleContract.View){

    @Provides fun getView() = view
    @Provides fun getCodesView() = codeView
    @Provides fun getEachView() = eachView
}
