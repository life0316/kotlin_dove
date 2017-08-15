package com.gmax.kotlin_one.inject

import com.gmax.kotlin_one.common.ApiCompoent
import com.gmax.kotlin_one.common.ApiModule
import com.gmax.kotlin_one.modules.home.PersonalActivity
import com.gmax.kotlin_one.mvp.CodesContract
import com.gmax.kotlin_one.mvp.ImageContract
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(dependencies = arrayOf(ApiCompoent::class),modules = arrayOf(ImageModule::class))
interface ImageComponent {
    fun inject(activity: PersonalActivity)
}

@Module(includes = arrayOf(ApiModule::class))
class ImageModule(private val imageView: ImageContract.ImageView,private val codeView: CodesContract.View){
    @Provides fun getCodesView() = codeView
    @Provides fun getImageView() = imageView
}