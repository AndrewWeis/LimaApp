package start.up.tracker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technique_table")
data class Technique(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val body: String,
    val timeToRead: String
)
