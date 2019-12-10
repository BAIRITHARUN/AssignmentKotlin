package com.infy.assignmentkotlin.main_activity.view.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.infy.assignmentkotlin.R
import com.infy.assignmentkotlin.main_activity.presenter.interfaces.IMainView
import com.infy.assignmentkotlin.main_activity.view.fragments.MainFragFragment

class MainActivity : AppCompatActivity(), IMainView {


    internal lateinit var mFLMain: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFLMain = findViewById(R.id.mFLMain)
        replaceFragment()
    }


    override fun replaceFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.mFLMain, MainFragFragment())
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mFLMain)
        if (fragment is MainFragFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}