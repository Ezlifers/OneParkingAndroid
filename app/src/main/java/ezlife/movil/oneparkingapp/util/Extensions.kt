package ezlife.movil.oneparkingauxiliar.util

import android.support.design.widget.TextInputLayout
import android.widget.EditText

fun EditText.text(): String {
    return this.text.toString()
}

fun TextInputLayout.text(): String? {
    return this.editText?.text.toString()
}

fun Int.toTimeFormat(): String {
    var h = this / 60
    val m = this % 60
    val min = if (m < 10) "0$m" else "$m"

    val format: String =
            if (h > 12) {
                h -= 12
                "$h:$min pm"
            } else "$h:$min am"
    return format
}

fun Int.toHourFormat(onlyMin: Boolean): String {
    if (onlyMin) {
        return if (this < 10) "0$this" else "$this"
    } else {
        val h = this / 60
        val m = this % 60
        return "${if (this < 10) "0$h" else "$h"} h ${if (this < 10) "0$m" else "$m"} min"
    }
}