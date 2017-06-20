package ezlife.movil.oneparkingapp.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ezlife.movil.oneparkingapp.R

fun Fragment.setupArgs(vararg args:Pair<String, Any>){
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

fun Fragment.toast(resId: Int) {
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(vararg extras: Pair<String, Any>) {
    val intent = Intent(activity, T::class.java)
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

fun Fragment.makeLoading(): ProgressDialog {
    val loading = ProgressDialog(activity)
    loading.setMessage(getString(R.string.loading))
    return loading
}