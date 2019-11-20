package com.elwaha.rawag.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.MainActivity
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.showMessageInDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseInstanceId.getInstance()
            .instanceId.addOnSuccessListener { instanceIdResult: InstanceIdResult? ->
            Log.i("FireToken", instanceIdResult!!.token)
            GlobalScope.launch {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    Injector.getUserRepo().sendFirebaseToken(instanceIdResult.token)
                }
            }
        }

        MainActivity.start(this)
        finish()
    }
}
