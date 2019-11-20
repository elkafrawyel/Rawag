package com.elwaha.rawag

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.Utils
import com.elwaha.rawag.utilies.CHANNEL_ID
import com.elwaha.rawag.utilies.changeLanguage

class MyApp  : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)
        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rawag"
            val descriptionText = "Rawag App"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }

}