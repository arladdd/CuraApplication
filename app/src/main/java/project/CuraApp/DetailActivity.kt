package project.CuraApp

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var problemImage: ImageView
    private lateinit var priceText: TextView
    private lateinit var targetText: TextView
    private lateinit var progressBar: android.widget.ProgressBar
    private lateinit var organizerImage: CircleImageView
    private lateinit var organizerName: TextView
    private lateinit var verifiedBadge: TextView
    private lateinit var backersContainer: LinearLayout
    private lateinit var storyText: TextView
    private lateinit var bookmarkButton: MaterialButton
    private lateinit var donateButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Initialize views
        initializeViews()

        // Load problem details
        loadProblemDetails()

        // Setup button clicks
        setupButtons()
    }

    private fun initializeViews() {
        problemImage = findViewById(R.id.problemImage)
        priceText = findViewById(R.id.priceText)
        targetText = findViewById(R.id.targetText)
        progressBar = findViewById(R.id.progressBar)
        organizerImage = findViewById(R.id.organizerImage)
        organizerName = findViewById(R.id.organizerName)
        verifiedBadge = findViewById(R.id.verifiedBadge)
        backersContainer = findViewById(R.id.backersContainer)
        storyText = findViewById(R.id.storyText)
        bookmarkButton = findViewById(R.id.bookmarkButton)
        donateButton = findViewById(R.id.donateButton)
    }

    private fun loadProblemDetails() {
        val problemItem = intent.getParcelableExtra<ProblemItem>("PROBLEM_ITEM")

        problemItem?.let { problem ->
            problemImage.setImageResource(problem.imageResourceId)

            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

            priceText.text = numberFormat.format(problem.price)
            targetText.text = "from ${numberFormat.format(problem.targetPrice)}"

            progressBar.progress = problem.progress

            organizerImage.setImageResource(problem.organizerImageResourceId)
            organizerName.text = problem.maker
            verifiedBadge.visibility = if (problem.isVerified) android.view.View.VISIBLE else android.view.View.GONE

            // Add backers
            addBackers(problem.backers)

            storyText.text = problem.story
        }
    }

    private fun addBackers(backers: List<Int>) {
        backersContainer.removeAllViews() // Clear existing views
        backers.forEachIndexed { index, backerId ->
            val backerImage = CircleImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.backer_image_size),
                    resources.getDimensionPixelSize(R.dimen.backer_image_size)
                ).apply {
                    marginStart = if (index > 0) resources.getDimensionPixelSize(R.dimen.backer_image_margin) else 0
                }
                setImageResource(backerId)
            }
            backersContainer.addView(backerImage)
        }
    }

    private fun setupButtons() {
        bookmarkButton.setOnClickListener {
            // Handle bookmark action
        }

        donateButton.setOnClickListener {
            // Handle donate action
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

