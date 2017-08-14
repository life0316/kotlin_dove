package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.fragments.RingListFragment
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.RingContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(RingModule::class))
interface RingComponent {
    fun inject(fragment:RingListFragment)
}

@Module(includes = arrayOf(ApiModule::class))
class RingModule(private val view: RingContract.View, private val codeView: CodesContract.View){

    @Provides fun getView() = view

    @Provides fun getCodesView() = codeView
}