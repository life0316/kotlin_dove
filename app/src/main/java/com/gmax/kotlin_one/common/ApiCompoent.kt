package com.gmax.kotlin_one.common

import com.gmax.kotlin_one.App
import dagger.Component

@Component(modules = arrayOf(ApiModule::class))
interface ApiCompoent{

    fun inject(app: App)
}