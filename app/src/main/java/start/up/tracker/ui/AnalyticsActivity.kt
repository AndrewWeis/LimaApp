package start.up.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import start.up.tracker.R
import start.up.tracker.utils.UtilExtensions.openActivity

class AnalyticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        bottomNavigationView.selectedItemId = R.id.analytics
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->openActivity(MainActivity::class.java)
                R.id.category->openActivity(CategoryActivity::class.java)
                R.id.settings->openActivity(SettingsActivity::class.java)
            }
            true
        }
    }
}