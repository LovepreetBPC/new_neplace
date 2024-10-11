package com.neplace.customer.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivitySideMenuBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.ProfileModel
import com.neplace.customer.viewmodel.GetProfileViewModel
import com.neplace.customer.common.Constant

class SideMenuActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivitySideMenuBinding
    lateinit var getProfileViewModel: GetProfileViewModel
    var subscribePlan:Boolean= false

    var userID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_side_menu)
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
        binding.txtNotificationCount.text = data.data.user.ntf_count.toString()
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
                commonAlertDialog("Do you want to logout?", "Alert", "logout")
            }
        }
    }


}