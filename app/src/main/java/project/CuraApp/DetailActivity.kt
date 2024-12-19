package project.CuraApp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var problemImage: ImageView
    private lateinit var problemTitle: TextView
    private lateinit var collectedAmount: TextView
    private lateinit var collectedFromText: TextView
    private lateinit var participantCount: TextView
    private lateinit var remainDays: TextView
    private lateinit var organizerName: TextView
    private lateinit var storyText: TextView


    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        problemImage = findViewById(R.id.problemImage)
        problemTitle = findViewById(R.id.problemTitle)
        collectedAmount = findViewById(R.id.collectedAmount)
        collectedFromText = findViewById(R.id.collectedFromText)
        participantCount = findViewById(R.id.participantCount)
        remainDays = findViewById(R.id.remainDays)
        organizerName = findViewById(R.id.organizerName)
        storyText = findViewById(R.id.storyText)

        val problemId = intent.getStringExtra("PROBLEM_ID")
        if (problemId.isNullOrEmpty()) {
            Log.e("DetailActivity", "Problem ID not found!")
            Toast.makeText(this, "Problem ID not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d("DetailActivity", "Received Problem ID: $problemId")
        fetchProblemDetails(problemId) // Now called safely
    }


    private fun fetchProblemDetails(problemId: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        // Use coroutine scope to handle the API request asynchronously
        coroutineScope.launch {
            try {
                val response: Response<DetailItem> = RetrofitInstance.api.getProblemDetails(problemId)
                if (response.isSuccessful) {
                    val problem = response.body()
                    if (problem != null) {
                        updateUI(problem) // Pass the problem to updateUI
                    } else {
                        Toast.makeText(this@DetailActivity, "Problem details not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("DetailActivity", "Response not successful: ${response.code()}")
                    Toast.makeText(this@DetailActivity, "Failed to load details!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DetailActivity, "Network error!", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }






    private fun updateUI(problem: DetailItem) {
        Glide.with(this).load(problem.imageUrl).into(problemImage)
        problemTitle.text = problem.title ?: "No Title"
        collectedAmount.text = "Rp ${problem.donationReceived ?: 0}"
        collectedFromText.text = "Out of Rp ${problem.donationNeeded ?: 0}"
        participantCount.text = "${problem.volunteerReceived ?: 0} participants"
        organizerName.text = problem.user?.name ?: "Anonymous"
        storyText.text = problem.description ?: "No description available"
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
