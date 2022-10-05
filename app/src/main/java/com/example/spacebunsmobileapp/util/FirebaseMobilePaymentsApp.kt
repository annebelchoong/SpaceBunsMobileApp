package com.example.spacebunsmobileapp.util

import android.app.Application
//import com.example.spacebunsmobileapp.BuildConfig.PublishableKey
//import com.stripe.android.BuildConfig
import com.stripe.android.PaymentConfiguration

class FirebaseMobilePaymentsApp : Application() {

    private val publishableKey: String = "pk_test_51LWbMdCffsYKlOgZJQu5OlKPoEe978IKUuzKJjwcW0ZDwb46LaVT09D3svdGTvaWoavzzCTqo9fM7DztaOL8dSGu00MhmVJabg"

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext,"pk_test_51LWbMdCffsYKlOgZJQu5OlKPoEe978IKUuzKJjwcW0ZDwb46LaVT09D3svdGTvaWoavzzCTqo9fM7DztaOL8dSGu00MhmVJabg");
//        PaymentConfiguration.init(applicationContext, BuildConfig.PublishableKey)
//        PaymentConfiguration.init(applicationContext, publishableKey)
    }
}