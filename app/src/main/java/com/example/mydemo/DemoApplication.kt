package com.example.mydemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or services here
    }
}