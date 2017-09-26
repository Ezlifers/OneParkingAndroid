package ezlife.movil.oneparkingapp.util

import android.databinding.BindingAdapter
import android.widget.TextView
import java.text.NumberFormat

object AttrsUtil {

    private val numberFormat: NumberFormat by lazy {
        val format = NumberFormat.getInstance()
        format.maximumFractionDigits = 0
        format
    }

    @JvmStatic
    @BindingAdapter("app:moneyFormat")
    fun setMoneyFormat(textView:TextView, money:Long){
        textView.text = numberFormat.format(money)
    }


}