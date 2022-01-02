package at.fhjoanneum.photo_spots

import android.content.Intent
import android.widget.Button

import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.*
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.jar.Manifest

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var frontcamera :Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //Request Permissions



        if(allPermissionGranted()){
            startCameraFront()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        //Setup ClickListener for photo button
        findViewById<Button>(R.id.camera_button_camera).setOnClickListener(){

            takePhoto()


        }
        //OnClickListener for switching Camera
        findViewById<Button>(R.id.camera_button_switch).setOnClickListener(){
            switchCamera()
        }

        findViewById<Button>(R.id.camera_button_flash).setOnClickListener(){
            switchFlash()
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    fun takePhoto() {
        Toast.makeText(this, "Picture Processing...", Toast.LENGTH_SHORT).show()

        var imageUri:String = "empty"
        val imageCapture = imageCapture
        //time-stamped output-file
        val photoFile = File(outputDirectory, java.text.SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
        //Create output options object which contains File + metadata
        val outputOptions = OutputFileOptions.Builder(photoFile).build()
        //setup imaeg capture listener, triggered after photo has been taken
        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object: OnImageSavedCallback{
            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
            }

            override fun onImageSaved(outputFileResults: OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                imageUri = savedUri.toString()
                val msg = "Photo capture successfully: $savedUri"
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)

                startViewPic(IMAGE_URI, imageUri)
            }
        })

    }

    fun startCameraFront() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            //bind lifecycle of camera to lifecycle owner
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()
            //preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(findViewById<PreviewView>(R.id.camera_preview).surfaceProvider)
            }

            imageCapture = Builder().build()
            imageCapture?.setFlashMode(FLASH_MODE_AUTO)
            //select Backcamera as default
            val cameraSelectorFront = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                //unbind
                cameraProvider.unbindAll()
                //bind
                cameraProvider.bindToLifecycle(this, cameraSelectorFront, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this)
        )
    }

    fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED

    }

    fun getOutputDirectory() :File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return  if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionGranted()){
                startCameraFront()
            } else {
                Toast.makeText(this, "Please allow Camera access!",Toast.LENGTH_SHORT).show()
            }
            //finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    fun startViewPic (EXTRA_ID: String, extra: String ) {
        val postPictureIntent = Intent(this, ViewPicture::class.java)
        postPictureIntent.putExtra(EXTRA_ID, extra)
        startActivity(postPictureIntent)
    }

    fun startCameraBack() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            //bind lifecycle of camera to lifecycle owner
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()
            //preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(findViewById<PreviewView>(R.id.camera_preview).surfaceProvider)
            }

            imageCapture = Builder().build()
            imageCapture?.setFlashMode(FLASH_MODE_OFF)
            //select FrontCamera as default
            val cameraSelectorFront = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                //unbind
                cameraProvider.unbindAll()
                //bind
                cameraProvider.bindToLifecycle(this, cameraSelectorFront, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this)
        )
    }

    fun switchCamera() {
        if (frontcamera) {
            startCameraBack()
            frontcamera = false
        } else {
            startCameraFront()
            frontcamera = true
        }

    }
    fun switchFlash() {
        if (imageCapture?.flashMode == FLASH_MODE_AUTO) {
            imageCapture?.flashMode = FLASH_MODE_ON
            Toast.makeText(this, "Set Flash-Mode to on",Toast.LENGTH_SHORT).show()
        }
        else if (imageCapture?.flashMode == FLASH_MODE_ON) {
            imageCapture?.flashMode = FLASH_MODE_OFF
            Toast.makeText(this, "Set Flash-Mode to off",Toast.LENGTH_SHORT).show()
        }
        else if (imageCapture?.flashMode == FLASH_MODE_OFF) {
            imageCapture?.flashMode = FLASH_MODE_AUTO
            Toast.makeText(this, "Set Flash-Mode to auto",Toast.LENGTH_SHORT).show()
        }
    }

    //get gps



    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)

        const val IMAGE_URI = "IMAGE_URI"
        const val IMAGE_LATITUDE = "IMAGE_LATITUDE"
        const val IMAGE_LONGITUDE = "IMAGE_LONGITUDE"
        const val IMAGE_ADDRESS = "IMAGE_ADDRESS"
    }

}