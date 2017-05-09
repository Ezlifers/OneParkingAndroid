package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.AddCarBinding
import ezlife.movil.oneparkingapp.providers.Car
import ezlife.movil.oneparkingapp.providers.CarProvider
import ezlife.movil.oneparkingapp.util.Preference
import ezlife.movil.oneparkingapp.util.text

class AddCarActivity : AppCompatActivity() {

    lateinit var binding: AddCarBinding
    lateinit var newCar: Car
    val firstTime: Boolean by lazy { intent.extras?.getBoolean(EXTRA_FIRST_TIME, false) ?: false }
    val provider: CarProvider by lazy { CarProvider(this, makeLoading()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car)
        binding.handler = this
        binding.firstTime = firstTime
        if (!firstTime) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun add() {
        val plate = "${binding.plate.text()}"
        val brand = "${binding.brand.text()}"
        val nickname = "${binding.brand.text()}"

        if (plate == "" || brand == "" || nickname == "") {
            toast(R.string.report_form)
            return
        }

        newCar = Car(nickname, plate, brand)
        provider.insertCar(newCar) { success, outRange ->
            when (success) {
                true -> {
                    toast(R.string.car_insert)
                    if (firstTime) {
                        savePreference(Preference.USER_LOGGED to true)
                        startActivity<MapActivity>()
                    }
                }
                false -> toast(if (outRange) R.string.car_insert_error_2 else R.string.car_insert_error)
            }
            finish()
        }
    }

    companion object {
        @JvmStatic val EXTRA_FIRST_TIME = "firstTime"
    }
}
