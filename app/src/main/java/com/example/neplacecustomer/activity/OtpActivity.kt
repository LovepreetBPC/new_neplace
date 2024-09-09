package com.example.neplacecustomer.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityOtpBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.LoginModel
import com.example.neplacecustomer.model.VerifyOtpModel
import com.example.neplacecustomer.viewmodel.LoginViewModel
import com.example.neplacecustomer.viewmodel.VerifyOtpViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.example.neplacecustomer.common.Constant
import com.google.firebase.FirebaseApp

class OtpActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityOtpBinding
    lateinit var loginViewModel: LoginViewModel

    lateinit var verifyOtpViewModel: VerifyOtpViewModel
    var device_token = ""
    var phoneNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        phoneNumber = intent.getStringExtra("extra").toString()
        binding.txtPhone.text = phoneNumber
        initViewss()
        setOnClick()

        FirebaseApp.initializeApp(this)

        Log.e("phoneNumber", "onCreate: $phoneNumber")
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000 // Convert milliseconds to minutes
                val seconds = (millisUntilFinished % 60000) / 1000 // Remaining seconds
                binding.txtResend.visibility = View.GONE

                val timeString = String.format("%02d:%02d", minutes, seconds)
                binding.txtVerifyOTP.text = "I didn't receive a code: $timeString"
            }

            // Callback function, fired when the time is up
            override fun onFinish() {
                binding.txtResend.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun setOnClick() {
        binding.txtVerifyOTP.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.txtSndOtp.setOnClickListener(this)
        binding.txtResend.setOnClickListener(this)


        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                device_token = task.result

                Log.e("TAG", "init: " + device_token)
            })

        }catch (e:Exception){
            Log.e("TAG_FirebaseOTP", "setOnClick catch: "+e.message)
        }


        binding.edtOtp1.addTextChangedListener(GenericTextWatcher(binding.edtOtp1, binding.edtOtp2, this))
        binding.edtOtp2.addTextChangedListener(GenericTextWatcher(binding.edtOtp2, binding.edtOtp3, this))
        binding.edtOtp3.addTextChangedListener(GenericTextWatcher(binding.edtOtp3, binding.edtOtp4, this))
//        binding.edtOtp4.addTextChangedListener(
//            GenericTextWatcher(binding.edtOtp4, binding.edtOtp4, this)
//        )

        binding.edtOtp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                // Check if edtOtp4 is fully filled
                if (s.toString().length == 1) {

                    val otp = binding.edtOtp1.text.toString() + binding.edtOtp2.text.toString() + binding.edtOtp3.text.toString() + binding.edtOtp4.text.toString()
                    verifyOtpViewModel.verifyOtpUser(phoneNumber, otp, device_token)

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.edtOtp4.windowToken, 0)
                }
            }
        })


        //      GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        binding.edtOtp1.setOnKeyListener(GenericKeyEvent(binding.edtOtp1, null))
        binding.edtOtp2.setOnKeyListener(GenericKeyEvent(binding.edtOtp2, binding.edtOtp1))
        binding.edtOtp3.setOnKeyListener(GenericKeyEvent(binding.edtOtp3, binding.edtOtp2))
        binding.edtOtp4.setOnKeyListener(GenericKeyEvent(binding.edtOtp4, binding.edtOtp3))


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }

            R.id.txtSndOtp -> {
                finish()
//                if (Validation()) {
//                    val otp = binding.edtOtp1.text.toString() + binding.edtOtp2.text.toString() + binding.edtOtp3.text.toString() + binding.edtOtp4.text.toString()
//                    verifyOtpViewModel.verifyOtpUser(phoneNumber, otp, device_token)
//                }
            }

            R.id.txtResend -> {

                loginViewModel.loginUser(phoneNumber)

            }
        }
    }

    private fun processLogin(model: LoginModel?) {

        if (model!!.statusCode == 200) {
            ToastMsg(model.data.sms.toString())

            object : CountDownTimer(30000, 1000) {

                // Callback function, fired on regular interval
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = millisUntilFinished / 60000 // Convert milliseconds to minutes
                    val seconds = (millisUntilFinished % 60000) / 1000 // Remaining seconds
                    binding.txtResend.visibility = View.GONE

                    val timeString = String.format("%02d:%02d", minutes, seconds)
                    binding.txtVerifyOTP.text = "I didn't receive a code: $timeString"
                }

                // Callback function, fired when the time is up
                override fun onFinish() {
                    binding.txtResend.visibility = View.VISIBLE
                }
            }.start()

//            passIntentToNextActivity(this, OtpActivity::class.java, phoneNumber)
//            finish()
        } else {

            ToastMsg(model.message.toString())

        }

    }


    private fun initViewss() {
        verifyOtpViewModel = ViewModelProvider(this)[VerifyOtpViewModel::class.java]


        verifyOtpViewModel.VerifyOtpResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status){
                        processLogin(it.data)

                    }else{
                        ToastMsg(it.data.message)
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

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]


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

    }


    class GenericKeyEvent internal constructor(
        private val currentView: EditText, private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.edtOtp1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: View, private val nextView: View?, private val activity: Activity
    ) : TextWatcher
    {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.edtOtp1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.edtOtp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.edtOtp3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.edtOtp4 -> if (text.length == 1) {
                    closesKeyboard(currentView)


                }
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) { // TODO Auto-generated method stub
        }

        private fun closesKeyboard(view: View?) {
            if (view != null) {
                val inputMethodManager =
                    activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)


            }

        }

    }


    private fun Validation(): Boolean {

        if (binding.edtOtp1.text.toString() == "") {
            ToastMsg("Enter valid otp")
            return false
        }
        if (binding.edtOtp2.text.toString() == "") {
            ToastMsg("Enter valid otp")
            return false
        }
        if (binding.edtOtp3.text.toString() == "") {
            ToastMsg("Enter valid otp")
            return false
        }
        if (binding.edtOtp4.text.toString() == "") {
            ToastMsg("Enter valid otp")
            return false
        }


        return true
    }

    private fun processError(msg: String?) {
        ToastMsg(msg.toString())
    }

    private fun processLogin(model: VerifyOtpModel?) {

        if (model != null) {
            if (model.status) {
                if (model.data.token!!.isNotEmpty()) {
                    val user = model.data.user
                    ToastMsg(model.message)
                    sharePref.saveString(Constant.TOKEN, model.data.token)
                    sharePref.saveString(Constant.USERTYPE, "Customer")
                    sharePref.saveString(Constant.USERID, user.id.toString())
                    sharePref.saveString(Constant.USERNAME, user.name)
                    sharePref.saveString(Constant.EMAIL, user.email)
                    sharePref.saveString(Constant.PHONE_NUMBER, user.phone)
                    sharePref.saveString(Constant.USERIMAGE, user.image.toString())

//                    passIntentToNextActivity(this, DashboardActivity::class.java, "")
                    val intent  = Intent(this,DashboardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
//                    ToastMsg("User data is null")
                    val intent = Intent(this, PersonalInformationActivity::class.java)
                    intent.putExtra("phone_number", phoneNumber)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(this, PersonalInformationActivity::class.java)
                intent.putExtra("phone_number", phoneNumber)
                startActivity(intent)
                finish()

                Log.e("phoneNumber", "onCreate:  Token -> ${model.data?.token}")
            }
        } else {
            // Handle the case where model is null
            // This might occur if the model object itself is null
        }


    }

}