package com.bullion.bulliontestcase.ui.edit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bullion.bulliontestcase.R
import com.bullion.bulliontestcase.data.remote.request.EditRequestBody
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.databinding.ActivityEditBinding
import com.bullion.bulliontestcase.databinding.ActivityRegisterBinding
import com.bullion.bulliontestcase.helper.PasswordEncryption
import com.bullion.bulliontestcase.ui.login.LoginActivity
import com.bullion.bulliontestcase.ui.main.MainActivity
import com.bullion.bulliontestcase.ui.register.RegisterViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val viewModel: EditViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.edit) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val userId = intent.getStringExtra("userId")

        viewModel.getDetail(userId.toString())
        viewModel.detail.observe(this) { userData ->
            val name = splitFullName(userData.name)
            binding.etFirstName.setText(name.first)
            binding.etLastName.setText(name.second)
            when (userData.gender) {
                "male" -> {
                    binding.cbMale.isChecked = true
                }
                "female" -> {
                    binding.cbFemale.isChecked = true
                }
                else -> {
                    binding.cbMale.isChecked = true
                    binding.cbFemale.isChecked = true
                }
            }
            binding.etDateOfBirth.setText(userData.dateOfBirth)
            binding.etEmail.setText(userData.email)
            binding.etPhoneNumber.setText(userData.phone)
            binding.etAddress.setText(userData.address)
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

        binding.btnUpdateUser.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val genderMale = binding.cbMale
            val genderFemale = binding.cbFemale
            val dateOfBirth = binding.etDateOfBirth.text.toString()
            val email = binding.etEmail.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()

            val success = validateFields(firstName, lastName, genderMale, genderFemale, dateOfBirth, email, phoneNumber, address)

            val gender: String = when {
                genderMale.isChecked -> "male"
                genderFemale.isChecked -> "female"
                else -> "male/female"
            }

            if (success) {
                val editRequestBody = EditRequestBody(firstName, lastName, gender, dateOfBirth, email, phoneNumber, address)
                if (userId != null) {
                    viewModel.postEdit(userId, editRequestBody) { isEdited ->
                        if (isEdited) {
                            Toast.makeText(applicationContext, "Register success", Toast.LENGTH_LONG).show()
                            goToMain()
                        }
                    }
                }
            }
        }
    }

    fun splitFullName(fullName: String): Pair<String, String> {
        val parts = fullName.split(" ")
        if (parts.size == 1) {
            return Pair(parts[0], "")
        }
        val firstName = parts.subList(0, parts.size - 1).joinToString(" ")
        val lastName = parts.last()
        return Pair(firstName, lastName)
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
    ): Boolean {

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
        else {
            return true
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}