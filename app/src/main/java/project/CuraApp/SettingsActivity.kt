package project.CuraApp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View

class SettingsActivity : AppCompatActivity() {

    private lateinit var profilePhotoImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var changePhotoButton: Button

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Settings"

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        toolbar.setTitleTextColor(Color.BLACK)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        profilePhotoImageView = findViewById(R.id.profile_photo)
        nameTextView = findViewById(R.id.name_text_view)
        changePhotoButton = findViewById(R.id.change_photo_button)

        // Set user name (you should replace this with the actual user's name)
        nameTextView.text = "Zarif Melet"

        changePhotoButton.setOnClickListener {
            openImageChooser()
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                profilePhotoImageView.setImageURI(selectedImageUri)
                // Here you would typically save the new image URI to your user's profile
                // For example: saveProfilePhotoUri(selectedImageUri)
            }
        }
    }

    // This is a placeholder function. In a real app, you'd implement this to save the photo URI.
    private fun saveProfilePhotoUri(uri: Uri) {
        // Save the URI to SharedPreferences or your backend
    }
}

