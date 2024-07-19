package com.example.myapplication.shared

import platform.Foundation.NSUUID.Companion.UUID
import platform.UIKit.UIDevice

actual fun getPlatformName(): String =
    "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"


actual fun getUUID():String = UUID().UUIDString