package project.CuraApp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProblemItem(
    val id: Int,
    val imageResourceId: Int,
    val date: String,
    val month: String,
    val title: String,
    val maker: String,
    val isVerified: Boolean,
    val price: Long,
    val targetPrice: Long,
    val currentViews: Int,
    val maxViews: Int,
    val progress: Int,
    val organizerImageResourceId: Int,
    val backers: List<Int>,
    val story: String,
    var isCompleted: Boolean = false,
    val activityPhotos: MutableList<String> = mutableListOf()
) : Parcelable

