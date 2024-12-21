package project.CuraApp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var problems = mutableListOf<ProblemItem>()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

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

        setupToolbar()
        setupRecyclerView()
        loadProblems()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "History"

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        toolbar.setTitleTextColor(Color.BLACK)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = HistoryAdapter(
            problems = problems,
            onUploadClick = { position ->
                openImagePicker(position)
            },
            onMarkCompleteClick = { position ->
                markProblemAsComplete(position)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadProblems() {
        // Sample data - In a real app, this would come from a database or API
        problems.add(
            ProblemItem(
                id = 1,
                imageResourceId = R.drawable.contoh,
                date = "17",
                month = "Dec",
                title = "DIMANAKAH DIA?! Setelah Viral dimanakah sosok gus mifta yang mengolok tukang es teh?",
                maker = "Arafi Laksmana",
                isVerified = true,
                price = 1000,
                targetPrice = 5000,
                currentViews = 27,
                maxViews = 50,
                progress = 20,
                organizerImageResourceId = R.drawable.gibran,
                backers = listOf(R.drawable.gibran, R.drawable.gibran),
                story = "Sample story text",
                isCompleted = false,
                activityPhotos = mutableListOf()
            )
        )
        adapter.notifyDataSetChanged()
    }

    private fun openImagePicker(position: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra("position", position)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                // In a real app, you would upload this image to your server
                // For now, we'll just update the UI
                val position = data.getIntExtra("position", -1)
                if (position != -1) {
                    problems[position].activityPhotos.add(imageUri.toString())
                    adapter.notifyItemChanged(position)
                    Toast.makeText(this, "Activity photo uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun markProblemAsComplete(position: Int) {
        problems[position].isCompleted = true
        // In a real app, you would update this in your database
        Toast.makeText(this, "Problem marked as complete", Toast.LENGTH_SHORT).show()
        adapter.notifyItemChanged(position)
    }
}

