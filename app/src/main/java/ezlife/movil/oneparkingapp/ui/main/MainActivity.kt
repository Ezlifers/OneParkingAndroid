package ezlife.movil.oneparkingapp.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.toast
import ezlife.movil.oneparkingapp.data.api.model.ZoneState
import ezlife.movil.oneparkingapp.data.observer.MarkObserver
import ezlife.movil.oneparkingapp.databinding.MainBinding
import ezlife.movil.oneparkingapp.util.LifeDisposable
import ezlife.movil.oneparkingapp.util.subscribeWithError
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

//TODO: Crear un observable que revise o emita un cambio de tipo en el marker free->tarification
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, OnMapReadyCallback {

    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var navigation: MainNavigationController
    @Inject
    lateinit var markObserver: MarkObserver
    lateinit var binding: MainBinding
    private val states: PublishSubject<LatLng> = PublishSubject.create()
    private var map: GoogleMap? = null
    private var markers: MutableMap<String, Marker> = mutableMapOf()


    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tracing = false


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()

        dis add btnMoney.clicks()
                .subscribe { }

        dis add btnCar.clicks()
                .subscribe { navigation.showDialogCar() }

        dis add btnLocation.clicks()
                .subscribe { }

        dis add viewModel.selectedCar()
                .subscribe { binding.car = it }

        dis add viewModel.getCash()
                .subscribeWithError(
                        onNext = { binding.money = it },
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )

        dis add viewModel.verifyCurrentSetup()
                .subscribeWithError(
                        onNext = navigation::showDialogSetup,
                        onError = { },
                        onHttpError = { }
                )

        dis add states
                .flatMap(viewModel::currentState)
                .subscribeWithError(
                        onNext = this::addMarkers,
                        onError = { toast(it.message!!) },
                        onHttpError = this::toast
                )

        dis add markObserver.markerState
                .subscribe {
                    val marker = markers[it.id]
                    val state = marker?.tag as ZoneState
                    state.tipo = it.state
                    changeColor(state, marker)
                }

    }

    //region Map
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        //if (checkLocationPermission() && requestingLocationUpdates) {
        //  map?.isMyLocationEnabled = true
        //}
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isZoomGesturesEnabled = false
        //map?.setOnCameraMoveStartedListener { reason ->
        //    movedByUser = reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
        //}
        map?.setOnCameraMoveListener {
            //  if (movedByUser) {
            //    stopLocationUpdates()
            //  val position = map!!.cameraPosition.target
            //validateDistance(position.latitude, position.longitude)
            //}
        }
        map?.setOnMarkerClickListener { marker ->
            val state = marker.tag as ZoneState
            navigation.showDialogZone(state, viewModel.day)
            true
        }
        //if (pendingStates != null) {
        //  addMarkers(pendingStates!!)
        // pendingStates = null
        // }
        //if (lastLocation != null)
        //  moveCamera(LatLng(lastLocation!!.latitude, lastLocation!!.longitude), false)
        //else
        moveCamera(viewModel.centerPosition, false)
        states.onNext(viewModel.centerPosition)
    }

    private fun moveCamera(latLng: LatLng, animated: Boolean) {
        if (animated) map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
        else map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
    }
    //endregion

    //region States
    private fun addMarkers(states: List<ZoneState>) {
        states.forEach {
            val marker: Marker =
                    if (markers.containsKey(it._id)) markers[it._id]!!
                    else map!!.addMarker(MarkerOptions()
                            .position(LatLng(it.localizacion.coordinates[1],
                                    it.localizacion.coordinates[0])))

            marker.tag = it
            markers[it._id] = marker
            changeColor(it, marker)
        }
    }

    private fun changeColor(zoneState: ZoneState, marker: Marker) {
        when (zoneState.tipo) {
            ZoneState.FREE -> {
                setMarkerColor(marker, BitmapDescriptorFactory.HUE_AZURE)
            }
            ZoneState.PROHIBITED -> {
                setMarkerColor(marker, BitmapDescriptorFactory.HUE_RED)
            }
            ZoneState.TARIFICATION -> {
                val state = zoneState.estado!!
                if (viewModel.isDisability() && (state.bahiasOcupadas < state.bahias || state.disOcupadas < state.dis)) {
                    setMarkerColor(marker, BitmapDescriptorFactory.HUE_GREEN)
                } else if (state.bahiasOcupadas < state.bahias) {
                    setMarkerColor(marker, BitmapDescriptorFactory.HUE_GREEN)
                } else {
                    setMarkerColor(marker, BitmapDescriptorFactory.HUE_ORANGE)
                }
            }
        }
    }

    private fun setMarkerColor(marker: Marker, value: Float) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(value))
    }
    //endregion

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector

    companion object {
        private val ZOOM = 17F
    }

}
