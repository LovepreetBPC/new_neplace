package com.neplace.customer.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.neplace.customer.R
import com.neplace.customer.common.Constant
import com.neplace.customer.databinding.ActivityTermsAndConditionsBinding


class TermsAndConditionsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTermsAndConditionsBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            finish()
        }


        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        //binding.webview.loadUrl("http://18.216.223.224/terms")
        binding.webview.loadUrl("https://app.goneplace.app/terms")
    }
}