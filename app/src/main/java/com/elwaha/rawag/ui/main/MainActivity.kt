package com.elwaha.rawag.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ObjectConverter
import com.elwaha.rawag.utilies.changeLanguage
import com.elwaha.rawag.utilies.toast


class MainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_main)

    }

}
