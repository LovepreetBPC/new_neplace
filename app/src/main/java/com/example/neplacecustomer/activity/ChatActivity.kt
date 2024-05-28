package com.example.neplacecustomer.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.ChatAdapter
import com.example.neplacecustomer.databinding.ActivityChatBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.model.Chat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.neplacecustomer.common.Constant

class ChatActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityChatBinding

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var driver_id = ""
    var trip_id = ""
    var senderId = ""
    var senderName = ""
    var driver_phoneNumber = ""
    var driver_name = ""
    var driver_image = ""

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgPhone.setOnClickListener(this)

        driver_id = intent.getStringExtra("driver_id").toString()
        trip_id = intent.getStringExtra("trip_id").toString()
        driver_phoneNumber = intent.getStringExtra("driver_phoneNumber").toString()
        driver_name = intent.getStringExtra("driver_name").toString()
        driver_image = intent.getStringExtra("driver_image").toString()

        senderId = sharePref.getString(Constant.USERID, "").toString()
        senderName = sharePref.getString(Constant.USERNAME, "").toString()

        Log.e("ChatActivity", "setOnClick:   user_id - " + driver_id + "sender_id " + senderId  + ",driver_name  -> $driver_name ,  driver_phoneNumber  -> $driver_phoneNumber   , driver_image -> $driver_image ")

        binding.txtTitle.text = driver_name.toString()
        Glide.with(this).load(Constant.BASEURL + driver_image)
            .placeholder(R.mipmap.img_place_holder)
            .error(R.mipmap.img_place_holder)
            .into(binding.imgProfile)
        firestore = FirebaseFirestore.getInstance()


        binding.btnSendMessage.setOnClickListener {
            val message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etMessage.setText("")
            } else {
                sendMessage(senderId, driver_id, message,trip_id)
                binding.etMessage.setText("")
            }
        }

        readMessage(senderId, driver_id,trip_id)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
//                startActivity(Intent(this, SideMenuActivity::class.java))
                finish()
            }
            R.id.imgPhone -> {

                makePhoneCall(driver_phoneNumber)
            }
        }
    }



    private fun sendMessage(senderId: String, receiverId: String, message: String, trip_id: String) {
        val timestamp = System.currentTimeMillis()

        val chat: HashMap<String, Any> = hashMapOf(
            "created" to timestamp,
            "message" to message,
            "senderID" to senderId,
            "senderName" to senderName,
        )


        // Save the chat document in the sender's collection
        firestore.collection("Chats").document( trip_id+ "_" +receiverId )
            .collection("Messages").add(chat)
            .addOnSuccessListener { documentReference ->
                // Chat data saved successfully
            }
            .addOnFailureListener { e ->
                // Handle the failure
            }

    }


    private fun readMessage(senderId: String, receiverId: String, trip_id: String) {
        Log.e("logerruiyriryir", "readMessage: "+trip_id+ "_" + receiverId)
        firestore.collection("Chats").document( trip_id+ "_" + receiverId)
            .collection("Messages").orderBy("created")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle the error
                    return@addSnapshotListener
                }

                chatList.clear()
                for (doc in snapshot!!.documents) {
                    val chat = doc.toObject(Chat::class.java)
                    chatList.add(chat!!)
                }
                Log.e("getChatData", "readMessage: "+chatList.toString())

                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                val adapter = ChatAdapter(this@ChatActivity, chatList, senderId)
                binding.recyclerView.adapter = adapter
            }
    }


    private fun makePhoneCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phoneNumber")

        if (packageManager.resolveActivity(callIntent, 0) != null) {
            startActivity(callIntent)
        } else {
            Toast.makeText(this, "Phone number No found !!", Toast.LENGTH_SHORT).show()
        }
    }
}