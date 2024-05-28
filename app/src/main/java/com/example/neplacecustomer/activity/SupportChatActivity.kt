package com.example.neplacecustomer.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.adapter.ChatAdapter
import com.example.neplacecustomer.databinding.ActivitySupportChatBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.example.neplacecustomer.common.Constant
import okhttp3.MultipartBody

class SupportChatActivity : BaseActivity() {

    lateinit var binding: ActivitySupportChatBinding
    private lateinit var firestore: FirebaseFirestore
    var senderId = ""
    var senderName = ""
    var companyId="7"
    var chatList = ArrayList<Chat>()
//    lateinit var sendMessageNotificationViewModel: SendMessageNotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        senderId = sharePref.getString(Constant.USERID, "").toString()
        senderName = sharePref.getString(Constant.USERNAME, "").toString()
//        companyId = sharePref.getString(Constant.COMPANYID, "").toString()
        initView()

        readMessage()

        binding.btnSendMessage.setOnClickListener {
            val message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etMessage.setText("")
            } else {
                sendMessage()

                binding.etMessage.setText("")
                val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("type", "support")
                    .addFormDataPart("message", binding.etMessage.text.toString())
                    .addFormDataPart("title", "Driver to admin")
                    .addFormDataPart("user_id", senderId)
                    .build()

//                sendMessageNotificationViewModel.sendMessageNotification(requestBody)
            }
        }



    }

    private fun sendMessage() {

        val timestamp = System.currentTimeMillis()
        val chat: HashMap<String, Any> = hashMapOf(
            "senderID" to senderId,
            "message" to binding.etMessage.text.toString(),
            "senderName" to senderName,
            "created" to timestamp
        )


        firestore.collection("admin-chat").document(senderId + "_" + companyId)
            .collection("Messages").add(chat)
            .addOnSuccessListener { documentReference ->
                // Chat data saved successfully
            }
            .addOnFailureListener { e ->
                // Handle the failure
            }

    }


    private fun readMessage() {
//        Toast.makeText(this, "$senderId _ $companyId", Toast.LENGTH_SHORT).show()

        try {
            firestore.collection("admin-chat").document(senderId + "_" + companyId)
                .collection("Messages").orderBy("created")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle the error
                        return@addSnapshotListener
                    }

                    chatList.clear()
                    if (snapshot!!.documents.isEmpty()){
                        Log.e("getMessage", "readMessage: No Data")
                    }else{

                        for (doc in snapshot.documents) {
                            val chat = doc.toObject(Chat::class.java)
                            chatList.add(chat!!)
                        }

                        val adapter = ChatAdapter(this@SupportChatActivity, chatList, senderId)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this)
                        binding.recyclerView.adapter = adapter
                    }
                }
        }catch (e:Exception){
            Log.e("getMessageError", "readMessage: "+e.message)
        }

    }


    private fun initView() {
//        sendMessageNotificationViewModel =
//            ViewModelProvider(this)[SendMessageNotificationViewModel::class.java]
//        sendMessageNotificationViewModel.sendMessageNotificationResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
////                    showProgress()
//                    dismissProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//                }
//
//                is BaseResponse.Error -> {
//                    dismissProgress()
//
//                }
//
//                null -> {
//
//                }
//            }
//        }
    }


}