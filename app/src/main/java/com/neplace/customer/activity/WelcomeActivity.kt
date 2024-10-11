package com.neplace.customer.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityWelcomeBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.LoginActivity
import com.neplace.customer.common.Constant

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