package com.elwaha.rawag.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elwaha.rawag.R
import com.elwaha.rawag.ui.main.MainActivity

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MainActivity.start(this)
        finish()
    }
}
