package ezlife.movil.oneparkingapp.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.MainBinding
import ezlife.movil.oneparkingapp.util.push
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, OnMapReadyCallback {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var navigation: MainNavigationController
    lateinit var binding: MainBinding

    val dis: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.tracing = false

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()

        dis push btnMoney.clicks()
                .subscribe { }

        dis push btnCar.clicks()
                .subscribe { navigation.showDialogCar() }

        dis push btnLocation.clicks()
                .subscribe { }

        dis push viewModel.selectedCar()
                .subscribe { binding.car = it }

    }

    override fun onStop() {
        super.onStop()
        dis.clear()
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector
}
