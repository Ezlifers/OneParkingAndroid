package ezlife.movil.oneparkingapp.fragments


import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ezlife.movil.oneparkingapp.R

class ZoneFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_zone, container, false)
    }

    fun reserve(dis:Boolean){

    }

    fun report(){

    }

    fun goToInfo(){

    }

    companion object {

        @JvmStatic
        @BindingAdapter("app:bays", "app:baysSize")
        fun setAvailable(view: TextView, bays: Int, baysSize: Int) {
            view.text = view.context.getString(R.string.bay_available, bays, baysSize)
            val color = if (bays < baysSize) R.color.active else R.color.notAvailable
            view.setTextColor(ContextCompat.getColor(view.context, color))
        }
    }

}
