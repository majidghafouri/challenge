package com.getyourguide.challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.facebook.stetho.Stetho

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_review)
    Stetho.initializeWithDefaults(this)
    setUpNavigation()
  }

  private fun setUpNavigation() {
    val navController = findNavController(R.id.my_nav_host_fragment)
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
  }

}
