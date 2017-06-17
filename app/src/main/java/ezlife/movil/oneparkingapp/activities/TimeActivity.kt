package ezlife.movil.oneparkingapp.activities

import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.db.Reserve
import java.text.SimpleDateFormat
import java.util.*

class TimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
    }

    fun addTime(){

    }

    fun stopTime(){

    }

    companion object{

        @JvmStatic
        @BindingAdapter("app:timeComplete")
        fun setFullTime(txt: TextView, reserve: Reserve) {
            val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val fromTxt = format.format(reserve.fecha)
            val time = reserve.fecha.time + (reserve.tiempo * 1000)
            val toTxt = format.format(time)
            txt.text = "$fromTxt - $toTxt"
        }

    }
}
