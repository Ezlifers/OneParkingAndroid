package ezlife.movil.oneparkingapp.providers

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.activities.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException


data class SimpleResponse(val success: Boolean)

object RetrofitHelper {

    lateinit var retrofit: Retrofit

    fun init(context: Context) {

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

        retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.url_base))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

}

class ProviderCallback<T>(val activity: AppCompatActivity
                          , val loading: ProgressDialog? = null
                          , val res404: Int = R.string.http_404
                          , val callback: (obj: T) -> Unit) : Callback<T> {

    override fun onResponse(call: Call<T>?, response: Response<T>) {
        loading?.dismiss()
        if (response.isSuccessful) {
           // callback(response.body())
        } else {
            when (response.code()) {
                404 -> activity.toast(res404)
                401 -> activity.toast(R.string.http_401)
                else -> activity.toast(R.string.http_server)
            }
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable) {
        when (t.cause) {
            is SocketTimeoutException -> activity.toast(R.string.http_timeout)
            is ConnectException -> activity.toast(R.string.http_connection)
        }
        loading?.dismiss()
    }
}