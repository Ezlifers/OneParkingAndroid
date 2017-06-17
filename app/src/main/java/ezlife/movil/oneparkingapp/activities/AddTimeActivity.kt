package ezlife.movil.oneparkingapp.activities

import android.databinding.BindingAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.util.toTimeFormat

class AddTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_time)
    }

    fun moreTime(){

    }

    fun lessTime(){

    }

    fun doneRequestTime(){

    }

    companion object{
        @JvmStatic
        @BindingAdapter("app:requestTime")
        fun setRequestTime(view: TextView, time: Int) {
            var timeString: String
            val h: Int = time / 60
            val m: Int = time % 60
            timeString = if (h < 10) "0$h" else "$h"
            timeString += if (m < 10) ":0$m" else ":$m"
            view.text = timeString
        }

        @JvmStatic
        @BindingAdapter("app:hourFormat")
        fun setTime(view: TextView, min: Int) {
            view.text = min.toTimeFormat()
        }
    }
}
