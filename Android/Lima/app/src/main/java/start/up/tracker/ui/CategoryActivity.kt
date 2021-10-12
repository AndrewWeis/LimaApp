package start.up.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import start.up.tracker.R
import start.up.tracker.data.sp.SharedPref
import start.up.tracker.utils.UtilExtensions.openActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppTheme()
        setContentView(R.layout.activity_category)



      //  initBottomNavigation()
    }

    private fun initAppTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.TrackerTheme)
        }
    }

    /*private fun initBottomNavigation() {
        bottomNavigationView.selectedItemId = R.id.category
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->openActivity(MainActivity::class.java)
                R.id.analytics->openActivity(AnalyticsActivity::class.java)
                R.id.settings->openActivity(SettingsActivity::class.java)
            }
            true
        }
    }*/
}