package com.witask.app

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.witask.app.databinding.ActivityCoreBinding


class CoreActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCoreBinding


    public val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.READ_EXTERNAL_STORAGE),0)
        _binding = ActivityCoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_core)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,R.id.calendar_fragment, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener {
            //navController.clearBackStack(navController.currentDestination!!.id)
            navController.navigate(it.itemId)
            return@setOnItemSelectedListener true
        }

    }

}