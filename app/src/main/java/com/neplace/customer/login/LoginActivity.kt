package com.neplace.customer.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.neplace.customer.R
import com.neplace.customer.activity.DashboardActivity
import com.neplace.customer.activity.OtpActivity
import com.neplace.customer.activity.WebViewActivity
import com.neplace.customer.databinding.ActivityLoginBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.LoginModel
import com.neplace.customer.viewmodel.LoginViewModel
import com.neplace.customer.viewmodel.SocialLoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.neplace.customer.common.Constant
import java.util.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var socialLoginViewModel: SocialLoginViewModel

    var token = ""
    var countyCode = "1"
    var phoneNumber = ""

    var REQUEST_CODE_GOOGLE_PLUS_LOGIN = 100
    private lateinit var googleApiClient: GoogleApiClient


    private val mGoogleSignInClient: GoogleSignInClient? by lazy {
        // Configure sign-in to request the user's ID, email address, and basic
        // user. ID and basic user are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        // New Google SignIn Client
        GoogleSignIn.getClient(this, gso)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

//        countyCode = binding.countryCode.selectedCountryCode
        binding.countryCode.isEnabled = false
        initViewss()
        setOnClick()


        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                token = task.result
                Log.d("firebaseTOKEN", "init: " + token)
            })

        }catch (e:Exception){
            Log.e("firebaseTOKEN", "onCreate: "+e.message)
        }

        /*try{
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {

                    return@OnCompleteListener
                }

                // Get new FCM registration token
                token = task.result

                Log.d("firebaseTOKEN", "init: " + token)
            })

        }*/


    }

    private fun initViewss() {
        socialLoginViewModel = ViewModelProvider(this)[SocialLoginViewModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.edtPhoneNumber.inputType = InputType.TYPE_CLASS_NUMBER

        loginViewModel.loginResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }

        socialLoginViewModel.loginResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()

                    if (it.data!!.status){
                        ToastMsg(it.data!!.message.toString())
                        passIntentToNextActivity(this, DashboardActivity::class.java, phoneNumber)
                        finish()
                        sharePref.saveString(Constant.TOKEN, it.data.data.token)
                        sharePref.saveString(Constant.USERNAME, it.data.data.name)
                    }else{
                        ToastMsg(it.data!!.message.toString())

                    }

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }

    }

    private fun convertPhoneNumberFormat(phoneNumber: String): String {
        // Remove all non-digit characters from the phone number
        val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")

        return digitsOnly
    }

    private fun Validation(): Boolean {

        if (binding.edtPhoneNumber.text.toString() == "") {
            ToastMsg("Enter phone number")
            return false
        }
        return true
    }

    private fun processError(msg: String?) {
        ToastMsg(msg.toString())
    }

    private fun processLogin(model: LoginModel?) {

        if (model!!.statusCode == 200) {

            ToastMsg(model.data.sms.toString())
            passIntentToNextActivity(this, OtpActivity::class.java, phoneNumber)
        } else {

            ToastMsg(model.message.toString())

        }

    }

    private fun setOnClick() {
        binding.txtContinue.setOnClickListener(this)
        binding.txtFindAccount.setOnClickListener(this)
        binding.relativeGoogle.setOnClickListener(this)

    }

    private fun openWebView(url: String) {
        val webViewIntent = Intent(this, WebViewActivity::class.java)
        webViewIntent.putExtra("url", url)
        startActivity(webViewIntent)
    }




    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txtContinue -> {
//                countyCode = binding.countryCode.selectedCountryCode

                if (Validation()) {
                    val convertedNumber = convertPhoneNumberFormat(binding.edtPhoneNumber.text.toString())
                    loginViewModel.loginUser("+$countyCode$convertedNumber")

                    phoneNumber = "+$countyCode$convertedNumber"
                    Log.e("phone_number", "onClick: " + countyCode + binding.edtPhoneNumber.text.toString())
                }

            }

            R.id.relativeGoogle -> {
                doGooglePlusLogin()
            }

            R.id.txtFindAccount -> {
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.find_account_bottom_sheet, null)
                val btnClose = view.findViewById<TextView>(R.id.txtYes)
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.setCancelable(false)
                dialog.setContentView(view)
                dialog.show()
            }
        }
    }


    fun doGooglePlusLogin() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent!!, REQUEST_CODE_GOOGLE_PLUS_LOGIN)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle the sign-in result
        if (requestCode == REQUEST_CODE_GOOGLE_PLUS_LOGIN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val googleSignInAccount = task.getResult(ApiException::class.java)

                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    socialLoginViewModel.loginUser(
                        googleSignInAccount?.email ?: "",
                        token,
                        "android",
                        "2",
                        googleSignInAccount?.id ?: "0"
                    )
                }
            } catch (e: ApiException) {
                // Google Sign-In failed, handle the exception
                Log.e("social_login", "onActivityResult: " + e.message.toString())
                // ...
            }
        }
    }


}