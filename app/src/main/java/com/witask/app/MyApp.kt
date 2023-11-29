package com.witask.app

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class MyApp : Application() {
    companion object {
        lateinit var preferences: SharedPreferences
    }

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()

        preferences = getSharedPreferences("prefs",Context.MODE_PRIVATE)

    }
}