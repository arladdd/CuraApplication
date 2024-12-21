package project.CuraApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
            // TODO: Implement registration logic
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        signInButton.setOnClickListener {
            finish()
        }
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

