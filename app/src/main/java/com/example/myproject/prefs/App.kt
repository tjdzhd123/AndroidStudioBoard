package com.example.myproject.prefs

import android.app.Application
import com.example.practiceapi.prefs.Prefs

class App: Application() {
    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}