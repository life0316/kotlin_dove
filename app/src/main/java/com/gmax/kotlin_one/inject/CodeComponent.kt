package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.modules.dove.AddDoveActivity
import com.gmax.kotlin_one.modules.dove.AddRingActivity
import com.gmax.kotlin_one.mvp.CodesContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(CodeModule::class))
interface CodeComponent {
    fun inject(activity: AddDoveActivity)
    fun inject(activity: AddRingActivity)
}

@Module(includes = arrayOf(ApiModule::class))
class CodeModule(private val codeView: CodesContract.View){
    @Provides fun getCodesView() = codeView
}