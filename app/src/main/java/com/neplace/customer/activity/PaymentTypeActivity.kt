package com.neplace.customer.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neplace.customer.R
import com.neplace.customer.adapter.CardListAdapter
import com.neplace.customer.databinding.ActivityPaymentTypeBinding
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.GetCards
import com.neplace.customer.model.GetCardsData
import com.neplace.customer.viewmodel.DeleteCardVM
import com.neplace.customer.viewmodel.GetCardVM
import com.neplace.customer.viewmodel.PlanSubscriptionViewModel
import com.neplace.customer.viewmodel.RideStatusUpdateViewModel
import com.neplace.customer.viewmodel.StripePaymentViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.neplace.customer.common.Constant
import okhttp3.MultipartBody

class PaymentTypeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityPaymentTypeBinding
    lateinit var planSubscriptionViewModel: PlanSubscriptionViewModel
    lateinit var getCardVM: GetCardVM
    lateinit var deleteCardVM: DeleteCardVM
    lateinit var cardListAdapter: CardListAdapter
    lateinit var pDialog: ProgressDialog
    var intentcard_id = ""
    var card_id = ""
    var cardPaymenyId = ""
    var plan_id = ""
    var ride_id = ""
    var type = ""
    lateinit var rideStatusUpdateViewModel: RideStatusUpdateViewModel
    lateinit var stripePaymentViewModel: StripePaymentViewModel
    private val db = Firebase.firestore

    var cardList: List<GetCardsData>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        lisener()
        initviews()
        initViews()

        plan_id = intent.getStringExtra("id").toString()
        ride_id = intent.getStringExtra("ride_id").toString()
        type = intent.getStringExtra("type").toString()

        intentcard_id = intent.getStringExtra("card_id").toString()
        Log.e("card_id", "onCreate: $intentcard_id")
        pDialog = ProgressDialog(this)
        if (type == "cancel_ride") {
            plan_id = ""
            binding.txtCancelRide.visibility = View.VISIBLE
        } else {
            binding.txtCancelRide.visibility = View.GONE

        }
    }

    private fun initviews() {
        getCardVM = ViewModelProvider(this)[GetCardVM::class.java]
        stripePaymentViewModel = ViewModelProvider(this)[StripePaymentViewModel::class.java]
        deleteCardVM = ViewModelProvider(this)[DeleteCardVM::class.java]
        planSubscriptionViewModel = ViewModelProvider(this)[PlanSubscriptionViewModel::class.java]
        getCardVM.getCard()

        Constant.User_CARD_Id = "0"
        //Log.d("confirmedSTATUS", "initviews1: ${Constant.User_CARD_Id}")

        getCardVM.getCardsResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()

                    if (it.data!!.data.isEmpty()) {
                        binding.txtNoData.visibility = View.VISIBLE
                        binding.tvpayment.visibility = View.GONE
                        binding.recyclerListView.visibility = View.GONE
                    } else {
                        card_id = it.data.data[0].cardid
                        cardPaymenyId = it.data.data[0].id.toString()

                        binding.txtNoData.visibility = View.GONE
                        binding.tvpayment.visibility = View.VISIBLE
                        binding.recyclerListView.visibility = View.VISIBLE
                        setRecycler(it.data)

                        Constant.User_CARD_Id = it.data.data.size.toString()
                        //Log.d("confirmedSTATUS", "initviews: ${Constant.User_CARD_Id}")
                    }

                }

                is BaseResponse.Error -> {
                    dismissProgress()
                }

                else -> {
                    dismissProgress()
                }
            }
        }

        deleteCardVM.deleteCard.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    getCardVM.getCard()
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    Log.e("deleteCard", "deleteCard: Error")
                }

                null -> {

                }
            }
        }

        stripePaymentViewModel.stripePaymentResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()

                    if (it.data!!.status) {
                        Toast.makeText(this, ""+it.data.message, Toast.LENGTH_SHORT).show()
//                        rideStatusUpdateViewModel.updateRideStatus("cancelled", ride_id, false)
//                        updateRideStatus("canceled")
                        startActivity(Intent(this, AllRidesActivity::class.java))
                        finish()


                    } else {
                        Toast.makeText(this, "" + it.data.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                }

                is BaseResponse.Error -> {
                    dismissProgress()
//                    Toast.makeText(this, ""+it.msg, Toast.LENGTH_SHORT).show()
                    Log.e("deleteCard", "deleteCard: Error" + it.msg)
//                    rideStatusUpdateViewModel.updateRideStatus("cancelled", ride_id, false)
//                    updateRideStatus("canceled")

                }

                null -> {

                }
            }
        }


        planSubscriptionViewModel.planSubscriptionResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status) {
                        Toast.makeText(this, "" + it.data.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "" + it.data.message, Toast.LENGTH_SHORT).show()
                    }
