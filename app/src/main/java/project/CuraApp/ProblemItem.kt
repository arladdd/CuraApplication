package project.CuraApp

import java.io.Serializable
import project.CuraApp.User

data class ProblemItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val donationNeeded: Double,
    val volunteerNeeded: Int,
    val donationReceived: Double,
    val volunteerReceived: Int,
    val user: User? = null
):Serializable {

    fun donationPercentage(): Int {
        return if (donationNeeded > 0) {
            ((donationReceived / donationNeeded) * 100).toInt()
        } else {
            0
        }
    }

}

