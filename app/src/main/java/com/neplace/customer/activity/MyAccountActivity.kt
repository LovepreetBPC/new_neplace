package com.neplace.customer.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityMyAccountBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.GetCardsData
import com.neplace.customer.viewmodel.GetCardVM
import com.neplace.customer.viewmodel.GetProfileViewModel
import com.neplace.customer.common.Constant

class MyAccountActivity : BaseActivity(),View.OnClickListener {

    lateinit var binding: ActivityMyAccountBinding
    lateinit var getCardVM: GetCardVM
    var cardList:List<GetCardsData>?=ArrayList()
    lateinit var getProfileViewModel: GetProfileViewModel
    var card_id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account)
        setData()
        setOnClick()
        initviews()
    }

    private fun setData() {

        val name = sharePref.getString(Constant.USERNAME, "").toString()
        val email = sharePref.getString(Constant.EMAIL, "").toString()
        val number = sharePref.getString(Constant.PHONE_NUMBER, "").toString()
        val profile_image = sharePref.getString(Constant.USERIMAGE, "").toString()


        binding.txtName.text = name
        binding.txtMobileNumber.text = number
        binding.txtEmail.text = email
        Glide.with(this).load(Constant.BASEURL+profile_image)
            .error(R.mipmap.img_place_holder)
            .into(binding.imgProfile)

    }


    override fun onResume() {
        super.onResume()
        initviews()

    }
    private fun setOnClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.imgEditProfile.setOnClickListener(this)
        binding.txtLogoutMain.setOnClickListener(this)
        binding.relativeAddBank.setOnClickListener(this)
        binding.imgEditBank.setOnClickListener(this)
        binding.txtDeleteAccount.setOnClickListener(this)
        binding.txtSubscibeNow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgMenu -> {
                startActivity(Intent(this, SideMenuActivity::class.java))
            }
            R.id.txtLogoutMain -> {
                commonAlertDialog("Do you want to logout?", "Alert", "logout")

            }
            R.id.txtDeleteAccount -> {
                commonAlertDialog("Are you sure you want to delete Account?", "Alert", "delete")

            }
            R.id.imgEditProfile -> {
                startActivity(Intent(this, EditProfileActivity::class.java))
                finish()
            }

            R.id.relativeAddBank -> {
                startActivity(Intent(this, CardDetailActivity::class.java))
            }
            R.id.imgEditBank -> {
                startActivity(Intent(this , PaymentTypeActivity::class.java).putExtra("card_id",card_id))
            }
            R.id.txtSubscibeNow -> {
                startActivity(Intent(this, SubscriptionPlansActivity::class.java))
            }
        }
    }

    private fun initviews() {
        getCardVM = ViewModelProvider(this)[GetCardVM::class.java]
        getProfileViewModel = ViewModelProvider(this)[GetProfileViewModel::class.java]
        getCardVM.getCard()
        getProfileViewModel.getProfileUser()

        getCardVM.getCardsResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    if (it.data!!.data.isEmpty()){
                        binding.relativecarddetail.visibility = View.GONE
                    }else{

                        binding.relativecarddetail.visibility = View.VISIBLE
                        card_id= it.data.data[0].cardid
                        binding.cardnumber.text = "************"+  it.data.data[0].card_number

                    }


                    dismissProgress()

                }

                is BaseResponse.Error -> {
                    dismissProgress()
                }
                else -> {
                    dismissProgress()
                }
            }
        }


        getProfileViewModel.profileEditResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status){
                        if (it.data.data.subscription){
                            if (it.data.data.subscriptionDetails.plan_id == 2) {
                                binding.txtPeningRide.text = "Unlimited"
                            }else{
                                binding.txtPeningRide.text = it.data.data.user.total_trips.toString()+"Ride"

                            }
                            binding.relativePlanDetail.visibility = View.VISIBLE
                            binding.txtPlanName.text = it.data.data.subscriptionDetails.plan.name.toString()
                            binding.txtPrice.text = it.data.data.subscriptionDetails.plan.price.toString()
                            binding.txtSubscibeNow.visibility = View.GONE

                        }else{
                            binding.relativePlanDetail.visibility = View.GONE
                            binding.txtSubscibeNow.visibility = View.VISIBLE


                        }

                    }
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    dismissProgress()
                }
            }
        }

    }

}