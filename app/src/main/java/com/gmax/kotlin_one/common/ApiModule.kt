package com.gmax.kotlin_one.common

import android.content.Context
import com.gmax.kotlin_one.Constant
import com.gmax.kotlin_one.retrofit.IOurNewService
import com.gmax.kotlin_one.utils.RxBus
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

@Module
class ApiModule(private val context: Context) {
    @Provides fun provideContext() = context
    @Provides fun provideRxbus() = RxBus.getInstance()
    //    @Provides fun provideRetrofit() = RetrofitManager.builder()
    @Provides fun provideGson() = GsonBuilder().create()
    @Provides fun provideBaseUrl() = HttpUrl.parse(Constant.BASE_OUR_URL_2)
    @Provides fun provideOkhttp(context: Context):OkHttpClient{
        val cacheSize = 1024 * 1024 * 10L
        val cacheDir = File(context.cacheDir, "http")
        val cache = Cache(cacheDir, cacheSize)
        return OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(100,TimeUnit.SECONDS)
                .build()
    }

    @Provides fun provideRetrofit(baseUrl: HttpUrl, client: OkHttpClient, gson: Gson) =
            Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

    @Provides fun provideIOurNewService(retrofit:Retrofit) = retrofit.create(IOurNewService::class.java)
}