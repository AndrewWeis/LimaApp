package start.up.tracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import start.up.tracker.R
import start.up.tracker.data.sp.SharedPref
import start.up.tracker.databinding.ActivityMainBinding
import start.up.tracker.utils.Coroutines
import start.up.tracker.utils.UtilExtensions.myToast
import start.up.tracker.utils.UtilExtensions.openActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*companion object {
        const val TASK_DATA = "TASK_DATA"
    }

    private val viewModel by viewModels<TaskViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: TaskAdapter
    private lateinit var sharedPref: SharedPref
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        initAppTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
*/



/*
        noteAdapter = TaskAdapter {
            openActivity(AddTaskActivity::class.java) {
                putParcelable(TASK_DATA, it)
            }
        }

        initView()
        observeTasks()
        initBottomNavigation()
*/
    }

/*
    private fun initAppTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.TrackerTheme)
        }
    }

    private fun initBottomNavigation() {
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.category->openActivity(CategoryActivity::class.java)
                R.id.analytics->openActivity(AnalyticsActivity::class.java)
                R.id.settings->openActivity(SettingsActivity::class.java)
            }
            true
        }
    }


    private fun initView() {
        binding.addTaskFAB.setOnClickListener {
            openActivity(AddTaskActivity::class.java)
        }

        binding.taskRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }

        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(binding.taskRV)
    }

    private fun observeTasks() {
        Coroutines.main {
            viewModel.getAllTasks().observe(this@MainActivity, {
                noteAdapter.submitList(it)
            })
        }
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Coroutines.main {
                    val note = noteAdapter.getNoteAt(viewHolder.adapterPosition)
                    viewModel.deleteTask(note).also {
                        myToast(getString(R.string.success_delete))
                    }
                }
            }
        }
    }
*/

    /*private fun deleteAllTasks() {
        val dialog = AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
        dialog.setTitle(getString(R.string.clear_tasks))
            .setMessage(getString(R.string.sure_clear_task))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                Coroutines.main {
                    viewModel.deleteAllTasks().also {
                        myToast(getString(R.string.success_clear))
                    }
                }
            }.setNegativeButton(android.R.string.cancel, null).create().show()
    }*/

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearNoteItem -> deleteAllTasks()
        }
        return super.onOptionsItemSelected(item)
    }*/
}