package start.up.tracker.analytics.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnalyticsMessages(
    val messages: List<AnalyticsMessage>
) : Parcelable
