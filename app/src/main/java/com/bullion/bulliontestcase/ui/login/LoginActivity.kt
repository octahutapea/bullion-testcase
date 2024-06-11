package com.bullion.bulliontestcase.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bullion.bulliontestcase.data.remote.request.LoginRequestBody
import com.bullion.bulliontestcase.databinding.ActivityLoginBinding
import com.bullion.bulliontestcase.helper.PasswordEncryption
import com.bullion.bulliontestcase.ui.main.MainActivity
import com.bullion.bulliontestcase.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.login) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.isLogin.observe(this) { isLogin ->
            if (isLogin) {
                goToMain()
            }
        }

        binding.btnAddNewUsers.setOnClickListener {
            goToRegister()
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val encyptedPassword = PasswordEncryption.encryptPassword(password)

            val success = validateFields(email, password)

            if (success) {
                val loginRequestBody = LoginRequestBody(email, encyptedPassword)

                viewModel.postLogin(loginRequestBody) { loggedIn ->
                    if (loggedIn) {
                        goToMain()
                    }
                }

            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateFields(email: String, password: String): Boolean {

        if (email.isBlank()) {
            binding.layoutEmail.helperText = "Email is required"
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.helperText = "Please enter a valid email address"
            return false
        }
        else if (password.isBlank()) {
            binding.layoutPassword.helperText = "Password is required"
            return false
        }
        else if (password.length < 8) {
            binding.layoutPassword.helperText = "Password must be at least 8 characters"
            return false
        }
        else {
            return true
        }
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}