package ezlife.movil.oneparkingapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.MapBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.fragments.ReportFragment
import ezlife.movil.oneparkingapp.providers.*
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.SessionApp
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import java.text.NumberFormat
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: MapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationSettingsRequest: LocationSettingsRequest

    private var map: GoogleMap? = null
    private val dao = DB.con.carDao()
    private var lastLocation: Location? = null
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
    private val provider: StateProvider by lazy { StateProvider(this) }
    private val cashProvider: CashProvider by lazy { CashProvider(this) }

    private var markers: Map<String, Marker> = mutableMapOf()
    private var pendingStates: List<ZoneState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.handler = this
        binding.money = 10_000

        val numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 0

        //binding.format = numberFormat
        binding.tracing = false
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        updateStates(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        createLocationRequest()
        createLocationCallback()
        buildLocationSettingsRequest()

        setupApp(this)
        loadMoney()
    }


    //region Cicle Life
    override fun onStart() {
        super.onStart()
        loadSelectedCar()
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission() && requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(KEY_LOCATION, lastLocation)
        outState?.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)

    }

    fun updateStates(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            requestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES)
        }
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

    fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                lastLocation = result.lastLocation
                moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), true)
                validateDistance(lastLocation!!.latitude, lastLocation!!.longitude)
            }
        }
    }

    fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                requestingLocationUpdates = true
                changeLocationFabState(true)
            } else {
                requestingLocationUpdates = false
                changeLocationFabState(false)
                toast(R.string.map_location_disabled)
            }
        }
    }

    fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this) {
                    map?.isMyLocationEnabled = true
                    getLastLocation()
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    requestingLocationUpdates = true
                    changeLocationFabState(true)
                }
                .addOnFailureListener(this) { exception ->
                    val apiException = exception as ApiException
                    val code = apiException.statusCode
                    when (code) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val rae: ResolvableApiException = exception as ResolvableApiException
                            rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            toast(R.string.map_location_error)
                        }
                    }
                }
    }

    fun stopLocationUpdates() {
        if (!requestingLocationUpdates)
            return
        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener {
                    requestingLocationUpdates = false
                    changeLocationFabState(false)
                }
    }

    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null && lastLocation == null) {
                lastLocation = task.result
                moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), false)
                validateDistance(lastLocation!!.latitude, lastLocation!!.longitude)
            }
        }
    }

    fun checkLocationPermission(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    //endregion

    //region Map
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (checkLocationPermission() && requestingLocationUpdates) {
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
            //showDialog(ZoneFragment.instance(state))
            true
        }
        if (pendingStates != null) {
            addMarkers(pendingStates!!)
            pendingStates = null
        }
        if (lastLocation != null)
            moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), false)
        else
            moveCamera(LatLng(CENTER_LAT, CENTER_LNG), false)
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

    //region Car
    fun loadSelectedCar() = asyncUI {
        val car = await { dao.selected() }
        binding.car = car
    }

    //endregion

    //region Money

    fun loadMoney() {

        val cash = preferences().getLong(Preference.USER_CASH, 0)
        binding.money = cash

        cashProvider.getCash { (saldo) ->
            binding.money = saldo
            savePreference(Preference.USER_CASH to saldo)
        }
    }

    fun addMoney() {

    }
    //endregion

    //region Location Fab
    fun checkPermLocation() {
        if (checkLocationPermission()) {
            toggleTracing()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERM_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERM_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleTracing()
            } else {
                toast(R.string.map_location_disabled)
            }
        }
    }

    fun toggleTracing() = when (requestingLocationUpdates) {
        true -> stopLocationUpdates()
        false -> startLocationUpdates()
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

    //region Load States
    fun validateDistance(lat: Double, lng: Double) {
        if (lastRequest == null) {
            lastRequest = LatLng(lat, lng)
            loadStates(lat, lng)
        } else {
            //val meters = SphericalUtil.computeDistanceBetween(lastRequest, LatLng(lat, lng))
            //if (meters >= MIN_METERS)
            //  loadStates(lat, lng, lastRequest?.latitude, lastRequest?.longitude)
        }
    }

    fun loadStates(lat: Double, lng: Double, prevLat: Double? = null, prevLng: Double? = null) {
        lastRequest = LatLng(lat, lng)
        val calendar = Calendar.getInstance()
        val time = (calendar[Calendar.HOUR_OF_DAY] * 60) + calendar[Calendar.MINUTE]
        provider.getStates(day, time, lat, lng, prevLat, prevLng) { states ->
            addMarkers(states)
        }
    }
    //endregion

    //region Attrs & Consts
    companion object {
        private val REQUEST_CHECK_SETTINGS = 1002
        private val REQUEST_PERM_LOCATION = 1003
        private val KEY_LOCATION = "location"
        private val KEY_REQUESTING_LOCATION_UPDATES = "location"
        private val MIN_METERS = 250
        private val CENTER_LAT = 2.4419053
        private val CENTER_LNG = -76.6063383
        private val ZOOM = 17F
    }
    //endregion

}
