package at.fhjoanneum.photo_spots.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.fhjoanneum.photo_spots.*
import at.fhjoanneum.photo_spots.databinding.FragmentMapBinding
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private var _binding: FragmentMapBinding? = null

    private lateinit var mMap: GoogleMap

    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private lateinit var applicationContext: Context

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap ->
            onMapReady(googleMap)
        }
        val appActivity = activity
        if (appActivity != null) {
            applicationContext = appActivity.getApplicationContext()
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appActivity)
        }

        return rootView


    }

    fun onMapReady(googleMap: GoogleMap) {
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

            val id = it.tag.toString()
            val db = LoginModelDatabase.getDatabase(requireContext())
            val username = db.loginModelDao.getUserName().toString()

            PostRepository.getPhotoByID(requireContext(),id.toInt(),
                success = {
                    if(it.UserName == username){

                        val intent = Intent(requireContext(), EditPostActivity::class.java)
                        intent.putExtra(DashboardFragment.TAG_ID, id)

                        intent.putExtra(DashboardFragment.TAG_BOOL, false)
                        startActivity(intent)
                    }else{
                        val viewPicIntent = Intent(requireContext(), ViewLocationActivity::class.java)
                        viewPicIntent.putExtra(TAG_ID, id)
                        startActivity(viewPicIntent)
                    }

                },
                error = {
                    val viewPicIntent = Intent(requireContext(), ViewLocationActivity::class.java)
                    viewPicIntent.putExtra(TAG_ID, id)
                    startActivity(viewPicIntent)
                }
            )


        }
    }

    //Fast gleicher Code wie PostPictureActivity
    //Falls irgendwer eine Lösung findet auf die Funktionen in der anderen Activity zuzugreifen, wäre es super :)
    //Einzige Änderung ist, dass bei getUserLocation das Ganze auf der Map dargestellt wird und nicht in den TextView kommt

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkGPS()
        } else {
            //permission denied
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            Toast.makeText(applicationContext, "no permissions granted!", Toast.LENGTH_SHORT).show()
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
                        //resolveApiException.startResolutionForResult(, 200)

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
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->

            val location = task.getResult()

            if (location != null){

                try {
                    val geocoder = Geocoder(applicationContext, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    //set address
                    val addressLine = address[0].getAddressLine((0))
                    val addressLocation = address[0].getAddressLine(0)

                    val currentPosition = LatLng(location.latitude, location.longitude)

                    mMap.addMarker(
                        MarkerOptions().position(currentPosition).title(addressLine).snippet("Your current Location").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))?.setTag("001")

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,10f))



                } catch (exception: IOException) {

                }

            }


        }
        return
    }




    fun setExistingMarkers() {

        PostRepository.getphotoList(applicationContext,
            success = {
            // handle success
           for (location in it) {
               val latitude = location.LocationLatitude
               val longitude = location.LocationLongitude
               mMap.addMarker(
                   MarkerOptions()
                       .position(LatLng(latitude,longitude))
                       .title(location.Title)
                       .snippet(location.Location)
               )?.setTag(location.Id)
           }
        },
            error = {
                // handle error
                Log.e("API ERROR",it)
            })
    }

    companion object {
        const val TAG_ID = "TAG"
        const val TAG_BOOL = "TAG_BOOL"
    }
}
