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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityEditProfileBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.ProfileModel
import com.example.neplacecustomer.utils.BitmapUtils
import com.example.neplacecustomer.viewmodel.GetProfileViewModel
import com.example.neplacecustomer.viewmodel.ProfileEditViewModel
import com.example.neplacecustomer.common.Constant
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

class EditProfileActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityEditProfileBinding
    lateinit var viewModel: ProfileEditViewModel
    lateinit var getProfileViewModel: GetProfileViewModel

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        initViews()
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgEdit.setOnClickListener(this)
        binding.txtUpdate.setOnClickListener(this)
    }


    private fun initViews() {
        viewModel = ViewModelProvider(this)[ProfileEditViewModel::class.java]
        getProfileViewModel = ViewModelProvider(this)[GetProfileViewModel::class.java]
        getProfileViewModel.getProfileUser()


        viewModel.profileEditResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    Toast.makeText(this, ""+ it.data!!.message.toString(), Toast.LENGTH_SHORT).show()
                    processEdit(it.data)
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    processError(it.msg)
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
                    processSet(it.data)
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    processError(it.msg)
                }
                else -> {
                    dismissProgress()
                }
            }
        }


    }

    private fun processError(msg: String?) {
        ToastMsg(msg.toString())

    }

    private fun processEdit(model: ProfileModel?) {
        if (model!!.status) {
//            ToastMsg(model.message.toString())
            sharePref.saveString(Constant.USERID, model.data.user.user_id.toString())
            sharePref.saveString(Constant.USERTYPE, "Customer")
            sharePref.saveString(Constant.USERNAME, model.data.user.user_name)
            sharePref.saveString(Constant.EMAIL, model.data.user.email)
            sharePref.saveString(Constant.PHONE_NUMBER, model.data.user.phone_number.toString())
            sharePref.saveString(Constant.USERIMAGE, model.data.user.user_image.toString())
            sharePref.saveString(Constant.USERIMAGE, model.data.user.address.toString())


        } else {
            ToastMsg(model.message.toString())

        }
    }
    private fun processSet(model: ProfileModel?) {
        if (model!!.status) {
//            ToastMsg(model.message.toString())
            binding.edtPhoneNumber.setText(model.data.user.phone_number.toString())

            binding.edtName.setText(model.data.user.first_name.toString())
            binding.edtLastName.setText(model.data.user.last_name.toString())
            binding.edtVehicleType.setText(model.data.user.address.toString())
            binding.edtEmail.setText(model.data.user.email.toString())

            if (!model.data.user.phone_number.isNullOrEmpty()){
                binding.edtPhoneNumber.setText(model.data.user.phone_number.toString())
            }

            Glide.with(this).load(Constant.BASEURL+model.data.user.user_image)
                .error(R.mipmap.img_place_holder)
                .into(binding.imgProfile)

        } else {
            Log.e("TAG", "processSet: "+model.message.toString())
//            ToastMsg(model.message.toString())

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }

            R.id.txtUpdate -> {

                if (Validation()) {
                    var body: MultipartBody.Part? = null
                    if (selectedImageFile != null) {
                        val requestBody: RequestBody = selectedImageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        body = MultipartBody.Part.createFormData("image", selectedImageFile!!.name, requestBody)

                        viewModel.profileEditUser(binding.edtName.text.toString(), binding.edtLastName.text.toString(), body)


                    } else {
                        val requestBody: RequestBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        body = MultipartBody.Part.createFormData("image", "", requestBody)

                        viewModel.profileEditUser(binding.edtName.text.toString(), binding.edtLastName.text.toString(), body)

                    }
                }

            }

            R.id.imgEdit -> {
                imagePickerDialog()
            }

        }
    }


    private fun Validation(): Boolean {

        if (binding.edtName.text.toString().equals("")) {
            ToastMsg("Enter first name")
            return false
        }
        if (binding.edtLastName.text.toString().equals("")) {
            ToastMsg("Enter last name")
            return false
        }

        return true
    }

    private fun imagePickerDialog() {
        val builder = AlertDialog.Builder(
            this@EditProfileActivity,
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
            choosePhotoFromGallary()
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
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
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
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
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
                Glide.with(this).load(uri)
                    .placeholder(R.mipmap.img_place_holder)
                    .error(R.mipmap.img_place_holder)
                    // .trnsform(RotateTransformation(this, 90f))
                    .into(binding.imgProfile)
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            mResultsBitmap = let { BitmapUtils.resamplePic(it, mTempPhotoPath) }
            val uri: Uri = getImageUri(mResultsBitmap)
            selectedUri = uri
            selectedImageData = uri.toString()
            selectedImageFile = File(getRealPathFromURI(selectedUri)!!)
            Log.e("LOG_TAG", " selected image url: " + selectedUri)
            Log.e("Select", "CAM_REQUEST $uri")
            Glide.with(this).load(uri)
                .placeholder(R.mipmap.img_place_holder)
                .error(R.mipmap.img_place_holder)
                // .trnsform(RotateTransformation(this, 90f))
                .into(binding.imgProfile)
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