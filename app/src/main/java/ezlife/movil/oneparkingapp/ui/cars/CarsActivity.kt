package ezlife.movil.oneparkingapp.ui.cars

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.CarsBinding
import javax.inject.Inject

class CarsActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var navigation: CarsNavigationController
    lateinit var binding: CarsBinding
    private val firstTime: Boolean by lazy { intent.extras?.getBoolean(EXTRA_FIRST_TIME, false) ?: false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cars)
        binding.firstTime = firstTime
        if (firstTime) navigation.navigateToAdd(firstTime)
        else {
            setSupportActionBar(binding.toolbar)
            navigation.navigateToList()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        navigation.goToBack(false)
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector

    companion object {
        @JvmStatic
        val EXTRA_FIRST_TIME = "firstTime"
    }
}