package com.neplace.customer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.neplace.customer.common.Constant
import com.neplace.customer.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val screenSource = intent.getStringExtra("SCREEN_OPEN")


        binding.imgBack.setOnClickListener {
            if (screenSource == "PlanDetailActivity") {
                startActivity(Intent(this@PrivacyPolicyActivity, PlanDetailActivity::class.java))
                finish()
            }else{
                finish()
            }
        }
        binding.webview.settings.setJavaScriptEnabled(true)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
       // binding.webview.loadUrl("http://18.216.223.224/privacy-policy")
        binding.webview.loadUrl(Constant.BASEURL + "privacy-policy")

//        binding.webview.loadUrl("https://nightcoders.org/rumonradio/")
    }
}