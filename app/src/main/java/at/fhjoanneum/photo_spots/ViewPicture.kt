package at.fhjoanneum.photo_spots


import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.*
import android.media.ExifInterface
import android.media.ExifInterface.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import okhttp3.MediaType
import java.io.File
import java.io.IOException
import java.util.*
import okhttp3.RequestBody

import okhttp3.MultipartBody




class ViewPicture : AppCompatActivity() {

    private var PERMISSION_ID = 1000
    private var photoLongitude:Double = 0.0
    private var photoLatitude:Double = 0.0
    private var  photoAltitude:Double = 0.0
    private var addressLocation:String = ""
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)

        val imageUriString: String = intent.getStringExtra(CameraActivity.IMAGE_URI).toString()
        Toast.makeText(this, imageUriString, Toast.LENGTH_SHORT).show()
        val imageUri: Uri = imageUriString.toUri()
        if (imageUriString != "empty") {

            Glide
                .with(this)
                .load(imageUri)
                .into(findViewById(R.id.viewpic_imageview))
        }


        val image = findViewById<ImageView>(R.id.viewpic_imageview)
        val imageFile: File = imageUri.toFile()

        val exifInterface: ExifInterface = ExifInterface(File(imageUri.path).absolutePath)
        val longitude = exifInterface.getAttribute(TAG_GPS_LONGITUDE)
        val latitude = exifInterface.getAttribute(TAG_GPS_LATITUDE)
        val test = exifInterface.getAttribute(TAG_DATETIME)

        //Toast.makeText(this, longitude + latitude + test, Toast.LENGTH_SHORT).show()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        findViewById<Button>(R.id.viewpic_button_retake).setOnClickListener(){
            val retakeIntent = Intent(this, CameraActivity::class.java)
            startActivity(retakeIntent)
        }
        findViewById<Button>(R.id.viewpic_button_post).setOnClickListener(){



            val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val body = MultipartBody.Part.createFormData("upload", imageFile.name, reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload_test")

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            val photoTitle = findViewById<EditText>(R.id.viewpic_edittext_title).text.toString()
            val description = findViewById<EditText>(R.id.viewpic_edittext_description).text.toString()
            val postData = UploadPostModel(photoTitle, "", description, addressLocation,photoLongitude, photoLatitude, photoAltitude)
            uploadPicture(body, postData)
            //uploadPost(postData)


        }

        findViewById<Button>(R.id.viewpic_button_categoryadd).setOnClickListener() {
            addCategory()
        }


    }

    private fun uploadPost(post:UploadPostModel){
        UploadNewPost(post,this,
            success = {
                // handle success
                Log.e("POST","SUCCESS")
                finish()
            },
            error = {
                // handle error
                Log.e("API",it)
                val toast = Toast.makeText(applicationContext, "Post failed, please try again later", Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }

    private fun uploadPicture(picture: MultipartBody.Part, postData :UploadPostModel){
        //var path = "ERROR"
        UploadPicture(picture,this,
            success = {
                // handle success
                Log.e("POST",it)
                postData.Photo = it
                uploadPost(postData)
            },
            error = {
                // handle error
                Log.e("API",it)
                val toast = Toast.makeText(applicationContext, "Post failed, please try again later", Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }

    private fun addCategory() {
        var insertText: String = findViewById<EditText>(R.id.viewpic_edittext_category).text.toString()
        var previousText: String = "test"
        if (findViewById<TextView>(R.id.viewpic_textview_cat1).text == "") {
            findViewById<TextView>(R.id.viewpic_textview_cat1).text = insertText
        } else if (findViewById<TextView>(R.id.viewpic_textview_cat2).text == "") {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).text.toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).text = insertText
            findViewById<TextView>(R.id.viewpic_textview_cat2).text = previousText

        } else if (findViewById<TextView>(R.id.viewpic_textview_morecat).text == "") {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).text.toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).text = insertText
            findViewById<TextView>(R.id.viewpic_textview_cat2).text = previousText
            findViewById<TextView>(R.id.viewpic_textview_morecat).text = "and more..."
        } else {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).text.toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).text = insertText
            findViewById<TextView>(R.id.viewpic_textview_cat2).text = previousText
        }
        findViewById<EditText>(R.id.viewpic_edittext_category).setText("")
    }

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            checkGPS()
            //Toast.makeText(this, "permissions granted!", Toast.LENGTH_SHORT).show()
        } else {
            //permission denied
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            Toast.makeText(this, "no permissions granted!", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkGPS() {
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(
            this.applicationContext
        )
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->

            try {
                //GPS is on

                val response = task.getResult(
                    ApiException::class.java
                )
                //Toast.makeText(this, "OnCompleteListener", Toast.LENGTH_SHORT).show()
                getUserLocation()

            } catch (exception : ApiException) {
                //GPS not on
                exception.printStackTrace()

                when(exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        //send request for enabling GPS
                        val resolveApiException = exception as ResolvableApiException
                        resolveApiException.startResolutionForResult(this, 200)

                    } catch (sendIntentException : IntentSender.SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        //setting unavailable
                    }

                }
            }

        }


    }

    fun getUserLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->

            val location = task.result

            if (location != null){
                //Toast.makeText(this, "${location.latitude},${location.longitude}, ${location.altitude}", Toast.LENGTH_SHORT).show()

                try {
                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    //set address
                    addressLocation = address[0].getAddressLine(0)
                    photoAltitude = location.altitude
                    photoLatitude = location.latitude
                    photoLongitude = location.longitude

                    //val addressLine = address[0].getAddressLine((0))
                    findViewById<TextView>(R.id.viewpic_textview_altitude).text = photoAltitude.toString()
                    findViewById<TextView>(R.id.viewpic_textview_latitude).text = photoLatitude.toString()
                    findViewById<TextView>(R.id.viewpic_textview_longitude).text = photoLongitude.toString()
                    findViewById<TextView>(R.id.viewpic_textview_address).text = addressLocation


                } catch (exception: IOException) {
                    Log.e("IO", "IOEXCEPTION")
                }

            }else{
                Toast.makeText(this, "Location Service Not available!!", Toast.LENGTH_SHORT).show()
            }



        }

    }




}