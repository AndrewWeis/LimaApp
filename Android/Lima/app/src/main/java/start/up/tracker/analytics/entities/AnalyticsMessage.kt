package start.up.tracker.analytics.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnalyticsMessage(
    var principleId: Int,
    var title: String,
    var error: String,
    var hint: String
) : Parcelable
