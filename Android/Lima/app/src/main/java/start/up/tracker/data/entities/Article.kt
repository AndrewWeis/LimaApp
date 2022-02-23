package start.up.tracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val body: String,
    val timeToRead: String
)
