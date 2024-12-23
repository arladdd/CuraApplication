package project.CuraApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var uploadPhotoButton: TextView
    private lateinit var idCardPreview: ImageView
    private lateinit var registerButton: TextView
    private lateinit var signInButton: TextView

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton)
        idCardPreview = findViewById(R.id.idCardPreview)
        registerButton = findViewById(R.id.registerButton)
        signInButton = findViewById(R.id.signInButton)
    }

    private fun setupClickListeners() {
        uploadPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        registerButton.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(fullName, email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        signInButton.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(fullName: String, email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.registerApi.postRegister(RegisterRequest(fullName, email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    withContext(Dispatchers.Main) {
                        if (token != null) {
                            saveTokenToLocal(token)
                            Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorMessage = parseErrorResponse(response.errorBody()?.string())
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseErrorResponse(errorBody: String?): String {
        return try {
            // Parse JSON error response
            val jsonObject = JSONObject(errorBody ?: "{}")
            jsonObject.optString("message", "Unknown error occurred")
        } catch (e: JSONException) {
            "Failed to parse error message"
        }
    }

    private fun saveTokenToLocal(token: String) {
        val sharedPreferences = getSharedPreferences("CuraAppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            idCardPreview.setImageURI(selectedImage)
            idCardPreview.visibility = ImageView.VISIBLE
        }
    }
}
