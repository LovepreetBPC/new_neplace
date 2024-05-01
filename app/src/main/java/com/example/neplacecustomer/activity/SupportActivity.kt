package com.example.neplacecustomer.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivitySupportBinding
    var number="7018508085"
    private val requestCall = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()
    }

    private fun listeners() {
        binding.relativePhone.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.relativeGmailSupport.setOnClickListener(this)
        binding.relativeChat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.imgBack->{
                finish()
            }
            R.id.relativePhone->{
                makePhoneCall()
            }
            R.id.relativeChat->{
                startActivity(Intent(this,SupportChatActivity::class.java))
            }
            R.id.relativeGmailSupport->{
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("pk.parshar91@gmail.com")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...")
                intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...")
                intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
                intent.type = "text/html"
                intent.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(intent, "Send mail"))
            }
        }
    }
    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this@SupportActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@SupportActivity, arrayOf(Manifest.permission.CALL_PHONE), requestCall)
        } else {
            val dial = "tel:$number"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }

    }
}