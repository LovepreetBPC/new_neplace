package com.neplace.customer.activity

import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivitySideMenuBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.ProfileModel
import com.neplace.customer.viewmodel.GetProfileViewModel
import com.neplace.customer.common.Constant
import com.neplace.customer.login.LoginActivity

class SideMenuActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivitySideMenuBinding
    lateinit var getProfileViewModel: GetProfileViewModel
    var subscribePlan:Boolean= false

    var userID = ""
    var Login_TYPE = ""

    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_side_menu)


        // Initialize sign in options the client-id is copied form google-services.json file
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("319811497841-imm66r5uo7f67tbs1dhktv07jvhl1f1i.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this@SideMenuActivity, googleSignInOptions)

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        try {
            Login_TYPE = sharePref.getString(Constant.LOGIN_TYPE, "").toString()
        }catch (e:Exception){

        }


        setOnClick()
    }

    private fun setOnClick() {
        binding.txtDashboard.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.txtReservation.setOnClickListener(this)
        binding.txtBookRides.setOnClickListener(this)
        binding.txtNotification.setOnClickListener(this)
        binding.txtChat.setOnClickListener(this)
        binding.txtMyAccount.setOnClickListener(this)
        binding.txtSubscriptionPlans.setOnClickListener(this)
        binding.txtBillingInformation.setOnClickListener(this)
        binding.txtLogOut.setOnClickListener(this)
        binding.relativeContactUs.setOnClickListener(this)
        binding.txtSupport.setOnClickListener(this)
        binding.txtTermsCondition.setOnClickListener(this)
        binding.txtPrivacyPolicy.setOnClickListener(this)


        val name = sharePref.getString(Constant.USERNAME, "").toString()
        val profile_image = sharePref.getString(Constant.USERIMAGE, "").toString()


        binding.txtName.text = name
        Glide.with(this).load(Constant.BASEURL+profile_image)
            .error(R.mipmap.img_place_holder)
            .into(binding.imgProfile)



        getProfileViewModel = ViewModelProvider(this)[GetProfileViewModel::class.java]
        getProfileViewModel.profileEditResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processSet(it.data)
                    userID = it.data?.data?.user?.user_id.toString()
                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, ""+it.msg, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    dismissProgress()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getProfileViewModel.getProfileUser()

    }

    private fun processSet(data: ProfileModel?) {

        Glide.with(this).load(Constant.BASEURL+data!!.data.user.user_image)
            .error(R.mipmap.img_place_holder)
            .into(binding.imgProfile)
        binding.txtName.text = data.data.user.user_name
        if(data.data.user.ntf_count == 0){
            binding.relativeNotificationCount.visibility = View.GONE
        }else{
            binding.relativeNotificationCount.visibility = View.VISIBLE
            binding.txtNotificationCount.text = data.data.user.ntf_count.toString()
        }

        subscribePlan = data.data.subscription
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.txtReservation -> {
                startActivity(Intent(this, AllRidesActivity::class.java))
            }
            R.id.txtBookRides -> {
                startActivity(Intent(this, BookRideActivity::class.java).putExtra("subscribePlan",subscribePlan))
            }
            R.id.txtDashboard -> {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            R.id.txtMyAccount -> {
                startActivity(Intent(this, MyAccountActivity::class.java))
            }
            R.id.txtNotification -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
            R.id.txtSupport -> {
                startActivity(Intent(this, SupportActivity::class.java).putExtra("userID", userID))
            }
            R.id.txtChat -> {
//                startActivity(Intent(this, ChatActivity::class.java))
            }
            R.id.txtBillingInformation -> {
                startActivity(Intent(this, BillingInformationActivity::class.java))
            }

            R.id.txtSubscriptionPlans -> {
                startActivity(Intent(this, SubscriptionPlansActivity::class.java))
            }

            R.id.txtPrivacyPolicy -> {
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }

            R.id.txtTermsCondition -> {
                startActivity(Intent(this, TermsAndConditionsActivity::class.java))
            }

            R.id.relativeContactUs -> {
                startActivity(Intent(this, ContactActivity::class.java))
            }

            R.id.txtLogOut -> {

                if (Login_TYPE.equals("ApiLogin")){
                    commonAlertDialog("Do you want to logout?", "Alert", "logout")
                }else{
                    googleSignInClient.signOut().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign out from Firebase
                            firebaseAuth.signOut()
                            sharePref.clear()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

                            Toast.makeText(applicationContext, "Logout successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Logout failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


}