package ezlife.movil.oneparkingapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import ezlife.movil.oneparkingapp.R

class InfoZoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_zone)
    }

    companion object{
        val EXTRA_ID = "zoneId"
    }
}
