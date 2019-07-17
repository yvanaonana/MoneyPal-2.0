package com.example.moneypal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.moneypal.fragment.CollationFragment
import com.hover.sdk.api.Hover
import com.hover.sdk.permissions.PermissionActivity
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
        startActivityForResult(Intent(this, PermissionActivity::class.java), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_fragment, fragment)
            .commit()
    }
}
