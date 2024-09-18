package com.example.neplacecustomer.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivitySupportBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.LoginActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.viewmodel.DeleteAccountViewModel

class SupportActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding:ActivitySupportBinding
    var number="7018508085"
    private val requestCall = 1
    lateinit var deleteviewModel: DeleteAccountViewModel
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userID").toString()
        listeners()
    }

    private fun listeners() {
        binding.relativePhone.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.relativeGmailSupport.setOnClickListener(this)
        binding.relativeChat.setOnClickListener(this)
        binding.txtDeleteAccount.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.txtDeleteAccount -> {
                deleteAlertDialog("Are you sure you want to delete Account?", "Alert", "delete")
            }

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

    private fun deleteAlertDialog(alertMsg: String, title: String, from: String) {
            val builder1 = AlertDialog.Builder(this)
            builder1.setTitle(title)
            builder1.setMessage(alertMsg)
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                "YES"
            ) { dialog, id ->

                deleteAccont()

                dialog.dismiss()
            }

            builder1.setNegativeButton(
                "NO") { dialog, id ->
                dialog.dismiss()
            }

            // Set the button styles
            val alertDialog = builder1.create()
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.white))
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(android.R.color.white))
            }

            alertDialog.show()
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this@SupportActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@SupportActivity, arrayOf(Manifest.permission.CALL_PHONE), requestCall)
        } else {
            val dial = "tel:$number"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }

    }

    private fun deleteAccont() {
        deleteviewModel = ViewModelProvider(this)[DeleteAccountViewModel::class.java]

        deleteviewModel.deleteAccount(userId)
        deleteviewModel.successReponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()

                    if(it.data?.status == true){
                        sharePref.clear()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "something went wrong!!", Toast.LENGTH_SHORT).show()
                    }
                }

                is BaseResponse.Error -> {
                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }
    }

}