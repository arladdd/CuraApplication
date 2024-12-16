package project.CuraApp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private var lastFragmentId: Int = R.id.navigation_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(android.R.color.white, theme) // Warna putih untuk status bar
            window.navigationBarColor = resources.getColor(android.R.color.white, theme) // Warna putih untuk navigation bar
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or  // Teks status bar menjadi gelap
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Teks navigation bar menjadi gelap
                    )
        }


        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_dashboard -> {
                    loadFragment(DashboardFragment())
                    lastFragmentId = item.itemId
                    true
                }
                R.id.navigation_complete -> {
                    loadFragment(CompleteFragment())
                    lastFragmentId = item.itemId
                    true
                }
                R.id.navigation_add_report -> {
                    startActivity(Intent(this, ReportActivity::class.java))
                    false
                }
                R.id.navigation_award -> {
                    loadFragment(AwardFragment())
                    lastFragmentId = item.itemId
                    true
                }
                R.id.navigation_leaderboard -> {
                    loadFragment(LeaderboardFragment())
                    lastFragmentId = item.itemId
                    true
                }
                else -> false
            }
        }

        // Load default fragment
        loadFragment(DashboardFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // Ensure the correct navigation item is selected when returning from ReportActivity
        bottomNavigation.selectedItemId = lastFragmentId
    }
}

