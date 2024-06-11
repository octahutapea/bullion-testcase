package com.bullion.bulliontestcase.ui.main

import ListUserAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.databinding.ActivityMainBinding
import com.bullion.bulliontestcase.ui.login.LoginActivity
import com.bullion.bulliontestcase.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvUsers.layoutManager = layoutManager

        val adapter = ListUserAdapter(this)
        binding.rvUsers.adapter = adapter

        viewModel.users.observe(this) { users ->
            adapter.submitList(users)
        }

        binding.logout.setOnClickListener {
            viewModel.logout() { success ->
                if (success) {
                    goToLogin()
                }
            }
        }

        binding.btnAddNewUsers.setOnClickListener {
            goToRegister()
        }

    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}