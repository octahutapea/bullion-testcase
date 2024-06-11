package com.bullion.bulliontestcase.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.databinding.ActivityDetailBinding
import com.bullion.bulliontestcase.ui.edit.EditActivity
import com.bullion.bulliontestcase.ui.register.RegisterActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userItem", UserItem::class.java)
        } else {
            intent.getParcelableExtra<UserItem>("userItem")
        }

        if (userData != null) {
            binding.tvName.text = userData.name
            binding.tvEmail.text = userData.email
            binding.tvGenderUser.text = userData.gender
            binding.tvPhoneNumber.text = userData.phone
            binding.tvBirthDate.text = userData.dateOfBirth
            binding.tvAddressUser.text = userData.address
        }

        binding.btnEditUsers.setOnClickListener {
            goToEdit(userData)
        }
    }

    private fun goToEdit(userData: UserItem?) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("userItem", userData)
        startActivity(intent)
    }

}