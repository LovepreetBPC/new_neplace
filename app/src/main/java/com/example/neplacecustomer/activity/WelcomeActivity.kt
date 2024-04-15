package com.example.neplacecustomer.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityWelcomeBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.LoginActivity
import com.nexter.application.common.Constant

class WelcomeActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.txtGetStart.visibility = View.GONE

        setOnClick()
    }

    private fun setOnClick() {
        binding.txtGetStart.setOnClickListener(this)
        // Use a Handler to delay the launch of the main activity
        binding.txtGetStart.visibility = View.VISIBLE
        if (sharePref.getString(Constant.TOKEN, "").equals("")) {
            Log.e("data", "setOnClick: Please wait" )
        }else{
            binding.txtGetStart.visibility = View.GONE

            Handler().postDelayed({
                // Start the main activity

                if (sharePref.getString(Constant.TOKEN, "").equals("")) {
                    binding.txtGetStart.visibility = View.VISIBLE
                    passIntentWithFinish(this, LoginActivity::class.java, "")

                } else {
                    binding.txtGetStart.visibility = View.GONE
                    passIntentWithFinish(this, DashboardActivity::class.java, "")



                }

                // Finish the splash activity
                finish()
            }, 2000 )
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txtGetStart -> {

                if (sharePref.getString(Constant.TOKEN, "").equals("")) {
                    passIntentWithFinish(this, LoginActivity::class.java, "")

                } else {
                    passIntentWithFinish(this, DashboardActivity::class.java, "")

                }
                Log.e("token", "onClick: Token ->  "+sharePref.getString(Constant.TOKEN, ""))
            }
        }
    }
}