package com.example.mobile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
             //   R.id.navigation_create_ticket,
             //   R.id.navigation_list_tickets,
             //   R.id.navigation_list_and_approve_tickets,
             //   R.id.navigation_edit_budget,
             //   R.id.navigation_create_report,
             //   R.id.navigation_add_user,
             //   R.id.navigation_add_department,
                R.id.navigation_notifications
            )
        )

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun activateAdminNavigation()  {
        navView.menu.findItem(R.id.navigation_add_user).isEnabled = true
        navView.menu.findItem(R.id.navigation_add_department).isEnabled = true
        navController.navigate(R.id.navigation_add_user)
    }

    fun activateAccountantNavigation() {
        navView.menu.findItem(R.id.navigation_list_and_approve_tickets).isEnabled = true
        navView.menu.findItem(R.id.navigation_edit_budget).isEnabled = true
        navView.menu.findItem(R.id.navigation_create_report).isEnabled = true
        navView.menu.findItem(R.id.navigation_notifications).isEnabled = true
        navController.navigate(R.id.navigation_list_and_approve_tickets)

    }

    fun activateTeamMemberNavigation() {
        navView.menu.findItem(R.id.navigation_create_ticket).isEnabled = true
        navView.menu.findItem(R.id.navigation_list_tickets).isEnabled = true
        navView.menu.findItem(R.id.navigation_notifications).isEnabled = true
        navController.navigate(R.id.navigation_list_tickets)
    }

    fun activateManagerNavigation() {
        navView.menu.findItem(R.id.navigation_create_ticket).isEnabled = true
        navView.menu.findItem(R.id.navigation_list_and_approve_tickets).isEnabled = true
        navView.menu.findItem(R.id.navigation_notifications).isEnabled = true
        navController.navigate(R.id.navigation_list_and_approve_tickets)
    }
}