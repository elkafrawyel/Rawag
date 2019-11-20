package com.elwaha.rawag.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.elwaha.rawag.R
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ObjectConverter
import com.elwaha.rawag.utilies.changeLanguage
import com.elwaha.rawag.utilies.showMessageInDialog


class MainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        if (mainViewModel.categoriesList.isEmpty())
            mainViewModel.get(MainViewModel.MainActions.CATEGORIES)

    }
}
