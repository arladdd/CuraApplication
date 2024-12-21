package project.CuraApp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var problemImage: ImageView
    private lateinit var problemTitle: TextView
    private lateinit var collectedAmount: TextView
    private lateinit var collectedFromText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var participantCount: TextView
    private lateinit var remainDays: TextView
    private lateinit var organizerImage: CircleImageView
    private lateinit var organizerName: TextView
    private lateinit var organizerVerified: TextView
    private lateinit var backersContainer: LinearLayout
    private lateinit var viewPeopleButton: Button
    private lateinit var storyText: TextView
    private lateinit var updatesText: TextView
    private lateinit var useOfFundsText: TextView
    private lateinit var contributeButton: Button
    private lateinit var donateButton: Button
    private var problemItem: ProblemItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(android.R.color.white, theme) // Warna putih untuk status bar
            window.navigationBarColor = resources.getColor(android.R.color.white, theme) // Warna putih untuk navigation bar
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or  // Teks status bar menjadi gelap
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Teks navigation bar menjadi gelap
                    )
        }

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize views
        initializeViews()

        // Load problem details
        loadProblemDetails()

        // Setup button clicks
        setupButtons()
    }

    private fun initializeViews() {
        problemImage = findViewById(R.id.problemImage)
        problemTitle = findViewById(R.id.problemTitle)
        collectedAmount = findViewById(R.id.collectedAmount)
        collectedFromText = findViewById(R.id.collectedFromText)
        progressBar = findViewById(R.id.progressBar)
        participantCount = findViewById(R.id.participantCount)
        remainDays = findViewById(R.id.remainDays)
        organizerImage = findViewById(R.id.organizerImage)
        organizerName = findViewById(R.id.organizerName)
        organizerVerified = findViewById(R.id.organizerVerified)
        backersContainer = findViewById(R.id.backersContainer)
        viewPeopleButton = findViewById(R.id.viewPeopleButton)
        storyText = findViewById(R.id.storyText)
        updatesText = findViewById(R.id.updatesText)
        useOfFundsText = findViewById(R.id.useOfFundsText)
        contributeButton = findViewById(R.id.contributeButton)
        donateButton = findViewById(R.id.donateButton)
    }

    private fun loadProblemDetails() {
        problemItem = intent.getParcelableExtra("PROBLEM_ITEM")

        problemItem?.let { problem ->
            problemImage.setImageResource(problem.imageResourceId)
            problemTitle.text = problem.title

            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            val collectedAmountFormatted = numberFormat.format(problem.price)
            val targetAmountFormatted = numberFormat.format(problem.targetPrice)

            collectedAmount.text = collectedAmountFormatted
            collectedFromText.text = "From $targetAmountFormatted"

            progressBar.progress = problem.progress

            participantCount.text = "${problem.currentViews}/${problem.maxViews}"
            remainDays.text = "${problem.date} ${problem.month}"

            organizerImage.setImageResource(problem.organizerImageResourceId)
            organizerName.text = problem.maker
            organizerVerified.visibility = if (problem.isVerified) View.VISIBLE else View.GONE

            // Add backers
            addBackers(problem.backers)

            storyText.text = problem.story

            // Set dummy text for updates and use of funds
            updatesText.text = "No updates yet"
            useOfFundsText.text = "Details about how the funds will be used will be posted here"
        }
    }

    private fun addBackers(backers: List<Int>) {
        backersContainer.removeAllViews()
        backers.take(5).forEachIndexed { index, backerId ->
            val backerImage = CircleImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.backer_image_size),
                    resources.getDimensionPixelSize(R.dimen.backer_image_size)
                ).apply {
                    if (index > 0) {
                        marginStart = -resources.getDimensionPixelSize(R.dimen.backer_image_overlap)
                    }
                }
                setImageResource(backerId)
                borderWidth = resources.getDimensionPixelSize(R.dimen.backer_image_border)
                borderColor = getColor(android.R.color.white)
            }
            backersContainer.addView(backerImage)
        }
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        findViewById<ImageButton>(R.id.mapButton).setOnClickListener {
            // Handle map button click
        }

        viewPeopleButton.setOnClickListener {
            // Handle view people button click
        }

        contributeButton.setOnClickListener {
            val intent = Intent(this, ContributeActivity::class.java)
            intent.putExtra("PROBLEM_ITEM", problemItem)
            startActivity(intent)
        }

        donateButton.setOnClickListener {
            val intent = Intent(this, DonateActivity::class.java)
            problemItem?.let {
                intent.putExtra("PROBLEM_ITEM", it)
            }
            startActivity(intent)
        }
    }

}

