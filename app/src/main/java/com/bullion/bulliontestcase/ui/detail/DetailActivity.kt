package com.bullion.bulliontestcase.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bullion.bulliontestcase.databinding.ActivityDetailBinding
import com.bullion.bulliontestcase.ui.edit.EditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")

        viewModel.getDetail(userId.toString())
        viewModel.detail.observe(this) { userData ->
            if (userData != null) {
                binding.tvName.text = userData.name
                binding.tvEmail.text = userData.email
                binding.tvGenderUser.text = userData.gender
                binding.tvPhoneNumber.text = userData.phone
                binding.tvBirthDate.text = userData.dateOfBirth
                binding.tvAddressUser.text = userData.address
            }
        }

        binding.btnEditUsers.setOnClickListener {
            goToEdit(userId)
        }
    }

    private fun goToEdit(userData: String?) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("userItem", userData)
        startActivity(intent)
    }

}