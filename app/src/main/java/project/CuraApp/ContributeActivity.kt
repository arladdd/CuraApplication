package project.CuraApp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ContributeActivity : AppCompatActivity() {

    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var ageRangeGroup: RadioGroup
    private lateinit var uploadContainer: FrameLayout
    private lateinit var termsCheckbox: CheckBox
    private lateinit var contributeButton: Button

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contribute)

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

        // Setup spinner
        setupGenderSpinner()

        // Setup click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        genderSpinner = findViewById(R.id.genderSpinner)
        ageRangeGroup = findViewById(R.id.ageRangeGroup)
        uploadContainer = findViewById(R.id.uploadContainer)
        termsCheckbox = findViewById(R.id.termsCheckbox)
        contributeButton = findViewById(R.id.contributeButton)
    }

    private fun setupGenderSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        uploadContainer.setOnClickListener {
            openImagePicker()
        }

        contributeButton.setOnClickListener {
            if (validateForm()) {
                submitForm()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            // You might want to show a preview of the selected image
            Toast.makeText(this, "ID Card uploaded successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        if (fullNameInput.text.isNullOrBlank()) {
            fullNameInput.error = "Please enter your full name"
            return false
        }

        if (emailInput.text.isNullOrBlank()) {
            emailInput.error = "Please enter your email"
            return false
        }

        if (phoneInput.text.isNullOrBlank()) {
            phoneInput.error = "Please enter your phone number"
            return false
        }

        if (genderSpinner.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
            return false
        }

        if (ageRangeGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select your age range", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please upload your ID Card", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Please accept the terms", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun submitForm() {
        // Handle form submission
        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}

