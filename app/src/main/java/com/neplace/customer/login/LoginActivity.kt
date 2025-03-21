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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.neplace.customer.R
import com.neplace.customer.activity.DashboardActivity
import com.neplace.customer.activity.OtpActivity
import com.neplace.customer.activity.WebViewActivity
import com.neplace.customer.common.Constant
import com.neplace.customer.databinding.ActivityLoginBinding
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.LoginModel
import com.neplace.customer.viewmodel.LoginViewModel
import com.neplace.customer.viewmodel.SocialLoginViewModel
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

    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private  var RC_SIGN_IN = 1000


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





        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()



        googleApiClient = GoogleApiClient.Builder(this)
            .addApi<GoogleSignInOptions>(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()


        // Initialize sign in options the client-id is copied form google-services.json file
        /*val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("319811497841-imm66r5uo7f67tbs1dhktv07jvhl1f1i.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        // Initialize firebase user
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(
                Intent(
                    this@LoginActivity,
                    DashboardActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
*/

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



    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            val signInAccountTask: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            // check condition
            if (signInAccountTask.isSuccessful) {
                // When google sign in successful initialize string
                val s = "Google sign in successful"
                // Display Toast
                displayToast(s)
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    val googleSignInAccount = signInAccountTask.getResult(ApiException::class.java)
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken, null
                        )
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task ->
                                // Check condition
                                if (task.isSuccessful) {
                                    // When task is successful redirect to profile activity
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            DashboardActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                    // Display Toast
                                    displayToast("Firebase authentication successful")
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast(
                                        "Authentication Failed :" + task.exception?.message
                                    )
                                }
                            }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }*/


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
               /* val intent: Intent = googleSignInClient.signInIntent
                // Start activity for result
                startActivityForResult(intent, 100)*/
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
//        val signInIntent = mGoogleSignInClient?.signInIntent
//        startActivityForResult(signInIntent!!, REQUEST_CODE_GOOGLE_PLUS_LOGIN)
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Handle the sign-in result
//        if (requestCode == REQUEST_CODE_GOOGLE_PLUS_LOGIN) {
//
//            try {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                val googleSignInAccount = task.getResult(ApiException::class.java)
//                Log.d("EMAIL_SIGNUP", "googleSignInAccount 359: "+ googleSignInAccount)
//
//                // Google Sign-In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    socialLoginViewModel.loginUser(
//                        googleSignInAccount?.email ?: "",
//                        token,
//                        "android",
//                        "2",
//                        googleSignInAccount?.id ?: "0"
//                    )
//                    sharePref.saveString(Constant.USERNAME, googleSignInAccount?.displayName)
//                    sharePref.saveString(Constant.EMAIL, googleSignInAccount?.email)
//
//                    Log.d("EMAIL_SIGNUP", "onActivityResult 374: "+ account.email)
//                }
//            } catch (e: ApiException) {
//                // Google Sign-In failed, handle the exception
//                Log.e("social_login", "onActivityResult: " + e.message.toString())
//                // ...
//            }
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(
                data!!
            )
            if (result != null) {
                handleSignInResult(result)
            }
        }
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            val email = acct!!.email
            val name = acct.displayName
            val photo = acct.photoUrl
            val user_name = name!!.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val fname = user_name[0]
            val lname = user_name[1]


            acct!!.email?.let {
                socialLoginViewModel.loginUser(
                    it,
                    token,
                    "android",
                    "2",
                    acct.id.toString()
                )
            }
                    sharePref.saveString(Constant.USERNAME, acct.displayName)
                    sharePref.saveString(Constant.EMAIL, acct.email)
        }
    }
}