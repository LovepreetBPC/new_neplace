package com.example.neplacecustomer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityDashboardBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.ProfileModel
import com.example.neplacecustomer.viewmodel.GetGoogleKeyViewModel
import com.example.neplacecustomer.viewmodel.GetProfileViewModel
import com.google.android.libraries.places.api.Places
import com.nexter.application.common.Constant
import com.nexter.application.retrofit.RetrofitUtils
import com.nexter.application.retrofit.RetrofitUtils.MAPS_API_KEY

class DashboardActivity : BaseActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var getProfileViewModel: GetProfileViewModel
     lateinit var googleKeyViewModel : GetGoogleKeyViewModel
     var subscribePlan:Boolean= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)



        initViews()

        Log.e("12345678", "book ride: " + MAPS_API_KEY)

    }


    private fun initViews() {

        googleKeyViewModel = ViewModelProvider(this)[GetGoogleKeyViewModel::class.java]

        googleKeyViewModel.getGoogleKey()
        googleKeyViewModel.getGoogleKeyResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {

                }

                is BaseResponse.Success -> {
                    if (it.data!!.status) {
                        MAPS_API_KEY = it.data.data.google_key
                    }
                }

                else -> {

                }
            }
        }


        Log.e("token", "initViews: " + sharePref.getString(Constant.TOKEN, "").toString())
        getProfileViewModel = ViewModelProvider(this)[GetProfileViewModel::class.java]
        getProfileViewModel.getProfileUser()
        binding.idScroolMain.visibility = View.GONE

        getProfileViewModel.profileEditResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    binding.idScroolMain.visibility = View.VISIBLE
                    processSet(it.data)
                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    dismissProgress()
                }
            }
        }



        binding.txtContinue.setOnClickListener {
            startActivity(Intent(this, BookRideActivity::class.java).putExtra("subscribePlan",subscribePlan))


        }
        binding.imgMenu.setOnClickListener {
            startActivity(Intent(this, SideMenuActivity::class.java))
        }
        binding.txtUpgradePlan.setOnClickListener {
            startActivity(Intent(this, SubscriptionPlansActivity::class.java))
        }
        binding.txtSubscibeNow.setOnClickListener {
            startActivity(Intent(this, SubscriptionPlansActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        getProfileViewModel.getProfileUser()
        googleKeyViewModel.getGoogleKey()


    }

    private fun processSet(model: ProfileModel?) {

        if (model!!.status) {
//            ToastMsg(model.message.toString())



            if (model.data.subscription){
                binding.relativePlanDetail.visibility = View.VISIBLE
                binding.linearPlan.visibility = View.VISIBLE
                binding.txtContinue.visibility = View.VISIBLE
                binding.txtUpgradePlan.visibility = View.VISIBLE
                binding.viewPlanBottom.visibility = View.VISIBLE
                binding.txtSubscibeNow.visibility = View.GONE
            }else{
                binding.relativePlanDetail.visibility = View.GONE
                binding.linearPlan.visibility = View.GONE
                binding.txtContinue.visibility = View.GONE
                binding.txtUpgradePlan.visibility = View.GONE
                binding.viewPlanBottom.visibility = View.GONE
                binding.txtSubscibeNow.visibility = View.VISIBLE

            }

            binding.txtMobileNumber.setText(model.data.user.phone_number.toString())

            binding.txtName.setText(model.data.user.user_name.toString())
//            binding.edtLastName.setText(model.data.last_name.toString())
            binding.txtEmail.setText(model.data.user.email.toString())

            Log.e("userEmail", "processSet: " + model.data.user.email.toString())
            Glide.with(this).load(Constant.BASEURL + model.data.user.user_image)
                .error(R.mipmap.img_place_holder)
                .into(binding.imgProfile)







            sharePref.saveString(Constant.USERNAME, model.data.user.user_name)
            sharePref.saveString(Constant.EMAIL, model.data.user.email)
            sharePref.saveString(Constant.PHONE_NUMBER, model.data.user.phone_number.toString())
            sharePref.saveString(Constant.USERIMAGE, model.data.user.user_image.toString())
            sharePref.saveString(Constant.PlanID, model.data.subscriptionDetails.plan_id.toString())
            subscribePlan = model.data.subscription






            if (model.data.subscription) {

                if (model.data.subscriptionDetails.plan_id == 2) {
                    binding.txtTransfersValue.text = "Unlimited"
                    binding.relativeRemainin.visibility = View.GONE
                    binding.View2.visibility = View.GONE
                    binding.txtPlanName.text = model.data.subscriptionDetails.plan.name.toString()
                    binding.txtPrice.text = "\$${model.data.subscriptionDetails.plan.price.toString()}/month"
                    binding.txtUsedNumber.text = model.data.user.total_trips.toString()
                    binding.txtRemainingNumber.text = ( model.data.user.total_trips.toInt()).toString()
                } else {
                    binding.txtTransfersValue.text = "5"
                    binding.relativeRemainin.visibility = View.VISIBLE
                    binding.View2.visibility = View.VISIBLE
                    binding.txtPlanName.text = model.data.subscriptionDetails.plan.name.toString()
                    binding.txtPrice.text =
                        "\$${model.data.subscriptionDetails.plan.price.toString()}/month"
                    binding.txtUsedNumber.text = model.data.user.total_trips.toString()
                    binding.txtRemainingNumber.text =
                        (5 - model.data.user.total_trips.toInt()).toString()
                }

            }



        } else {
//            ToastMsg(model.message.toString())

        }

    }
}