package ezlife.movil.oneparkingapp.util

import android.content.SharedPreferences
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*

fun EditText.text(): String = text.toString()

fun TextInputLayout.text(): String = editText?.text.toString()

infix fun CompositeDisposable.push(disposable: Disposable) {
    add(disposable)
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

fun SharedPreferences.edit(vararg preferences: Pair<String, Any>) {
    val editor = edit()
    preferences.forEach {
        val value = it.second
        when (value) {
            is Int -> editor.putInt(it.first, value)
            is Boolean -> editor.putBoolean(it.first, value)
            is Long -> editor.putLong(it.first, value)
            is String -> editor.putString(it.first, value)
        }
    }
    editor.apply()
}
