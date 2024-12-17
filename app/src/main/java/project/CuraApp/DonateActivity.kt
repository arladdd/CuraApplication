package project.CuraApp

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.util.*

class DonateActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var cardNumberInput: EditText
    private lateinit var expiryInput: EditText
    private lateinit var cvcInput: EditText
    private lateinit var anonymousCheckbox: CheckBox
    private lateinit var donateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

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

        // Setup input formatting
        setupInputFormatting()

        // Setup button clicks
        setupButtons()
    }

    private fun initializeViews() {
        amountInput = findViewById(R.id.amountInput)
        cardNumberInput = findViewById(R.id.cardNumberInput)
        expiryInput = findViewById(R.id.expiryInput)
        cvcInput = findViewById(R.id.cvcInput)
        anonymousCheckbox = findViewById(R.id.anonymousCheckbox)
        donateButton = findViewById(R.id.donateButton)
    }

    private fun setupInputFormatting() {
        // Format amount input
        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                try {
                    val amount = s.toString().replace(".", "").toLongOrNull() ?: 0
                    if (amount < 1000) {
                        amountInput.error = "Minimum donation is Rp1.000"
                    }
                } catch (e: Exception) {
                    amountInput.error = "Invalid amount"
                }
            }
        })

        // Format expiry date input (MM/YY)
        expiryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && !s.toString().contains("/")) {
                    val input = s.toString().replace("/", "")
                    if (input.length >= 2) {
                        val month = input.substring(0, 2)
                        val year = if (input.length > 2) input.substring(2) else ""
                        val formatted = "$month/$year"
                        if (formatted != s.toString()) {
                            expiryInput.setText(formatted)
                            expiryInput.setSelection(formatted.length)
                        }
                    }
                }
            }
        })
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        donateButton.setOnClickListener {
            if (validateForm()) {
                processDonation()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate amount
        val amount = amountInput.text.toString().replace(".", "").toLongOrNull() ?: 0
        if (amount < 1000) {
            amountInput.error = "Minimum donation is Rp1.000"
            isValid = false
        }

        // Validate card number
        if (cardNumberInput.text.length != 16) {
            cardNumberInput.error = "Please enter a valid card number"
            isValid = false
        }

        // Validate expiry date
        val expiry = expiryInput.text.toString()
        if (!expiry.matches(Regex("^(0[1-9]|1[0-2])/([0-9]{2})\$"))) {
            expiryInput.error = "Please enter a valid expiry date (MM/YY)"
            isValid = false
        }

        // Validate CVC
        if (cvcInput.text.length != 3) {
            cvcInput.error = "Please enter a valid CVC"
            isValid = false
        }

        return isValid
    }

    private fun processDonation() {
        // Handle donation process
        Toast.makeText(this, "Processing donation...", Toast.LENGTH_SHORT).show()
        // Add your payment processing logic here
        finish()
    }
}

