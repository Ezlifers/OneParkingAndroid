package ezlife.movil.oneparkingapp.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.util.Preference

fun AppCompatActivity.toast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.alert(title: Int, msg: Int, positiveCallback: () -> Unit) {
    val alert = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(R.string.alert_negative, null)
            .setPositiveButton(R.string.alert_positive) { _, _ ->
                positiveCallback()
            }.create()
    alert.show()
}

fun AppCompatActivity.alert(title: Int, msg: String, positiveCallback: () -> Unit) {
    val alert = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(R.string.alert_negative, null)
            .setPositiveButton(R.string.alert_positive) { _, _ ->
                positiveCallback()
            }.create()
    alert.show()
}

fun AppCompatActivity.preferences(): SharedPreferences {
    return this.getSharedPreferences(Preference.NAME, AppCompatActivity.MODE_PRIVATE)
}

fun AppCompatActivity.savePreference(vararg preferences: Pair<String, Any>) {
    val editor = preferences().edit()
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

fun AppCompatActivity.makeLoading(): ProgressDialog {
    val loading = ProgressDialog(this)
    loading.setMessage(getString(R.string.loading))
    return loading
}

fun AppCompatActivity.showDialog(fragment: DialogFragment) {
    val ft = supportFragmentManager.beginTransaction()
    val prev = supportFragmentManager.findFragmentByTag("dialog")
    if (prev != null) {
        ft.remove(prev)
    }
    ft.addToBackStack(null)
    fragment.show(ft, "dialog")
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) intent.putExtras(bundle)
    this.startActivity(intent)
}


inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivity(vararg extras: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    extras.forEach {
        val value = it.second
        when (value) {
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
        }
    }
    this.startActivity(intent)
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivityForResult(requestCode: Int, vararg extras: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    extras.forEach {
        val value = it.second
        when (value) {
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
        }
    }
    this.startActivityForResult(intent, requestCode)
}
