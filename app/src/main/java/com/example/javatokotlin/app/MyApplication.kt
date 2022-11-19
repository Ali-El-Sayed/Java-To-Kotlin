package com.example.javatokotlin.app

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // should only be done once when app starts
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .build()
        Realm.setDefaultConfiguration(config)
    }
}