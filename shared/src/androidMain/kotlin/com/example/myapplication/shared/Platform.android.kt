package com.example.myapplication.shared

import java.util.UUID

actual fun getPlatformName(): String = "Android ${android.os.Build.VERSION.SDK_INT}"


actual fun getUUID():String = UUID.randomUUID().toString()