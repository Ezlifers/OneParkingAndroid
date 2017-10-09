package ezlife.movil.oneparkingapp.util

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


fun EditText.text(): String = text.toString()

fun TextInputLayout.text(): String = editText?.text.toString()

fun ViewGroup.inflate(layoutId: Int): View = LayoutInflater.from(context).inflate(layoutId, this, false)

fun Int.toTimeFormat(): String {
    var h = this / 60
    val m = this % 60
    val min = if (m < 10) "0$m" else "$m"

    return if (h > 12) {
        h -= 12
        "$h:$min pm"
    } else "$h:$min am"
}

fun Int.toHourFormat(onlyMin: Boolean): String {
    return if (onlyMin) {
        if (this < 10) "0$this" else "$this"
    } else {
        val h = this / 60
        val m = this % 60
        "${if (this < 10) "0$h" else "$h"} h ${if (this < 10) "0$m" else "$m"} min"
    }
}

fun Fragment.setupArgs(vararg args: Pair<String, Any>) {
    val bundle = Bundle()
    args.forEach {
        val value = it.second
        when (value) {
            is Int -> bundle.putInt(it.first, value)
            is Boolean -> bundle.putBoolean(it.first, value)
            is Long -> bundle.putLong(it.first, value)
            is String -> bundle.putString(it.first, value)
        }
    }
    this.arguments = bundle
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




