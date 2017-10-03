package com.colavo.android.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.colavo.android.App


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(MainActivity())
       val intent = Intent(this, MainActivity::class.java)
       startActivity(intent)

    }
}
