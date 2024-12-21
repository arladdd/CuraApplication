package project.CuraApp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var volunteerInput: EditText
    private lateinit var campaignDate: TextView
    private lateinit var expiredDate: TextView
    private lateinit var donationInput: EditText
    private lateinit var fundsDetailsInput: EditText
    private lateinit var uploadedImage: ImageView
    private lateinit var uploadPlaceholder: ImageView
    private lateinit var submitButton: Button

    private var selectedImageUri: Uri? = null
    private val campaignCalendar = Calendar.getInstance()
    private val expiredCalendar = Calendar.getInstance()
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

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

        // Setup click listeners
        setupClickListeners()

        // Setup input formatting
        setupInputFormatting()
    }

    private fun initializeViews() {
        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        volunteerInput = findViewById(R.id.volunteerInput)
        campaignDate = findViewById(R.id.campaignDate)
        expiredDate = findViewById(R.id.expiredDate)
        donationInput = findViewById(R.id.donationInput)
        fundsDetailsInput = findViewById(R.id.fundsDetailsInput)
        uploadedImage = findViewById(R.id.uploadedImage)
        uploadPlaceholder = findViewById(R.id.uploadPlaceholder)
        submitButton = findViewById(R.id.submitButton)

        // Set initial dates
        val today = Calendar.getInstance()
        expiredCalendar.time = today.time
        campaignCalendar.time = today.time
        updateExpiredDateLabel()
        updateCampaignDateLabel()
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        findViewById<View>(R.id.campaignDateButton).setOnClickListener {
            showCampaignDatePicker()
        }

        findViewById<View>(R.id.expiredDateButton).setOnClickListener {
            showExpiredDatePicker()
        }

        findViewById<View>(R.id.uploadPhotoButton).setOnClickListener {
            openImagePicker()
        }

        submitButton.setOnClickListener {
            if (validateForm()) {
                submitCampaign()
            }
        }
    }

    private fun setupInputFormatting() {
        // Format donation input
        donationInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                try {
                    val amount = s.toString().replace(".", "").toLongOrNull() ?: 0
                    if (amount < 500000) {
                        donationInput.error = "Minimum donation is Rp500.000"
                    }
                } catch (e: Exception) {
                    donationInput.error = "Invalid amount"
                }
            }
        })

        // Format volunteer input
        volunteerInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    try {
                        val value = s.toString().toInt()
                        if (value < 0) {
                            volunteerInput.setText("0")
                            volunteerInput.setSelection(volunteerInput.text.length)
                        }
                    } catch (e: NumberFormatException) {
                        volunteerInput.setText("0")
                        volunteerInput.setSelection(volunteerInput.text.length)
                    }
                }
            }
        })
    }

    private fun showCampaignDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                if (!selectedDate.before(expiredCalendar)) {
                    campaignCalendar.set(Calendar.YEAR, year)
                    campaignCalendar.set(Calendar.MONTH, month)
                    campaignCalendar.set(Calendar.DAY_OF_MONTH, day)
                    updateCampaignDateLabel()
                } else {
                    Toast.makeText(this, "Tanggal kampanye tidak boleh sebelum tanggal kadaluarsa", Toast.LENGTH_SHORT).show()
                }
            },
            campaignCalendar.get(Calendar.YEAR),
            campaignCalendar.get(Calendar.MONTH),
            campaignCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = expiredCalendar.timeInMillis
        datePickerDialog.show()
    }

    private fun showExpiredDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                expiredCalendar.set(Calendar.YEAR, year)
                expiredCalendar.set(Calendar.MONTH, month)
                expiredCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateExpiredDateLabel()

                // Reset campaign date if it's before the new expired date
                if (campaignCalendar.before(expiredCalendar)) {
                    campaignCalendar.time = expiredCalendar.time
                    updateCampaignDateLabel()
                }
            },
            expiredCalendar.get(Calendar.YEAR),
            expiredCalendar.get(Calendar.MONTH),
            expiredCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun updateCampaignDateLabel() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        campaignDate.text = dateFormat.format(campaignCalendar.time)
    }

    private fun updateExpiredDateLabel() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        expiredDate.text = dateFormat.format(expiredCalendar.time)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            uploadedImage.setImageURI(selectedImageUri)
            uploadedImage.visibility = View.VISIBLE
            uploadPlaceholder.visibility = View.GONE
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (titleInput.text.isNullOrBlank()) {
            titleInput.error = "Please enter a title"
            isValid = false
        }

        if (descriptionInput.text.isNullOrBlank()) {
            descriptionInput.error = "Please enter a description"
            isValid = false
        }

        if (fundsDetailsInput.text.isNullOrBlank()) {
            fundsDetailsInput.error = "Please enter details of use of funds"
            isValid = false
        }

        val amount = donationInput.text.toString().replace(".", "").toLongOrNull() ?: 0
        if (amount < 500000) {
            donationInput.error = "Minimum donation is Rp500.000"
            isValid = false
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please upload a photo", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun submitCampaign() {
        // Handle campaign submission
        Toast.makeText(this, "Submitting campaign...", Toast.LENGTH_SHORT).show()
        // Add your submission logic here
        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}

