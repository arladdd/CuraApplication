package project.CuraApp

data class DetailItem(
    val _id: String?,
    val title: String?,
    val imageUrl: String?,
    val description: String?,
    val user: User?,
    val location: String?,
    val donationNeeded: Int?,
    val volunteerNeeded: Int?,
    val donationReceived: Int?,
    val volunteerReceived: Int?,
    val volunteerUser: List<String>?,
    val donationUser: List<String>?,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
) {
    fun donationPercentage(): Int {
        return if (donationNeeded != null && donationNeeded > 0) {
            ((donationReceived?.toDouble() ?: 0.0) / donationNeeded * 100).toInt()
        } else {
            0
        }
    }
}
