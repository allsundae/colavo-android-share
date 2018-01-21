package com.colavo.android.ui.customerdetail

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
import com.colavo.android.ui.PlaceholderFragment04
import android.view.*
import com.colavo.android.App
import com.colavo.android.base.BaseFragment
import com.colavo.android.utils.toast
import javax.inject.Inject
import com.colavo.android.entity.customer.ImageUrl
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customerdetail.CustomerCreatePresenterImpl
import com.colavo.android.utils.showSnackBar
import kotlinx.android.synthetic.main.customer_create.*
import android.support.design.widget.BottomSheetBehavior
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.widget.Toast
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.OnProgressListener
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import kotlin.reflect.jvm.internal.impl.load.java.lazy.ContextKt.child
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.provider.MediaStore
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.system.ErrnoException
import android.util.Log
import com.theartofdev.edmodo.cropper.CropImage.getPickImageChooserIntent
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class CustomerCreateFragment : BaseFragment(), CustomerCreateView {
    @Inject
    lateinit var customerCreatePresenter: CustomerCreatePresenterImpl

    var imageURL : ImageUrl = ImageUrl("","")
    private var filePath: Uri? = null
    private var mCropImageView: CropImageView? = null
    private var mCropImageUri : Uri? = null

    override fun getLayout() = R.layout.customer_create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as App).addCustomerComponent().inject(this)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle = arguments!!

        val salon = bundle.getSerializable(PlaceholderFragment04.EXTRA_SALON) as SalonModel
        customerCreatePresenter.attachView(createCustomerView = this)

        input_phone.setEmptyDefault("")

        mCropImageView = CropImageView//(CropImageView) findViewById(R.id.CropImageView);
//TODO        create_customer_image.setOnClickListener{customerCreatePresenter.createCustomerImage()}
        doneButton.setOnClickListener{ checkField(salon) }

        touch_outside.setOnClickListener({ v -> dismissFragment() }) //(activity as AppCompatActivity).finish() })
        BottomSheetBehavior.from(bottom_sheet)
                .setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> dismissFragment()//(activity as AppCompatActivity).finish()
                          //  BottomSheetBehavior.STATE_EXPANDED ->   setStatusBarDim(false)
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        // no op
                    }
                })
   //     create_customer_image.setOnClickListener({ v -> dismissFragment() })

        create_customer_image.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
