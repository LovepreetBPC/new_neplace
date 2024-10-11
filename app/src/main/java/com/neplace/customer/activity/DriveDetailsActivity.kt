package com.neplace.customer.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityDriveDetailsBinding

class DriveDetailsActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var binding: ActivityDriveDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drive_details)

        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtContinue.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack -> {
                finish()
            }
       R.id.txtContinue -> {
           showDialog()
            }

        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.thanks_dialog_layout)
        val relativeOk = dialog.findViewById(R.id.relativeOk) as RelativeLayout

        relativeOk.setOnClickListener {
            startActivity(Intent(this, DriverAcceptanceActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()

    }
}