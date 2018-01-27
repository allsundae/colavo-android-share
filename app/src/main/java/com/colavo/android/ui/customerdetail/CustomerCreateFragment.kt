package com.colavo.android.ui.customerdetail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.colavo.android.R
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
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import com.google.firebase.storage.FirebaseStorage
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.system.ErrnoException
import com.colavo.android.di.net.NetModule.Companion.BASE_STORAGE_URL
import com.colavo.android.entity.customer.CustomerEntity
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.ui.customerdetail.CustomerDetailFragment.Companion.EXTRA_CUSTOMER_DETAIL
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class CustomerCreateFragment() : BaseFragment(), CustomerCreateView {
    @Inject
    lateinit var customerCreatePresenter: CustomerCreatePresenterImpl

    var imageURL : ImageUrl = ImageUrl("","")
    private var filePath: Uri? = null
    private var mCropImageView: CropImageView? = null
    private var mCropImageUri : Uri? = null
    private var customer = CustomerModel()

    override fun getLayout() = R.layout.customer_create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as App).addCustomerComponent().inject(this)
       // setHasOptionsMenu(true)

    }

    override fun refresh() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle = arguments!!

        customerCreatePresenter.attachView(createCustomerView = this)

        val sender : String = bundle.getString("SENDER")
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
        mCropImageView = CropImageView

        if (sender == "edit") {

            customer = bundle.getSerializable(EXTRA_CUSTOMER_DETAIL) as CustomerModel
            if (customer.image_urls.full != ""){
                val byteArray = bundle.getByteArray("BYTE")
                val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                create_customer_image.setImageBitmap(decodedBitmap)
            }

            input_name.setText(customer.name)
            input_phone.setEmptyDefault("customer.phone")
            if (customer.phone != null && customer.phone != "") {
                input_phone.number = customer.phone
            }
        }
        else {
            input_phone.setEmptyDefault(null)
        }



        doneButton.setOnClickListener{ checkField(salon, sender) }

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

        val fragment = CustomerDetailFragment()
        fragment.refresh()


    }

    private fun checkField(salon: SalonModel, editmode: String) {
        var myInternationalNumber: String =""
        var myPlainNumber: String = ""

        if (input_name.text.toString()=="") {
            showSnackbar (getString(R.string.err_name))
        }
        else if (input_phone.text == null) {
            writeNewCustomer(editmode, salon.id, input_name.text.toString(), myInternationalNumber, myPlainNumber, imageURL)
        }
        else{
            if (input_phone.isValid() ) {
                myPlainNumber = input_phone.getText()
                myInternationalNumber = input_phone.getNumber()
                writeNewCustomer(editmode, salon.id, input_name.text.toString(), myInternationalNumber, myPlainNumber, imageURL)
            }
            else
            {
                showSnackbar (getString(R.string.err_phone_notvalid))
            }
        }
    }

    //결과 처리

    //upload the file
    private fun writeNewCustomer(editmode: String, salonKey: String, name: String, international_phone: String, plain_phone: String, imageUrl: ImageUrl) {
        showToast(filePath.toString())
        var newCustomerEntity = CustomerEntity ("",plain_phone, international_phone, name, imageUrl )
        var mDatabase = FirebaseDatabase.getInstance().getReference().child("salon_customers").child(salonKey)
        var newCustomerKey: String = mDatabase.push().key
  /*      val bundle: Bundle = arguments!!
        customer = bundle.getSerializable(EXTRA_CUSTOMER_DETAIL) as CustomerModel*/

        if (editmode == "edit" && customer.uid != "") {
            newCustomerKey = customer.uid
        }

        var imageFull : String = ""
        var imageThumb : String = ""
        var mImage : ImageUrl = ImageUrl()

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
            val filename = "profile" + ".png" //+ formatter.format(now)
            //storage 주소와 폴더 파일명을 지정해 준다.
            val storageRef = storage.getReferenceFromUrl(BASE_STORAGE_URL).child("images/customers/$newCustomerKey/profiles/$filename") //"gs://jhone-364e5.appspot.com"
            //올라가거라...
            storageRef.putFile(filePath!!)
                    //성공시
                    .addOnSuccessListener { taskSnapshot ->
                        progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                        //newCustomerEntity.setImageUrl(taskSnapshot.downloadUrl.toString(), taskSnapshot.downloadUrl.toString())
                        imageFull = taskSnapshot.downloadUrl.toString()
                        imageThumb = taskSnapshot.downloadUrl.toString()
                        mImage = ImageUrl(full = imageFull, thumb = imageThumb)

                        showToast ("Upload completed." ) //getString(R.string.success_create_customer)
                        Logger.log ("Upload completed. : " + taskSnapshot.downloadUrl.toString() + "\n imageFull : " + imageFull ) //getString(R.string.success_create_customer)
                        Logger.log ("(1) mImage : " + mImage.toString() ) //Logger.log ("(2) mImage : " + mImage.toString() + "\t imageFull:" + imageFull )
                        newCustomerEntity = CustomerEntity (newCustomerKey, plain_phone, international_phone, name, mImage )

                        writeNewUser(mDatabase, newCustomerKey, newCustomerEntity)
                    }
                    //실패시
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        showToast ("Upload failed. Please try again.") //getString(R.string.success_create_customer)
                    }
                    //진행중
                    .addOnProgressListener { taskSnapshot ->
                        val progress = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()//이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                    }
        } else {
            //showToast ("Select photo first.") //getString(R.string.success_create_customer)
            writeNewUser(mDatabase, newCustomerKey, newCustomerEntity)
        }
    }

    private fun writeNewUser(mDatabase: DatabaseReference, newCustomerKey: String, newCustomerEntity: CustomerEntity) {
        mDatabase.child(newCustomerKey).setValue(newCustomerEntity).addOnSuccessListener {
            onCreatedSuccess()
        }.addOnFailureListener {
            onCreatedFailed()
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
        //dismissFragment()
    }

    override fun showToast(event: String) {
        context?.toast(event)
    }

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
        val cropped = mCropImageView?.getCroppedImage(500, 500)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            val imageUri = getPickImageResultUri(data)
            Logger.log("onActivityResult: imageUri : ${imageUri}")

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            var requirePermissions = false
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ((activity as AppCompatActivity).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            (activity as AppCompatActivity).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                    isUriRequiresPermissions(imageUri))*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ((activity as AppCompatActivity).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    (activity as AppCompatActivity).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) )
                    {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true
                mCropImageUri = imageUri
                showToast ("To use this feature, Read/Write external storage permission is needed. " + mCropImageUri.toString())
                //requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        0)
            }

            if (!requirePermissions) {
                Logger.log ("Didn't needed PERMISSION " + mCropImageUri.toString() +"\t ${imageUri.toString()}")
                mCropImageView?.setImageUriAsync(imageUri)
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


