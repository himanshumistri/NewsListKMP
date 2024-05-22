package com.example.myapplication.android

import android.app.Application
import com.example.myapplication.shared.di.initKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}