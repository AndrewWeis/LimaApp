package start.up.tracker.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import start.up.tracker.R
import start.up.tracker.databinding.HomeFragmentBinding
import start.up.tracker.entities.Project
import start.up.tracker.mvvm.view_models.home.HomeViewModel
import start.up.tracker.ui.data.entities.ListItem
import start.up.tracker.ui.data.entities.header.Header
import start.up.tracker.ui.data.entities.home.HomeSection
import start.up.tracker.ui.data.entities.home.ProjectsData
import start.up.tracker.ui.extensions.list.ListExtension
import start.up.tracker.ui.fragments.base.BaseNavigationFragment
import start.up.tracker.ui.list.adapters.home.HomeAdapter
import start.up.tracker.ui.list.generators.home.HomeGenerator
import start.up.tracker.ui.list.view_holders.home.HomeSectionViewHolder
import start.up.tracker.ui.list.view_holders.home.ProjectViewHolder
import start.up.tracker.utils.TimeHelper
import start.up.tracker.utils.resources.ResourcesUtils

@AndroidEntryPoint
class HomeFragment :
    BaseNavigationFragment(R.layout.home_fragment),
    HomeSectionViewHolder.OnHomeSectionClickListener,
    ProjectViewHolder.OnProjectClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private var binding: HomeFragmentBinding? = null

    private lateinit var adapter: HomeAdapter
    private var listExtension: ListExtension? = null
    private val generator = HomeGenerator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)

        initAdapter()
        initFabListener()
        initObservers()
        initEventsListener()

        viewModel.updateNumberOfTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listExtension = null
    }

    override fun onHomeSectionClick(listItem: ListItem) {
        viewModel.onHomeSectionClick(listItem)
    }

    override fun onProjectClick(project: Project) {
        viewModel.onProjectClicked(project)
    }

    private fun showProjectsHeader() {
        val header = Header(title = ResourcesUtils.getString(R.string.title_projects))
        val listItem: ListItem = generator.createHeaderListItem(header)

        if (binding?.homeList?.isComputingLayout == false) {
            adapter.setProjectsHeaderListItem(listItem)
            return
        }

        binding?.homeList?.post {
            adapter.setProjectsHeaderListItem(listItem)
        }
    }

    private fun showUpcomingSection(upcoming: HomeSection) {
        val listItem: ListItem = generator.createUpcomingSectionListItem(upcoming)

        if (binding?.homeList?.isComputingLayout == false) {
            adapter.setUpcomingListItem(listItem)
            return
        }

        binding?.homeList?.post {
            adapter.setUpcomingListItem(listItem)
        }
    }

    private fun showTodaySection(today: HomeSection) {
        val listItem: ListItem = generator.createTodaySectionListItem(today)

        if (binding?.homeList?.isComputingLayout == false) {
            adapter.setTodayListItem(listItem)
            return
        }

        binding?.homeList?.post {
            adapter.setTodayListItem(listItem)
        }
    }

    private fun showInboxSection(inbox: HomeSection) {
        val listItem: ListItem = generator.createInboxSectionListItem(inbox)

        if (binding?.homeList?.isComputingLayout == false) {
            adapter.setInboxListItem(listItem)
            return
        }

        binding?.homeList?.post {
            adapter.setInboxListItem(listItem)
        }
    }

    private fun showProjects(projectData: ProjectsData) {
        val listItem: ListItem = generator.createProjectsListItems(projectData)

        if (binding?.homeList?.isComputingLayout == false) {
            adapter.setProjectsListItem(listItem)
            return
        }

        binding?.homeList?.post {
            adapter.setProjectsListItem(listItem)
        }
    }

    private fun initFabListener() {
        binding?.addProjectFab?.setOnClickListener {
            viewModel.onAddProjectClick()
        }
    }

    private fun initObservers() {
        viewModel.projects.observe(viewLifecycleOwner) { projectData ->
            showProjectsHeader()
            showProjects(projectData)
        }

        viewModel.inboxSection.observe(viewLifecycleOwner) { inbox ->
            showInboxSection(inbox)
        }

        viewModel.todaySection.observe(viewLifecycleOwner) { today ->
            showTodaySection(today)
        }

        viewModel.upcomingSection.observe(viewLifecycleOwner) { upcoming ->
            showUpcomingSection(upcoming)
        }
    }

    private fun initAdapter() {
        adapter = HomeAdapter(
            layoutInflater = layoutInflater,
            viewModel = viewModel,
            onHomeSectionClickListener = this,
            onProjectClickListener = this
        )

        listExtension = ListExtension(binding?.homeList)
        listExtension?.setVerticalLayoutManager()
        listExtension?.setAdapter(adapter)
    }

    private fun initEventsListener() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.projectEvents.collect { event ->
            when (event) {
                is HomeViewModel.HomeEvents.NavigateToProject -> {
                    val action = HomeFragmentDirections.actionProjectToProjectTasks(
                        event.project.projectId,
                        event.project.projectTitle
                    )
                    navigateTo(action)
                }

                is HomeViewModel.HomeEvents.NavigateToAddProject -> {
                    val action = HomeFragmentDirections.actionProjectToAddProject()
                    navigateTo(action)
                }

                is HomeViewModel.HomeEvents.NavigateToToday -> {
                    val today = TimeHelper.getTodayAsString(TimeHelper.DateFormats.DD_MMM_EEEE)
                    val action = HomeFragmentDirections.actionProjectToToday(today)
                    navigateTo(action)
                }

                is HomeViewModel.HomeEvents.NavigateToUpcoming -> {
                    val action = HomeFragmentDirections.actionProjectToUpcoming()
                    navigateTo(action)
                }
            }
        }
    }
}