//                  getCardVM.getCard()
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
                    Log.e("deleteCard", "deleteCard: Error")
                }

                null -> {

                }
            }
        }
    }


    fun lisener() {
        binding.txtaddCard.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.txtCancelRide.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txtaddCard -> {
                startActivity(Intent(this, CardDetailActivity::class.java))
            }

            R.id.imgBack -> {
                finish()
            }

            R.id.txtCancelRide -> {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("card_id", cardPaymenyId)
                    .addFormDataPart("ride_id", ride_id)
                    .build()
                stripePaymentViewModel.addStripePayment(requestBody)
            }
        }
    }

    fun setRecycler(data: GetCards?) {
        binding.recyclerListView.layoutManager = LinearLayoutManager(this)
        cardListAdapter = CardListAdapter(this, data!!.data,
            object : CardListAdapter.ondeleteCrd {
                override fun ondelete(id: String) {
                    deleteCardVM.deletecard(id)
                }

                override fun onPlanClick(card_id: Int) {
                    cardPaymenyId = card_id.toString()
                    if (plan_id != "" && plan_id.isNotEmpty()) {
                        planSubscriptionViewModel.getPlanSubscription(plan_id, card_id.toString())
                    }
                }
            }
        )
        binding.recyclerListView.adapter = cardListAdapter

    }

    fun showProgress() {
        pDialog.show()
    }

    fun dismissProgress() {
        pDialog.dismiss()
    }

    override fun onResume() {
        super.onResume()
        getCardVM.getCard()
    }


//    private fun updateRideStatus(newStatus: String) {
//        db.collection("Trip").document(ride_id).update("status", newStatus).addOnSuccessListener {
////                Toast.makeText(this, "Ride status updated successfully", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener { e ->
//            Log.e("lovepreet12345", "Error updating ride status", e)
////                Toast.makeText(this, "Failed to update ride status", Toast.LENGTH_SHORT).show()
//        }
//    }
private fun updateRideStatus(newStatus: String) {
//        db.collection("Trip").document(ride_id).update("status", newStatus).addOnSuccessListener {
////                Toast.makeText(this, "Ride status updated successfully", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener { e ->
//            Log.e("lovepreet12345", "Error updating ride status", e)
////                Toast.makeText(this, "Failed to update ride status", Toast.LENGTH_SHORT).show()
//        }

    db.collection("Trip").document(ride_id).get().addOnSuccessListener { document ->
        if (document.exists()) {
            // Document exists, proceed with update
            db.collection("Trip").document(ride_id).update("status", newStatus)
                .addOnSuccessListener {
                    Log.d("lovepreet12345", "Ride status updated successfully")
                }.addOnFailureListener { e ->
                    Log.e("lovepreet12345", "Error updating ride status: ${e.message}")
                }
        } else {
            // Document doesn't exist
            Log.e("lovepreet12345", "Document with ID $ride_id doesn't exist.")
            createAndSaveNewDocument(ride_id)
        }
    }.addOnFailureListener { e ->
        Log.e("lovepreet12345", "Error getting document: ${e.message}")
    }

}

    private fun createAndSaveNewDocument(newStatus: String) {
        // Create a new document with the specified ID and save data to it
        val tripData = hashMapOf(
            "status" to newStatus,
            // Add other fields as needed
        )

        db.collection("Trip").document(ride_id).set(tripData).addOnSuccessListener {
            Log.d("lovepreet12345", "New document created and data saved successfully")
        }.addOnFailureListener { e ->
            Log.e("lovepreet12345", "Error creating and saving new document: ${e.message}")
        }
    }



    private fun initViews() {

        rideStatusUpdateViewModel = ViewModelProvider(this)[RideStatusUpdateViewModel::class.java]

        rideStatusUpdateViewModel.rideResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status) {
                        startActivity(Intent(this, AllRidesActivity::class.java))
                        finish()
                    }

                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
//                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }
    }


}