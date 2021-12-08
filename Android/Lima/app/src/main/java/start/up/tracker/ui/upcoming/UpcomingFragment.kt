package start.up.tracker.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import start.up.tracker.R
import start.up.tracker.databinding.FragmentUpcomingBinding

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private val viewModel: UpcomingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUpcomingBinding.bind(view)

        viewModel.upcomingTasks.observe(viewLifecycleOwner) {
            Log.i("upcoming", it.toString())
        }
    }
}