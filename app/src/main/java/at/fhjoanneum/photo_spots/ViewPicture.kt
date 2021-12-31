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
import org.w3c.dom.Text
import java.io.File
import java.io.IOException
import java.util.*

class ViewPicture : AppCompatActivity() {

    private var PERMISSION_ID = 1000

    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)

        val imageUriString: String = intent.getStringExtra(CameraActivity.IMAGE_URI).toString()
        //Toast.makeText(this, imageUriString, Toast.LENGTH_SHORT).show()
        val imageUri: Uri = imageUriString.toUri()
        if (imageUriString != "empty") {

            Glide
                .with(this)
                .load(imageUri)
                .into(findViewById(R.id.viewpic_imageview))
        }

        findViewById<TextView>(R.id.viewpic_textview_altitude).setText(intent.getStringExtra(CameraActivity.IMAGE_LATITUDE))

        val image = findViewById<ImageView>(R.id.viewpic_imageview)
        val imageFile: File = imageUri.toFile()

        val exifInterface: ExifInterface = ExifInterface(File(imageUri.getPath()).absolutePath)
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

        findViewById<Button>(R.id.viewpic_button_categoryadd).setOnClickListener() {
            addCategory()
        }


    }

    private fun addCategory() {
        var insertText: String = findViewById<EditText>(R.id.viewpic_edittext_category).getText().toString()
        var previousText: String = "test"
        if (findViewById<TextView>(R.id.viewpic_textview_cat1).text == "") {
            findViewById<TextView>(R.id.viewpic_textview_cat1).setText(insertText)
        } else if (findViewById<TextView>(R.id.viewpic_textview_cat2).text == "") {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).getText().toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).setText(insertText)
            findViewById<TextView>(R.id.viewpic_textview_cat2).setText(previousText)

        } else if (findViewById<TextView>(R.id.viewpic_textview_morecat).text == "") {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).getText().toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).setText(insertText)
            findViewById<TextView>(R.id.viewpic_textview_cat2).setText(previousText)
            findViewById<TextView>(R.id.viewpic_textview_morecat).setText("and more...")
        } else {
            previousText = findViewById<TextView>(R.id.viewpic_textview_cat1).getText().toString()
            findViewById<TextView>(R.id.viewpic_textview_cat1).setText(insertText)
            findViewById<TextView>(R.id.viewpic_textview_cat2).setText(previousText)
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

            val location = task.getResult()

            if (location != null){
                Toast.makeText(this, "${location.latitude},${location.longitude}, ${location.altitude}", Toast.LENGTH_SHORT).show()

                try {
                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    //set address
                    val addressLine = address[0].getAddressLine((0))
                    val addressLocation = address[0].getAddressLine(0)
                    findViewById<TextView>(R.id.viewpic_textview_altitude).setText(location.altitude.toString())
                    findViewById<TextView>(R.id.viewpic_textview_latitude).setText(location.latitude.toString())
                    findViewById<TextView>(R.id.viewpic_textview_longitude).setText(location.altitude.toString())
                    findViewById<TextView>(R.id.viewpic_textview_address).setText(addressLocation)


                } catch (exception: IOException) {

                }

            }



        }

    }




}