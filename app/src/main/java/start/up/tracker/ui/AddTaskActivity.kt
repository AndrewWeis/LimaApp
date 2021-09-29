package start.up.tracker.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.data.db.Task
import start.up.tracker.data.sp.SharedPref
import start.up.tracker.databinding.ActivityAddTaskBinding
import start.up.tracker.utils.Coroutines
import start.up.tracker.utils.UtilExtensions.myToast
import start.up.tracker.utils.UtilExtensions.setTextEditable


@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {
    private val viewModel by viewModels<TaskViewModel>()
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var sharedPref: SharedPref

    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppTheme()
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        task = intent.extras?.getParcelable(MainActivity.TASK_DATA)

        initToolbar()
        initView()
        initClick()
    }

    private fun initAppTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.TrackerTheme)
        }
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initView() {
        if (task != null) { //this is for set data to form and update data
            binding.titleET.setTextEditable(task?.label ?: "")
        }
    }

    private fun initClick() {
        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val id = if (task != null) task?.id else null
        val label = binding.titleET.text.toString().trim()


        if (label.isEmpty()) {
            myToast(getString(R.string.form_empty))
            return
        }

        val task = Task(id = id, label = label)
        Coroutines.main {
            if (id != null) { //for update note
                viewModel.updateTask(task).also {
                    myToast(getString(R.string.success_update))
                    finish()
                }
            } else { //for insert note
                viewModel.addTask(task).also {
                    myToast(getString(R.string.success_save))
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}