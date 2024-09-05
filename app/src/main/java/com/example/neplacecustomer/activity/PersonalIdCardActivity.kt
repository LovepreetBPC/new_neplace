package com.example.neplacecustomer.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityPersonalIdCardBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.VerifyOtpModel
import com.example.neplacecustomer.utils.BitmapUtils
import com.example.neplacecustomer.viewmodel.RegisterCustomerCardViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PersonalIdCardActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityPersonalIdCardBinding
    lateinit var registerCustomerCardViewModel: RegisterCustomerCardViewModel


    private lateinit var pickerDialog: Dialog
    private var selectedImageFile: File? = null
    private var selectedImageFileBack: File? = null

    private var GALLERY = 145
    var photoURI: Uri? = null
    var REQUEST_IMAGE_CAPTURE = 88
    private val PERMISSION_CAMERA_REQUEST_CODE = 400
//    lateinit var currentPhotoPath: String
    private val PERMISSION_STORAGE_REQUEST_CODE = 600
    private val FILE_PROVIDER_AUTHORITY = "com.example.neplacecustomer"
    private var mTempPhotoPath: String? = null

    lateinit var mResultsBitmap: Bitmap
//    lateinit var selectedUri: Uri
//    lateinit var selectedUriBack: Uri
    var selectedImageData = ""
    var selectType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_id_card)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_id_card)
        initViews()
        setOnClick()
        setSpinner()
    }

    private fun initViews() {
        registerCustomerCardViewModel = ViewModelProvider(this)[RegisterCustomerCardViewModel::class.java]

        registerCustomerCardViewModel.registerCustomerCardResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processSet(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    dismissProgress()
                }
            }
        }

    }

    private fun processSet(data: VerifyOtpModel?) {
        ToastMsg(data!!.message.toString())
        startActivity(Intent(this, SubscriptionPlansActivity::class.java))
        finish()

    }

    private fun processError(msg: String?) {
        ToastMsg(msg.toString())

    }


    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtSave.setOnClickListener(this)
        binding.relativefront.setOnClickListener(this)
        binding.relativeback.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.txtSave -> {
                if (Validation()) {
                    var body: MultipartBody.Part? = null
                    var bodyBack: MultipartBody.Part? = null
                    if (selectedImageFile != null) {
                        Log.d("personalID", "onClick: if")

                        if (selectedImageFileBack != null ){
                            Log.d("personalID", "onClick back: if")

                            val requestBody: RequestBody = selectedImageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            body = MultipartBody.Part.createFormData("personal_id_card_front_image", selectedImageFile!!.name, requestBody)
                            bodyBack = MultipartBody.Part.createFormData("personal_id_card_back_image", selectedImageFileBack!!.name, requestBody)

                            registerCustomerCardViewModel.registerCustomerCardUser(body,bodyBack)

                        }else{
                            Log.d("personalID", "onClick back: else")
                            Toast.makeText(applicationContext, "please enter your ID card back image!!", Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        Log.d("personalID", "onClick: else")
                        Toast.makeText(applicationContext, "please enter your ID card front image!!", Toast.LENGTH_SHORT).show()
                        /*val requestBody: RequestBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        body = MultipartBody.Part.createFormData("personal_id_card_front_image", "", requestBody)
                        bodyBack = MultipartBody.Part.createFormData("personal_id_card_back_image", "", requestBody)
                        registerCustomerCardViewModel.registerCustomerCardUser(body!!,bodyBack)*/
                    }
                }
            }

            R.id.relativefront -> {
                imagePickerDialog(88)

            }
            R.id.relativeback -> {
                imagePickerDialog(100)

            }

        }
    }


    private fun Validation(): Boolean {

        return true
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun imagePickerDialog(i: Int) {

        val builder = AlertDialog.Builder(
            this@PersonalIdCardActivity,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
        val inflater = layoutInflater
        val rootView: View = inflater.inflate(R.layout.custom_select_image_dialog, null)
        val clickImage = rootView.findViewById<ImageView>(R.id.clickImage)
        val selectImage = rootView.findViewById<ImageView>(R.id.selectImage)
        builder.setView(rootView)
        builder.setCancelable(true)
        pickerDialog = builder.create()
        val lp2 = WindowManager.LayoutParams()
        val window: Window = pickerDialog.getWindow()!!
        pickerDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        lp2.copyFrom(window.attributes)
        //This makes the dialog take up the full width
        lp2.width = ViewGroup.LayoutParams.MATCH_PARENT
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT
        lp2.windowAnimations = R.style.DialogAnimation
        window.attributes = lp2
        val dialogWindow: Window = pickerDialog.getWindow()!!
        val lp = dialogWindow.attributes
        dialogWindow.setGravity(Gravity.BOTTOM)
        clickImage.setOnClickListener {
            pickerDialog.dismiss()
            if (checkCameraPermission()) {
                launchCamera(i)
            } else {
                requestCameraPermission()
            }
        }
        selectImage.setOnClickListener {
            pickerDialog.dismiss()
            if (!checkStoragePermission()) {
                requestStoragePermission()
            } else {

                if (i == 88) {
                    choosePhotoFromGallary(145)
                }
                else {
                    choosePhotoFromGallary(200)

                }

            }
        }
        pickerDialog.show()
    }

    private fun checkCameraPermission(): Boolean {
        val cameraPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            ), PERMISSION_CAMERA_REQUEST_CODE
        )
    }
    fun setSpinner(){
        val arrayList = arrayOf("Identification Card", "Passport", "Driving License","Government Issued ID","Others")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPassenger.adapter = adapter
        binding.spinnerPassenger.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectType = arrayList[position]
//                    selectPassenger = seatNumber - arrayList[position]

                    Log.e("luggage", "setPassengerSpinner   $selectType")
//                val selectedFruit = parent?.getItemAtPosition(position) as String
                    // Do something with the selected item (selectedFruit)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }

    }

    private fun launchCamera(i: Int) {
        REQUEST_IMAGE_CAPTURE = i

        Log.e("launchCamera", "launchCamera: " + REQUEST_IMAGE_CAPTURE)
        // Create the capture image intent
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (let { takePictureIntent.resolveActivity(it.packageManager) } != null) {
            // Create the temporary File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // Get the path of the temporary file
                mTempPhotoPath = photoFile.absolutePath
                // Get the content URI for the image file
                photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile)
                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                // Launch the camera activity
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    val permissionTemp =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    grantUriPermission(packageName, Uri.fromFile(photoFile), permissionTemp)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            Log.e("launchCamera", "else")
        }
    }

    private fun checkStoragePermission(): Boolean {
        val readPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writePermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

        )
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            ),
            PERMISSION_STORAGE_REQUEST_CODE
        )
    }

    fun choosePhotoFromGallary(i: Int) {
        GALLERY = i
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val uri: Uri = data.getData()!!
                selectedImageData = uri.toString()
                if (GALLERY == 145) {
                    selectedImageFile = File(getRealPathFromURI(uri)!!)

                    Glide.with(this).load(uri)
                        .placeholder(R.mipmap.img_place_holder)
                        .error(R.mipmap.img_place_holder)
                        // .trnsform(RotateTransformation(this, 90f))
                        .into(binding.imgDocment)
                    if (uri != null) {
                        binding.imgDocment.visibility = View.VISIBLE
                    } else {
                        binding.imgDocment.visibility = View.GONE

                    }
                }
                else if (GALLERY == 200){
                    selectedImageFileBack = File(getRealPathFromURI(uri)!!)
                    Glide.with(this).load(uri)
                        .placeholder(R.mipmap.img_place_holder)
                        .error(R.mipmap.img_place_holder)
                        // .trnsform(RotateTransformation(this, 90f))
                        .into(binding.imgDocmentBack)
                    if (uri != null) {
                        binding.imgDocmentBack.visibility = View.VISIBLE
                    } else {
                        binding.imgDocmentBack.visibility = View.GONE

                    }
                }

            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            mResultsBitmap = let { BitmapUtils.resamplePic(it, mTempPhotoPath) }
            val uri: Uri = getImageUri(mResultsBitmap)
            selectedImageData = uri.toString()
            Log.e("LOG_TAG", " selected image url: " + uri)
            Log.e("Select", "CAM_REQUEST $uri")
            if (REQUEST_IMAGE_CAPTURE == 88) {
                if (uri != null) {
                    selectedImageFile = File(getRealPathFromURI(uri)!!)
                    binding.imgDocment.visibility = View.VISIBLE
                } else {
                    binding.imgDocment.visibility = View.GONE

                }
                Glide.with(this).load(uri)
                    .placeholder(R.mipmap.img_place_holder)
                    .error(R.mipmap.img_place_holder)
                    // .trnsform(RotateTransformation(this, 90f))
                    .into(binding.imgDocment)
            } else if (REQUEST_IMAGE_CAPTURE == 100) {
                if (uri != null) {
                    selectedImageFileBack = File(getRealPathFromURI(uri)!!)
                    binding.imgDocmentBack.visibility = View.VISIBLE
                } else {
                    binding.imgDocmentBack.visibility = View.GONE
                }
                Glide.with(this).load(uri)
                    .placeholder(R.mipmap.img_place_holder)
                    .error(R.mipmap.img_place_holder)
                    // .trnsform(RotateTransformation(this, 90f))
                    .into(binding.imgDocmentBack)
            }
        }
    }


    fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            inImage,
            "ll" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
    }
}