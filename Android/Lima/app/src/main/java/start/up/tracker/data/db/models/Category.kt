package start.up.tracker.data.db.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Category(
    val categoryName: String,
    @PrimaryKey(autoGenerate = true)  val id: Int = 0
) : Parcelable