package ezlife.movil.oneparkingapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.MapBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.fragments.CarsFragment
import ezlife.movil.oneparkingapp.fragments.GoogleDialogFragment
import ezlife.movil.oneparkingapp.fragments.OnCarSelectListener
import ezlife.movil.oneparkingapp.providers.CurrentState
import ezlife.movil.oneparkingapp.providers.ZoneProvider
import ezlife.movil.oneparkingapp.providers.ZoneState
import ezlife.movil.oneparkingapp.util.SessionApp
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import java.text.NumberFormat
import java.util.*

class MapActivity : AppCompatActivity(), OnCarSelectListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private lateinit var binding: MapBinding
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var googleClient: GoogleApiClient
    private var map: GoogleMap? = null
    private val dao = DB.con.carDao()
    private var lastLocation: Location? = null
    private var resolvingError = false
    private var requestingLocationUpdates = false
    private var lastRequest: LatLng? = null
    private var movedByUser = false
    private val day: Int by lazy {
        val dayWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        when (dayWeek) {
            in Calendar.MONDAY..Calendar.FRIDAY -> 0
            Calendar.SATURDAY -> 1
            Calendar.SUNDAY -> 2
            else -> 0
        }
    }
    private val provider: ZoneProvider by lazy { ZoneProvider(this, null) }
    private var markers: Map<String, Marker> = mutableMapOf()
    private var pendingStates: List<ZoneState>? = null
    private var permLocation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.handler = this
        binding.money = 10_000
        binding.format = NumberFormat.getInstance()
        binding.tracing = false
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        permLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        mapFragment.getMapAsync(this)
        updateStates(savedInstanceState)
        buildGoogleApiClient()
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    //region Cicle Life
    override fun onStart() {
        super.onStart()
        googleClient.connect()
        loadSelectedCar()
    }

    override fun onResume() {
        super.onResume()
        if (googleClient.isConnected && requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (googleClient.isConnected) {
            stopLocationUpdates()
        }
    }

    override fun onStop() {
        googleClient.disconnect()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(STATE_RESOLVING_ERROR, resolvingError)
        outState?.putParcelable(LOCATION, lastLocation)
    }

    fun updateStates(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            resolvingError = savedInstanceState.getBoolean(STATE_RESOLVING_ERROR)
            lastLocation = savedInstanceState.getParcelable(LOCATION)
        }
    }
    //endregion

    //region Google Api Client Callbacks

    fun buildGoogleApiClient() {
        googleClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onConnected(p0: Bundle?) {
        if (lastLocation == null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleClient)
            if (lastLocation == null) {
                moveCamera(LatLng(CENTER_LAT, CENTER_LNG), false)
                validateDistance(CENTER_LAT, CENTER_LNG)
            } else {
                moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), false)
                validateDistance(lastLocation!!.latitude, lastLocation!!.longitude)
            }

        }
    }

    override fun onConnectionSuspended(p0: Int) {
        googleClient.connect()
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        if (resolvingError) {
            return
        } else if (result.hasResolution()) {
            try {
                resolvingError = true
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR)
            } catch (e: IntentSender.SendIntentException) {
                googleClient.connect()
            }
        } else {
            GoogleDialogFragment.show(supportFragmentManager, result.errorCode)
            resolvingError = true
        }
    }

    fun onDialogDismissed() {
        resolvingError = false
    }
    //endregion

    //region Location Setup
    fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 500F
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()
    }

    fun checkLocationSettings() {
        val result = LocationServices.SettingsApi.checkLocationSettings(googleClient,
                locationSettingsRequest)
        result.setResultCallback {
            val status = it.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    startLocationUpdates()
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> status.startResolutionForResult(this
                        , REQUEST_CHECK_SETTINGS)
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> toast(R.string.map_location_error)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationUpdates()
            } else {
                toast(R.string.map_location_disabled)
            }
        }
    }

    fun startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest, this)
                .setResultCallback {
                    requestingLocationUpdates = true
                    changeLocationFabState(true)
                }

    }

    fun stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(googleClient, this)
                .setResultCallback {
                    requestingLocationUpdates = false
                    changeLocationFabState(false)
                }
    }

    override fun onLocationChanged(location: Location) {
        lastLocation = location
        moveCamera(LatLng(location.latitude, location.longitude), true)
        validateDistance(location.latitude, location.longitude)
    }
    //endregion

    //region Map
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (permLocation) {
            map?.isMyLocationEnabled = true
        }
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isZoomGesturesEnabled = false
        map?.setOnCameraMoveStartedListener { reason ->
            movedByUser = reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
        }
        map?.setOnCameraMoveListener {
            if (movedByUser) {
                stopLocationUpdates()
                val position = map!!.cameraPosition.target
                validateDistance(position.latitude, position.longitude)
            }
        }
        map?.setOnMarkerClickListener { marker ->
            val state = marker.tag as ZoneState

            true
        }
        if (pendingStates != null) {
            addMarkers(pendingStates!!)
        }
        if (lastLocation != null)
            moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), false)
    }

    fun moveCamera(latLng: LatLng, animated: Boolean) {
        if (animated)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
        else
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
    }

    fun addMarkers(states: List<ZoneState>) {
        if (map != null) {
            states.forEach {
                val marker: Marker = if (markers.containsKey(it._id)) {
                    markers[it._id]!!
                } else {
                    map!!.addMarker(MarkerOptions().position(LatLng(it.localizacion.coordinates[1], it.localizacion.coordinates[0])))
                }
                marker.tag = it.estado
                changeColor(it.estado, marker)
            }
        } else {
            pendingStates = states
        }
    }

    fun changeColor(state: CurrentState, marker: Marker) {
        if (SessionApp.user.discapasitado && (state.bahiasOcupadas < state.bahias || state.disOcupadas < state.dis)) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        } else if (state.bahiasOcupadas < state.bahias) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        }
    }


    //endregion

    //region Handlers
    fun loadSelectedCar() = asyncUI {
        val car = await { dao.selected() }
        binding.car = car
    }

    fun selectCar() {
        showDialog(CarsFragment.instance())
    }

    override fun onCarSelected() {
        loadSelectedCar()
    }

    fun addMoney() {

    }

    fun checkPermLocation() {
        if (permLocation) {
            toggleTracing()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERM_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERM_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map?.isMyLocationEnabled = true
                toggleTracing()
            } else {
                toast(R.string.map_location_disabled)
            }
        }
    }

    fun toggleTracing() = when (requestingLocationUpdates) {
        true -> stopLocationUpdates()
        false -> checkLocationSettings()
    }

    fun changeLocationFabState(state: Boolean) = when (state) {
        true -> {
            val color = ContextCompat.getColor(this, R.color.colorAccent)
            binding.fabLocation.backgroundTintList = ColorStateList.valueOf(color)
            binding.fabLocation.setImageResource(R.drawable.ic_my_location)
        }
        else -> {
            val color = ContextCompat.getColor(this, android.R.color.white)
            binding.fabLocation.backgroundTintList = ColorStateList.valueOf(color)
            binding.fabLocation.setImageResource(R.drawable.ic_location_disabled)
        }
    }
    //endregion

    fun validateDistance(lat: Double, lng: Double) {
        if (lastRequest == null) {
            lastRequest = LatLng(lat, lng)
            loadStates(lat, lng)
        } else {
            val meters = SphericalUtil.computeDistanceBetween(lastRequest, LatLng(lat, lng))
            if (meters >= MIN_METERS)
                loadStates(lat, lng, lastRequest?.latitude, lastRequest?.longitude)
        }
    }

    fun loadStates(lat: Double, lng: Double, prevLat: Double? = null, prevLng: Double? = null) {
        lastRequest = LatLng(lat, lng)
        val calendar = Calendar.getInstance();
        val time = (calendar[Calendar.HOUR_OF_DAY] * 60) + calendar[Calendar.MINUTE]
        provider.getStates(day, time, lat, lng, prevLat, prevLng) { states ->
            Log.i("ESTADOOS", states.toString())
            addMarkers(states)
        }
    }

    companion object {
        private val REQUEST_RESOLVE_ERROR = 1001
        private val REQUEST_CHECK_SETTINGS = 1002
        private val REQUEST_PERM_LOCATION = 1003
        private val STATE_RESOLVING_ERROR = "resolving_error"
        private val LOCATION = "location"
        private val MIN_METERS = 250
        private val CENTER_LAT = 2.4419053
        private val CENTER_LNG = -76.6063383
        private val ZOOM = 17F
    }
}
