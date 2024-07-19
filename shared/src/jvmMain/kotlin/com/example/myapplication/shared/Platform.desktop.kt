package com.example.myapplication.shared

import java.util.UUID


actual fun getPlatformName(): String = "Desktop ${System.getProperty("os.name")}"

actual fun getUUID():String = UUID.randomUUID().toString()
