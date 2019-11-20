package com.elwaha.rawag.utilies

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.splash.Splash
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val CHANNEL_ID = "com.elwaha.rawag"

open class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, Splash::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setTicker(remoteMessage.notification!!.body)
            .setWhen(System.currentTimeMillis())
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationId = System.currentTimeMillis().toInt()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.i("FireToken", p0)
        GlobalScope.launch {
            if (Injector.getPreferenceHelper().isLoggedIn) {
                Injector.getUserRepo().sendFirebaseToken(p0)
            }
        }
    }
}