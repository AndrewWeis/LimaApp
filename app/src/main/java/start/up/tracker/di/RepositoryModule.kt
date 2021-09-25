package start.up.tracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import start.up.tracker.data.db.TaskDao
import start.up.tracker.data.repository.TaskRepositoryImpl

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun providesTaskRepository(noteDao: TaskDao) = TaskRepositoryImpl(noteDao)
}