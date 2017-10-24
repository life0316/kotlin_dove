package com.gmax.kotlin_one.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription

open class BasePresenter {

    var compositeSubscription = CompositeSubscription()

    protected fun addSubscription(subscription: Subscription){
        compositeSubscription.add(subscription)
    }

    fun unSubscribe(){
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }
    }
}