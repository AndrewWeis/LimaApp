package start.up.tracker.ui.addeditcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import start.up.tracker.R

class AddEditCategoryFragment : Fragment(R.layout.fragment_add_edit_category) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_category, container, false)
    }



}