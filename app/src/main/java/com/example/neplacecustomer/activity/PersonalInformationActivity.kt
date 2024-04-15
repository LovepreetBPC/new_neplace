package com.example.neplacecustomer.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityPersonalInformationBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.VerifyOtpModel
import com.example.neplacecustomer.utils.BitmapUtils
import com.example.neplacecustomer.viewmodel.RegisterViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.messaging.FirebaseMessaging
import com.nexter.application.common.Constant
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PersonalInformationActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityPersonalInformationBinding
    lateinit var registerViewModel: RegisterViewModel

    private lateinit var pickerDialog: Dialog

    private var selectedImageFile: File? = null

    private val GALLERY = 145
    var photoURI: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 88
    private val PERMISSION_CAMERA_REQUEST_CODE = 400

    //    lateinit var currentPhotoPath: String
    private val PERMISSION_STORAGE_REQUEST_CODE = 600
    private val FILE_PROVIDER_AUTHORITY = "com.example.neplacecustomer"
    private var mTempPhotoPath: String? = null

    lateinit var mResultsBitmap: Bitmap
    lateinit var selectedUri: Uri
    var selectedImageData = ""

    var device_token = ""


    var REQUEST_AUTOCOMPLETE = 201
    var state: String = ""
    var pickup_city: String = ""
    var country: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_information)

        Places.initialize(
            this@PersonalInformationActivity,
            "AIzaSyDybi97YEi_iux4SxEuNiTkOXEYs8SUy0g"
        )
        initViewss()
        setOnClick()
    }

    private fun setOnClick() {
        binding.txtNext.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.txtUploadPhoto.setOnClickListener(this)
        binding.txtEnterAddress.setOnClickListener(this)


        val number = intent.getStringExtra("phone_number").toString()
        binding.edtPhoneNumber.setText(number)


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
            device_token = task.result

            Log.e("TAG", "init: " + device_token)
        })

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }

            R.id.txtNext -> {
                var body: MultipartBody.Part? = null

                if (Validation()) {

                    if (selectedImageFile != null) {
                        val requestBody: RequestBody =
                            selectedImageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        body = MultipartBody.Part.createFormData(
                            "image",
                            selectedImageFile!!.name,
                            requestBody
                        )
                        registerViewModel.registerUser(
                            binding.edtFirstName.text.toString(),
                            binding.edtLastName.text.toString(),
                            binding.edtEmail.text.toString(),
                            binding.edtPhoneNumber.text.toString(),
                            "2",
                            binding.edtCity.text.toString(),
                            binding.txtEnterAddress.text.toString(),
                            "123456",
                            "123456",
                            body,
                            device_token
                        )

                    } else {
                        val requestBody: RequestBody =
                            "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        body = MultipartBody.Part.createFormData("image", "", requestBody)

                        registerViewModel.registerUser(
                            binding.edtFirstName.text.toString(),
                            binding.edtLastName.text.toString(),
                            binding.edtEmail.text.toString(),
                            binding.edtPhoneNumber.text.toString(),
                            "2",
                            binding.edtCity.text.toString(),
                            binding.txtEnterAddress.text.toString(),
                            "123456",
                            "123456",
                            body,
                            device_token
                        )
                    }


                }
            }

            R.id.txtUploadPhoto -> {
                imagePickerDialog()

            }

            R.id.txtEnterAddress -> {
                onClickLocation()

            }
        }
    }

    fun onClickLocation() {
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, REQUEST_AUTOCOMPLETE)
    }

    private fun initViewss() {
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


        registerViewModel.registerResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processLogin(it.data)
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

    private fun Validation(): Boolean {

        if (binding.edtFirstName.text.toString() == "") {
            ToastMsg("Enter first number")
            return false
        }
        if (binding.edtLastName.text.toString() == "") {
            ToastMsg("Enter last number")
            return false
        }
        if (binding.edtPhoneNumber.text.toString() == "") {
            ToastMsg("Enter phone number")
            return false
        }

        if (binding.edtEmail.text.toString() == "") {
            ToastMsg("Enter email id")
            return false
        }

        return true
    }

    private fun processError(msg: String?) {
        ToastMsg(msg.toString())
    }

    private fun processLogin(model: VerifyOtpModel?) {

        if (model!!.status) {
            ToastMsg(model.message)
            startActivity(Intent(this, PersonalIdCardActivity::class.java))
            sharePref.saveString(Constant.TOKEN, model.data.token)
            sharePref.saveString(Constant.USERID, model.data.user.id.toString())
            sharePref.saveString(Constant.USERTYPE, "Customer")
            sharePref.saveString(Constant.USERNAME, model.data.user.name)
            sharePref.saveString(Constant.USERIMAGE, model.data.user.image.toString())
            finish()
        } else {

            ToastMsg(model.message.toString())

        }

    }


    private fun imagePickerDialog() {
        val builder = AlertDialog.Builder(
            this@PersonalInformationActivity,
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
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                launchCamera()
            }
        }
        selectImage.setOnClickListener {
            pickerDialog.dismiss()
            if (!checkStoragePermission()) {
                requestStoragePermission()
            } else {
                choosePhotoFromGallary()
            }
        }
        pickerDialog.show()
    }

    private fun checkCameraPermission(): Boolean {
        val cameraPermission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CAMERA_REQUEST_CODE
        )
    }

    private fun launchCamera() {
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_STORAGE_REQUEST_CODE
        )
    }


    fun choosePhotoFromGallary() {

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
                selectedUri = uri
                selectedImageFile = File(getRealPathFromURI(selectedUri)!!)

                Log.e("LOG_TAG", " selected image url: " + selectedUri)
                if (uri != null) {

                    binding.cardProfileImage.visibility = View.VISIBLE
                    Glide.with(this).load(uri)
                        .placeholder(R.mipmap.img_place_holder)
                        .error(R.mipmap.img_place_holder)
                        // .trnsform(RotateTransformation(this, 90f))
                        .into(binding.imgProfile)

                } else {
                    binding.cardProfileImage.visibility = View.GONE
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            mResultsBitmap = let { BitmapUtils.resamplePic(it, mTempPhotoPath) }
            val uri: Uri = getImageUri(mResultsBitmap)
            selectedUri = uri
            selectedImageData = uri.toString()
            selectedImageFile = File(getRealPathFromURI(selectedUri)!!)
            Log.e("LOG_TAG", " selected image url: " + selectedUri)
            Log.e("Select", "CAM_REQUEST $uri")

            if (uri != null) {
                binding.cardProfileImage.visibility = View.VISIBLE

                Glide.with(this).load(uri)
                    .placeholder(R.mipmap.img_place_holder)
                    .error(R.mipmap.img_place_holder)
                    // .trnsform(RotateTransformation(this, 90f))
                    .into(binding.imgProfile)
            } else {
                binding.cardProfileImage.visibility = View.GONE
            }
        } else if (resultCode == RESULT_OK && requestCode == 201) {
            val place: Place = Autocomplete.getPlaceFromIntent(data!!)
            val latLng: LatLng? = place.latLng

            val myLat = latLng!!.latitude
            val myLong = latLng!!.longitude

            val geocoder = Geocoder(this@PersonalInformationActivity, Locale.getDefault())
            try {
                val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)!!
                state = addresses[0].getAdminArea()
                pickup_city = addresses[0].getLocality()
                country = addresses[0].countryName
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.txtEnterAddress.setText(
                place.name.toString().plus(", ").plus(place.address.toString())
            )
            binding.edtCity.setText(pickup_city.toString())
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