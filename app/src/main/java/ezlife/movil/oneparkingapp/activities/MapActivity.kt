package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.MapBinding
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.fragments.CarsFragment
import ezlife.movil.oneparkingapp.util.asyncUI
import ezlife.movil.oneparkingapp.util.await
import java.text.NumberFormat


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding:MapBinding
    private var mMap: GoogleMap? = null
    private val dao = DB.con.carDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.handler = this
        binding.money = 10_000
        binding.format = NumberFormat.getInstance()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onResume() {
        super.onResume()
        loadSelectedCar()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    fun loadSelectedCar()= asyncUI{
        val car = await { dao.selected() }
        binding.car = car
    }

    fun addMoney() {

    }

    fun selectCar() {
        showDialog(CarsFragment.instance())
    }
}
