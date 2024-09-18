package com.example.neplacecustomer.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neplacecustomer.R
import com.example.neplacecustomer.common.SharedPref
import com.example.neplacecustomer.retrofit.ApiInterface
import java.io.File

open class BaseActivity : AppCompatActivity() {
    lateinit var currentPhotoPath: String


    var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null

    var imageFrom = ""

    var apiService: ApiInterface? = null
    lateinit var pDialog: ProgressDialog
    var resultUri: Uri? = null
    var sharePref = SharedPref
//    lateinit var checkPermission: CheckPermissionInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initViews()

    }
    private fun initViews() {

//        checkPermission = this
        pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")
        sharePref.init(this)

    }

    fun showProgress() {
        pDialog.show()
    }

    fun dismissProgress() {
        pDialog.dismiss()
    }

    fun getRealPathFromURI(contentURI: Uri): String? {

        val result: String?
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result

    }


    fun ToastMsg(message: String) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }


    fun replaceFragment(
        fragment: Fragment, frame: Int, addtoStack: Boolean, allowAnim: Boolean,
        bundle: Bundle?
    ) {
        val ft = supportFragmentManager.beginTransaction()

        if (addtoStack) {
            ft.addToBackStack(fragment.javaClass.name)
        }
        if (allowAnim) {
            fragment.arguments = bundle
            ft.replace(frame, fragment)
            ft.commit()
        }
    }

    fun commonAlertDialog(alertMsg: String, title: String, from: String) {

        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(title)
        builder1.setMessage(alertMsg)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "YES"
        ) { dialog, id ->

            if (from == "logout") {
                sharePref.clear()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()

            } else if (from == "delete") {

            }
            dialog.dismiss()


        }

        builder1.setNegativeButton(
            "NO") { dialog, id ->
            dialog.dismiss()
        }

        // Set the button styles
        val alertDialog = builder1.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.white))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(android.R.color.white))
        }

        alertDialog.show()

    }
    fun commonAlertDialog(alertMsg: String) {

        val builder1 =
            AlertDialog.Builder(this)
        builder1.setMessage(alertMsg)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->

//            passIntentWithFinish(this, SignupActivity::class.java, "")
            dialog.dismiss()

        }
        builder1.setNegativeButton(
            "Cancel"
        ) { dialog, id ->

            dialog.dismiss()


        }

        val alert11 = builder1.create()
        alert11.show()

    }
    fun passIntentWithFinish(context: Context, className: Class<*>, extra: String) {
        val intent = Intent(context, className)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("extra", extra)
        startActivity(intent)
        finish()
    }

    fun passIntentToNextActivity(context: Context, className: Class<*>, extra: String) {
        val intent = Intent(context, className)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("extra", extra)
        startActivity(intent)
    }
//    fun passIntentWithModel(context: Context, className: Class<*>, userprofileData: ProfileModel) {
//        val intent = Intent(context, className)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        intent.putExtra("extra_model", userprofileData)
//        startActivity(intent)
//    }

}