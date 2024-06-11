package com.bullion.bulliontestcase.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bullion.bulliontestcase.databinding.ActivityDetailBinding
import com.bullion.bulliontestcase.ui.edit.EditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")

        viewModel.getDetail(userId.toString())
        viewModel.detail.observe(this) { userData ->
            if (userData != null) {
                binding.tvName.text = userName
                binding.tvEmail.text = userData.email
                binding.tvGenderUser.text = userData.gender
                binding.tvPhoneNumber.text = userData.phone

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

                val date = inputFormat.parse(userData.dateOfBirth)
                val formattedDate = outputFormat.format(date!!)

                binding.tvBirthDate.text = formattedDate
                binding.tvAddressUser.text = userData.address
            }
        }

        binding.btnEditUsers.setOnClickListener {
            goToEdit(userId, userName)
        }
    }

    private fun goToEdit(userData: String?, userName: String?) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("userId", userData)
        intent.putExtra("userName", userName)
        startActivity(intent)
    }

}