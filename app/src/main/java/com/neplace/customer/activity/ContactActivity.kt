package com.neplace.customer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity()  {

    lateinit var binding: ActivityContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.txtUpdate.setOnClickListener {

            if (binding.edtDec.text.toString().isEmpty()){
                Toast.makeText(this, "Please Enter message ", Toast.LENGTH_SHORT).show()
            }else{
                finish()

            }

        }
    }
}