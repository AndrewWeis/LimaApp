package start.up.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import start.up.tracker.R
import start.up.tracker.utils.UtilExtensions.openActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        bottomNavigationView.selectedItemId = R.id.settings
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->openActivity(MainActivity::class.java)
                R.id.category->openActivity(CategoryActivity::class.java)
                R.id.analytics->openActivity(AnalyticsActivity::class.java)
            }
            true
        }
    }
}