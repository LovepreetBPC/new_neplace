package com.example.neplacecustomer.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityCardDetailBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.Craete_CardToken
import com.example.neplacecustomer.viewmodel.AddCardViewModel
import com.example.neplacecustomer.retrofit.UserNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class CardDetailActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityCardDetailBinding
    lateinit var addCArdViewModel: AddCardViewModel
    private lateinit var selectedDate: Date

    var cardExpiry = ""
    var cardMonth = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lisener()
        initViewss()
    }


    fun lisener() {
        binding.txtSaveCard.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.edtCardNumber.addTextChangedListener(CardNumberFormattingTextWatcher())

        binding.edtExpiryDate.setOnClickListener {
            showMonthYearPickerDialog()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txtSaveCard -> {
                val textWithoutSpaces = binding.edtCardNumber.text.toString().replace(" ", "")
                showProgress()
                UserNetwork.getStripe()
                    .createCardInterface(textWithoutSpaces, cardMonth, cardExpiry, binding.edtCvv.text.toString())
                    .enqueue(object : Callback<Craete_CardToken> {
                        override fun onResponse(
                            call: Call<Craete_CardToken>, response: Response<Craete_CardToken>
                        ) {
                            if (response.isSuccessful) {
                                Log.e("1234234", "onResponse: " + response.body()?.id)
                                dismissProgress()
                                payList(response.body()?.id ?: "", response.body()?.card?.last4 ?: "")
                            }else{
                                dismissProgress()
                                Toast.makeText(this@CardDetailActivity, "Invalid Card Detail  ", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Craete_CardToken>, t: Throwable) {
                            dismissProgress()
                            Log.e("1234234", "onResponse: " + t.message)
                        }

                    })
            }

            R.id.imgBack -> {
                finish()
            }
        }

    }


    fun payList(id: String, amount: String) {

        val hashMap = HashMap<String, Any>()
        hashMap["token"] = id
        hashMap["card_number"] = amount

        addCArdViewModel.addCard(hashMap)

        Log.e("payList123456", "payList:  token->  $id  ,  card_number -> $amount")

    }

    private fun initViewss() {
        addCArdViewModel = ViewModelProvider(this)[AddCardViewModel::class.java]


        addCArdViewModel.addCardDetail.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
//                    Toast.makeText(this, "loading", Toast.LENGTH_SHORT).show()
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if(it.data!!.status){
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    }


                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, ""+it.msg, Toast.LENGTH_SHORT).show()
                    dismissProgress()
                }

                else -> {
                    dismissProgress()
                }
            }
        }
    }


    private inner class CardNumberFormattingTextWatcher : TextWatcher {
        private val MAX_LENGTH = 19 // Maximum length of formatted card number

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            // Remove previous formatting
            val originalText = s.toString()
            val cleanText = originalText.replace("[^\\d]".toRegex(), "")

            // Apply formatting
            val formattedText = StringBuilder()
            for (i in 0 until cleanText.length) {
                formattedText.append(cleanText[i])
                if ((i + 1) % 4 == 0 && (i + 1) != cleanText.length) {
                    formattedText.append(" ") // Add a space after every 4 digits
                }
            }

            // Set the formatted text to the EditText
            binding.edtCardNumber.removeTextChangedListener(this)
            binding.edtCardNumber.setText(formattedText)
            binding.edtCardNumber.setSelection(formattedText.length)
            binding.edtCardNumber.addTextChangedListener(this)

            // Ensure the length does not exceed the maximum length
            if (s?.length ?: 0 > MAX_LENGTH) {
                binding.edtCardNumber.setText(s?.subSequence(0, MAX_LENGTH))
                binding.edtCardNumber.setSelection(MAX_LENGTH)
            }
        }
    }


    private fun showMonthYearPickerDialog() {
        val monthYearPickerDialog =
            MonthPickerDialog(this, object : MonthPickerDialog.OnMonthYearPickListener {
                override fun onMonthYearPick(month: Int, year: Int) {
                    val selectedDate = String.format("%02d/%04d", month, year)
                    // Do something with the selected date (e.g., set it to a TextView)
                    binding.edtExpiryDate.setText(selectedDate)

                    cardExpiry = year.toString()
                    cardMonth = month.toString()
                }
            })
        monthYearPickerDialog.show()
    }

}











