package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.fragments.HomeFragment
import com.gmax.kotlin_one.modules.LoginActivity
import com.gmax.kotlin_one.modules.RegistActivity
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.LoginContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(LoginModule::class))
interface LoginComponent {

    fun inject(activity:LoginActivity)
    fun inject(activity:RegistActivity)
    fun inject(fragment:HomeFragment)
}

@Module(includes = arrayOf(ApiModule::class))
class LoginModule(private val view: LoginContract.View,private val codeView: CodesContract.View){
    @Provides fun getView() = view
    @Provides fun getCodesView() = codeView
}