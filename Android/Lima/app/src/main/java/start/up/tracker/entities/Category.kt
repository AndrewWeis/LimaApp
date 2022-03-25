package start.up.tracker.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import start.up.tracker.ui.data.constants.DEFAULT_PROJECT_COLOR

@Parcelize
@Entity(tableName = "categories_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val color: Int = DEFAULT_PROJECT_COLOR,
    val tasksInside: Int = 0,
) : Parcelable