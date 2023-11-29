package com.witask.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val user = getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("profile",null)
        if(user!=null) {
            startActivity(Intent(applicationContext,CoreActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object {
        lateinit var callback: (s: String)->Unit
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG","Result $data $requestCode")
        if (data!=null) {
            Log.d("TAG","CALL")
            callback(data.data.toString())
            contentResolver.takePersistableUriPermission(
                data.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }
}