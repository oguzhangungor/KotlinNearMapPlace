package com.ogungor.nearplaces

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ogungor.nearplaces.Common.Common
import com.ogungor.nearplaces.Helper.DirectionJSONParser
import com.ogungor.nearplaces.Remote.IGoogleAPIService
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ViewDirectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        private val MY_PERMISSION_CODE = 1
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    lateinit var mLastLocation:Location

    lateinit var mService: IGoogleAPIService

    lateinit var mCurrentMarker: Marker


     var polyline:Polyline?=null



    override fun onRequestPermissionsResult(


        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ViewDirectionActivity.MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (checkLocationPermission()) {

                            buildLocationRequest()
                            buildLocationCallBack()


                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest, locationCallback,
                                Looper.myLooper()
                            )

                            mMap.isMyLocationEnabled = true
                        }
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {



        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ViewDirectionActivity.MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ViewDirectionActivity.MY_PERMISSION_CODE
                )


            return false
        } else
            return true

    }

    @SuppressLint("RestrictedApi")
    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }



    private fun buildLocationCallBack() {

        // Son lokasyonu alıyoruz
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                mLastLocation = location!!.lastLocation;

                //Add your Location Map
                val markerOptions = MarkerOptions()
                    .position(LatLng(mLastLocation.latitude,mLastLocation.longitude))
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                mCurrentMarker = mMap!!.addMarker(markerOptions)


                //Move Camera

                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastLocation.latitude,mLastLocation.longitude)))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))


                //Create Marker for destination location

                val destinationLatlng= LatLng(Common.currentResult!!.geometry!!.location!!.lat.toDouble(),
                    Common.currentResult!!.geometry!!.location!!.lng.toDouble())

                mMap.addMarker(MarkerOptions().position(destinationLatlng)
                    .title(Common.currentResult!!.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                )


                //Get director

                drawPath(mLastLocation,Common.currentResult!!.geometry!!.location)


            }
        }

    }

    private fun drawPath(mLastLocation: Location?, location: com.ogungor.nearplaces.Model.Location?) {
        if (polyline!=null){
            polyline!!.remove() // Old direction remove
        }
        else {
            val origin= StringBuilder(mLastLocation!!.latitude.toString())
                .append(",")
                .append(mLastLocation!!.longitude.toString())
                .toString()

            val destination= StringBuilder(location!!.lat.toString())
                .append(",")
                .append(location!!.lng.toString())
                .toString()

            mService.getDirection(origin,destination)
                .enqueue(object :Callback<String>
                {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        ParserTask().execute(response.body()!!.toString())
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("ERROR",t.message.toString())
                    }

                })
        }
    }

   inner class ParserTask(): AsyncTask<String,Int,List<List<HashMap<String,String>>>> (){

        internal val waitingDialog: android.app.AlertDialog = SpotsDialog(this@ViewDirectionActivity)

       override fun onPreExecute() {
           super.onPreExecute()
           waitingDialog.setMessage("Please waiting...")
       }

       override fun doInBackground(vararg p0: String?): List<List<HashMap<String, String>>>? {
            val jsonObjects:JSONObject
           var routes: List<List<HashMap<String, String>>>?=null
           try {
               jsonObjects= JSONObject(p0[0])
               val parser= DirectionJSONParser()
               routes=parser.parse(jsonObjects)

           }catch (e: JSONException){
               e.printStackTrace()
           }

           return routes
        }

       override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
           super.onPostExecute(result)

           var points:ArrayList<LatLng>?=null
           var polylineOptions: PolylineOptions?=null

           for (i in result!!.indices){
               points= ArrayList()
               polylineOptions= PolylineOptions()

               val path= result[i]

               for (j in path!!.indices){
                   val point=path[j]
                   val lat = point["lat"]!!.toDouble()
                   val lng = point["lng"]!!.toDouble()
                   val position=LatLng(lat, lng)


                   points.add(position)


               }


               polylineOptions.addAll(points)
               polylineOptions.width(12F)
               polylineOptions.color(Color.BLUE)
               polylineOptions.geodesic(true)

           }

           polyline=mMap.addPolyline(polylineOptions)
           waitingDialog.dismiss()
       }
   }


    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_direction)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //init unit
        mService = Common.googleAPIServiceScalars

        //runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
            )
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            mLastLocation=location
            val markerOptions = MarkerOptions()
                .position(LatLng(mLastLocation.latitude,mLastLocation.longitude))
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            mCurrentMarker = mMap!!.addMarker(markerOptions)


            //Move Camera

            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastLocation.latitude,mLastLocation.longitude)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))


            //Create Marker for destination location

            val destinationLatlng= LatLng(Common.currentResult!!.geometry!!.location!!.lat.toDouble(),
                Common.currentResult!!.geometry!!.location!!.lng.toDouble())

            mMap.addMarker(MarkerOptions().position(destinationLatlng)
                .title(Common.currentResult!!.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

            )


            //Get director

            drawPath(mLastLocation,Common.currentResult!!.geometry!!.location)




        }




    }
}