package com.example.moneypal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.moneypal.fragment.CollationFragment
import com.hover.sdk.api.Hover
import kotlinx.android.synthetic.main.activity_money_pal.*

class MoneyPalActivity : AppCompatActivity() {

    private val requestReadSMS :Int = 3

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(TransactionsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(CollationFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_pal)
        replaceFragment(TransactionsFragment())
        Hover.initialize(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), requestReadSMS)
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_fragment, fragment)
            .commit()
    }
}
