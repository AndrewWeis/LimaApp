
package start.up.tracker.ui
/*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import start.up.tracker.R
import start.up.tracker.data.sp.SharedPref
import start.up.tracker.databinding.ActivitySettingsBinding
import start.up.tracker.utils.UtilExtensions.openActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppTheme()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpSwitch()
       // initBottomNavigation()
    }

    private fun setUpSwitch() {
        if (sharedPref.loadNightModeState()) {
            binding.switchMode.isChecked = true
        }

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref.setNightModeState(true)
                restartApp()
            } else {
                sharedPref.setNightModeState(false)
                restartApp()
            }
        }
    }

    private fun restartApp() {
        val i = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun initAppTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.TrackerTheme)
        }
    }

    */
/*private fun initBottomNavigation() {
        bottomNavigationView.selectedItemId = R.id.settings
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->openActivity(MainActivity::class.java)
                R.id.category->openActivity(CategoryActivity::class.java)
                R.id.analytics->openActivity(AnalyticsActivity::class.java)
            }
            true
        }
    }*//*

}*/
