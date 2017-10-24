package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.modules.dove.DoveListFragment
import com.gmax.kotlin_one.modules.dove.RingInfoActivity
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.DoveContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(DoveModule::class))
interface DoveComponent {

    fun inject(fragment: DoveListFragment)
    fun inject(activity:RingInfoActivity)
}

@Module(includes = arrayOf(ApiModule::class))
class DoveModule(private val view: DoveContract.View, private val codeView: CodesContract.View){

    @Provides fun getView() = view

    @Provides fun getCodesView() = codeView
}