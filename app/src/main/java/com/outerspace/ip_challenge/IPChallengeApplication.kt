package com.outerspace.ip_challenge

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class IPChallengeApplication: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appCtx: Context
        fun appContext() = appCtx
    }

    override fun onCreate() {
        super.onCreate()
        appCtx = applicationContext
    }

}