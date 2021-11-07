package start.up.tracker.ui.categoryInside

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import start.up.tracker.R
import start.up.tracker.databinding.FragmentCategoryInsideBinding

class CategoryInsideFragment : Fragment(R.layout.fragment_category_inside) {

    private val viewModel: CategoryInsideViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCategoryInsideBinding.bind(view)

        binding.apply {
            txtTest.text = viewModel.categoryName
        }
    }

}