package com.neplace.customer.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityDashboardBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.ProfileModel
import com.neplace.customer.viewmodel.GetGoogleKeyViewModel
import com.neplace.customer.viewmodel.GetProfileViewModel
import com.neplace.customer.common.Constant
import com.neplace.customer.utils.Utils
import com.neplace.customer.retrofit.RetrofitUtils.MAPS_API_KEY
import android.provider.Settings

class DashboardActivity : BaseActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var getProfileViewModel: GetProfileViewModel
     lateinit var googleKeyViewModel : GetGoogleKeyViewModel
     var subscribePlan:Boolean= false

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)


        if (Utils.isInternetAvailable(this)){
            Log.e("interNet", "onCreate: ")
        }else{
            Utils.showNoInternetDialog(this)
        }

        initViews()
        askNotificationPermission()

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
                     val MAPS_API_KEY = it.data.data.google_key
                     val mapbox_api_key = it.data.data.mapbox_api_key
                     val stripe_key = it.data.data.stripe_key


                        Utils.saveApiKeys(this, MAPS_API_KEY, mapbox_api_key, stripe_key)
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


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
//                Log.e(TAG, "PERMISSION_GRANTED")
                // FCM SDK (and your app) can post notifications.
            } else {
//                Log.e(TAG, "NO_PERMISSION")
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivity(settingsIntent)
            }
        }
    }

}