/*                val intent = Intent()
                intent.type = "image*//*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0)*/
                CropContainer.visibility=View.VISIBLE
                startActivityForResult(getPickImageChooserIntent(), 200)
            }
        })

        cropButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onCropImageClick (view)
            }
        })
    }

    private fun dismissFragment() {
        //if Keyboard is visible, hide it
        val view = (activity as AppCompatActivity).currentFocus
        if (view != null) {
            val imm = (activity as AppCompatActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }

        // back to parent view
        fragmentManager?.popBackStackImmediate()
    }

    private fun checkField(salon: SalonModel) {
        var myInternationalNumber: String =""

        if (input_name.text.toString()=="") {
            showSnackbar (getString(R.string.err_name))
        }
        else if (input_phone.text.toString()=="") {
            uploadFile()
            customerCreatePresenter.createCustomer(salon.id, input_name.text.toString(), myInternationalNumber, imageURL) //input_phone.text.toString()
        }
        else{
            if (input_phone.isValid() ) {
                myInternationalNumber = input_phone.getNumber()
                uploadFile()
                customerCreatePresenter.createCustomer(salon.id, input_name.text.toString(), myInternationalNumber, imageURL) //input_phone.text.toString()
            }
            else
            {
                showSnackbar (getString(R.string.err_phone_notvalid))
            }
        }

    }

    //결과 처리
/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data!!.data
            Log.d(TAG, "uri:" + filePath.toString())
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                val bitmap = MediaStore.Images.Media.getBitmap( (context!!.applicationContext as App).getContentResolver(), filePath)
                create_customer_image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }*/

    //upload the file
    private fun uploadFile() {
        showToast(filePath.toString())

        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("업로드중...")
            progressDialog.show()

            //storage
            val storage = FirebaseStorage.getInstance()

            //Unique한 파일명을 만들자.
            val formatter = SimpleDateFormat("yyyyMMHH_mmss")
            val now = Date()
            val filename = "profile" + formatter.format(now) + ".png"
            //storage 주소와 폴더 파일명을 지정해 준다.
            val storageRef = storage.getReferenceFromUrl("gs://jhone-364e5.appspot.com").child("images/" + filename)
            //올라가거라...
            storageRef.putFile(filePath!!)
                    //성공시
                    .addOnSuccessListener {
                        progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                        showToast ("Upload completed.") //getString(R.string.success_create_customer)

                    }
                    //실패시
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        showToast ("Upload failed.") //getString(R.string.success_create_customer)
                    }
                    //진행중
                    .addOnProgressListener { taskSnapshot ->
                        val progress = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()//이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                    }
        } else {
            showToast ("Select photo first.") //getString(R.string.success_create_customer)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        customerCreatePresenter.onDestroy()
        //clearCustomerDetailComponent()
    }
    override fun onError(throwable: Throwable) = (activity as AppCompatActivity).toast("${throwable.message}")

    override fun onCreatedSuccess() {
        showToast (getString(R.string.success_create_customer))
        dismissFragment()
    }
    override fun onCreatedFailed() {
        showToast (getString(R.string.err_create_customer))
        dismissFragment()
    }

    override fun showToast(event: String) =  (activity as AppCompatActivity).toast(event)

    override fun showSnackbar(event: String) {
        create_customer.showSnackBar(event)
    }
    override fun showCreateProgress() {
        doneButton.visibility = View.GONE
        doneProgress.visibility = View.VISIBLE
    }

    override fun hideCreateProgress() {
        doneButton.visibility = View.VISIBLE
        doneProgress.visibility = View.GONE
    }


    fun getPickImageChooserIntent(): Intent {
            val outputFileUri = getCaptureImageOutputUri()

            val allIntents = ArrayList<Intent>()
            val packageManager = (activity as AppCompatActivity).getPackageManager()
            val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.`package` = res.activityInfo.packageName
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }
                allIntents.add(intent)
            }
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.`package` = res.activityInfo.packageName
                allIntents.add(intent)
            }
            var mainIntent = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.colavo.ui.SalonMainActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)
            val chooserIntent = Intent.createChooser(mainIntent, "Select source")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray<Parcelable>())

            return chooserIntent
        }

    /**
     * Get URI to image received from capture by camera.
     */
    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage = (activity as AppCompatActivity).getExternalCacheDir()
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage!!.getPath(), "pickImageResult.jpeg"))
        }
        return outputFileUri
    }

    fun getPickImageResultUri(data: Intent?): Uri? {
        var isCamera = true
        if (data != null && data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera) getCaptureImageOutputUri() else data!!.data
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br></br>
     */
    fun isUriRequiresPermissions(uri: Uri?): Boolean {
        try {
            val resolver = (activity as AppCompatActivity).getContentResolver()
            val stream = resolver.openInputStream(uri!!)
            stream!!.close()
            return false
        } catch (e: FileNotFoundException) {
            if (e.cause is ErrnoException) {
                return true
            }
        } catch (e: Exception) {
        }

        return false
    }

    /**
     * Crop the image and set it back to the cropping view.
     */
    fun onCropImageClick(view: View) {
        val cropped = mCropImageView!!.getCroppedImage(500, 500)
        if (cropped != null) {
            //mCropImageView!!.setImageBitmap(cropped)
            try {
                filePath = returnUrifromTempBitmap(cropped)
            } catch (e: FileNotFoundException) {
                if (e.cause is ErrnoException) {
                    showToast (e.toString())
                }
            } catch (e: Exception) {
                showToast (e.toString())
            }

            create_customer_image.setImageBitmap(cropped)
            CropContainer.visibility = View.GONE
            showToast(filePath.toString())

        }
    }

    fun returnUrifromTempBitmap(bitmap: Bitmap) : Uri{
        var tempDir: File = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.getAbsolutePath()+"/.temp/")
        tempDir.mkdir()

        val tempFile: File = File.createTempFile("profile", ".png", tempDir)
        val bytes   = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

    //write the bytes in file
        val fos : FileOutputStream = FileOutputStream(tempFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return Uri.fromFile(tempFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {

            val imageUri = getPickImageResultUri(data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            var requirePermissions = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    (activity as AppCompatActivity).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true
                mCropImageUri = imageUri
                showToast ("PERMISSION NEEDED" + mCropImageUri.toString())
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }

            if (!requirePermissions) {
                showToast ("Didn't needed PERMISSION " + mCropImageUri.toString())
                mCropImageView!!.setImageUriAsync(imageUri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (mCropImageUri != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val bitmap = MediaStore.Images.Media.getBitmap( (context!!.applicationContext as App).getContentResolver(), mCropImageUri)
//            create_customer_image.setImageBitmap(bitmap)
//            CropContainer.visibility = View.GONE
            showToast ("Didn't needed PERMISSION " + mCropImageUri.toString())
           mCropImageView!!.setImageUriAsync(mCropImageUri)
        } else {
            showToast("Required permissions are not granted")
        }
    }

    /**
     * Get the URI of the selected image from [.getPickImageChooserIntent].<br></br>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */

}


