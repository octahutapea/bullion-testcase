package com.bullion.bulliontestcase.ui.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bullion.bulliontestcase.R
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.databinding.ActivityRegisterBinding
import com.bullion.bulliontestcase.helper.PasswordEncryption
import com.bullion.bulliontestcase.ui.login.LoginActivity
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.register) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_24)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.etDateOfBirth.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()

            builder.setTitleText("Select Date of Birth")
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())

            val picker = builder.build()
            picker.show(supportFragmentManager, picker.toString())

            picker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val formattedDate = dateFormatter.format(selection)

                binding.etDateOfBirth.setText(formattedDate)
            }
        }

        binding.etPhotoProfile.setOnClickListener {
            startGallery()
        }

        binding.btnAddNewUsers.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val genderMale = binding.cbMale
            val genderFemale = binding.cbFemale
            val dateOfBirth = binding.etDateOfBirth.text.toString()
            val email = binding.etEmail.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()
            val photoProfile = binding.etPhotoProfile.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val success = validateFields(firstName, lastName, genderMale, genderFemale, dateOfBirth, email, phoneNumber, address, photoProfile, password, confirmPassword)

            val gender: String = when {
                genderMale.isChecked -> "male"
                genderFemale.isChecked -> "female"
                else -> "male/female"
            }

            val encryptedPassword = PasswordEncryption.encryptPassword(password)

            if (success) {

                viewModel.postRegister(firstName, lastName, gender, dateOfBirth, email, phoneNumber, address, photoProfile.toUri(), encryptedPassword, applicationContext) { isRegister ->
                    if (isRegister) {
                        Toast.makeText(applicationContext, "Register success", Toast.LENGTH_LONG).show()
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val mimeType = contentResolver.getType(uri!!)

        if (mimeType == "image/jpeg" || mimeType == "image/jpg") {
            val fileSizeInMB = getFileSizeFromUri(uri) / (1024 * 1024)
            val maxSizeInMB = 5

            if (fileSizeInMB <= maxSizeInMB) {
                binding.etPhotoProfile.setText(uri.toString())
            } else {
                Toast.makeText(applicationContext, "Ukuran file terlalu besar. Maksimal 5 MB.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Format file harus JPEG.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFileSizeFromUri(uri: Uri): Long {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val fileSize = inputStream?.available()?.toLong() ?: -1
            inputStream?.close()
            fileSize
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun validateFields(
        firstName: String,
        lastName: String,
        genderMale: CheckBox,
        genderFemale: CheckBox,
        dateOfBirth: String,
        email: String,
        phoneNumber: String,
        address: String,
        photoProfile: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$"

        if (firstName.isBlank()) {
            binding.layoutFirstName.helperText = "First name is required"
            return false
        }
        else if (lastName.isBlank()) {
            binding.layoutLastName.helperText = "Last name is required"
            return false
        }
        else if (!genderMale.isChecked && !genderFemale.isChecked) {
            Toast.makeText(applicationContext, "Gender is required", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (dateOfBirth.isBlank()) {
            binding.layoutDateOfBirth.helperText = "Date of Birth is required"
            return false
        }
        else if (email.isBlank()) {
            binding.layoutEmail.helperText = "Email is required"
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.helperText = "Please enter a valid email address"
            return false
        }
        else if (phoneNumber.isBlank()) {
            binding.layoutPhoneNumber.helperText = "Phone number is required"
            return false
        }
        else if (address.isBlank()) {
            binding.layoutAddress.helperText = "Address is required"
            return false
        }
        else if (photoProfile.isBlank()) {
            binding.layoutPhotoProfile.helperText = "Photo profile is required"
            return false
        }
        else if (password.isBlank()) {
            binding.layoutPassword.helperText = "Password is required"
            return false
        }
        else if (!password.matches(passwordPattern.toRegex())) {
            binding.layoutPassword.helperText = "Password must be at least 8 characters, have alphabet, number, and capital letter"
            return false
        }
        else if (confirmPassword.isBlank()) {
            binding.layoutConfirmPassword.helperText = "Password is required"
            return false
        }
        else if (confirmPassword != password) {
            binding.layoutConfirmPassword.helperText = "Password is different"
            return false
        }
        else {
            return true
        }
    }

}