package at.fhjoanneum.photo_spots
import at.fhjoanneum.photo_spots.databinding.ActivityMapsBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.IOException
import java.util.*
import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()
        setExistingMarkers()
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
        mMap.setOnInfoWindowClickListener {
            if (it.tag != "001") {
                val viewPicIntent = Intent(this, ViewLocationActivity::class.java)
                viewPicIntent.putExtra(TAG_ID, it.getTag().toString())
                startActivity(viewPicIntent)
            } else {
                val takePicIntent = Intent(this, CameraActivity::class.java)
                startActivity(takePicIntent)
            }



        }
    }

    //Fast gleicher Code wie PostPictureActivity
    //Falls irgendwer eine Lösung findet auf die Funktionen in der anderen Activity zuzugreifen, wäre es super :)
    //Einzige Änderung ist, dass bei getUserLocation das Ganze auf der Map dargestellt wird und nicht in den TextView kommt

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            return checkGPS()
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->

            val location = task.getResult()

            if (location != null){

                try {
                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    //set address
                    val addressLine = address[0].getAddressLine((0))
                    val addressLocation = address[0].getAddressLine(0)


                    //GpsData = GpsDataModel(location.altitude, location.latitude, location.longitude, addressLine)
                    val currentPosition = LatLng(location.latitude, location.longitude)

                    mMap.addMarker(MarkerOptions().position(currentPosition).title(addressLine).snippet("Your current Location").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))?.setTag("001")

                    //mMap.moveCamera(CameraUpdateFactory.zoomIn())
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,10f))

                    //openLocation(addressLocation.toString())


                } catch (exception: IOException) {

                }

            }


        }
        return
    }


    //Auch gerne so umschreiben, dass auf die Daten in der Datenbank zugegriffen wird
    //Kenne ich mich ehrlich gesagt noch zu wenig aus, um das selbst umzusetzen, danke :)
    //LocationRepository habe ich nur zu Testzwecken erstellt, könnt ihr gerne rausnehmen


    fun setExistingMarkers() {
/*
        val locationIds = LocationRepository.getLocationIds()
        for (id in locationIds) {
            val location = LocationRepository.locationById(id)
            if (location != null) {
                val latitude = location.GpsData.Latitude
                val longitude = location.GpsData.Longitude
                mMap.addMarker(MarkerOptions()
                    .position(LatLng(latitude,longitude))
                    .title(location.title)
                    .snippet(location.GpsData.Address)
                )?.setTag(location.id)
            }
        }

 */
    }

    companion object {
        const val TAG_ID = "TAG"
    }
}