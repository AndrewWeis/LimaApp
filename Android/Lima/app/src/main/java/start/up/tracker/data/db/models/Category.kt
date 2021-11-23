package start.up.tracker.data.db.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import start.up.tracker.utils.DEFAULT_PROJECT_COLOR

@Parcelize
@Entity
data class Category(
    val categoryName: String,
    val color: Int = DEFAULT_PROJECT_COLOR,
    @PrimaryKey(autoGenerate = true)  val id: Int = 0
) : Parcelable