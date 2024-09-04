package com.example.neplacecustomer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.neplacecustomer.common.Constant
import com.example.neplacecustomer.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imgBack.setOnClickListener {
            finish()
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