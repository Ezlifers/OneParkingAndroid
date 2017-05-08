package ezlife.movil.oneparkingapp.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.LoginBinding
import ezlife.movil.oneparkingapp.providers.UserProvider
import ezlife.movil.oneparkingauxiliar.util.text

class LoginActivity : AppCompatActivity() {

    lateinit var binding: LoginBinding
    val provider: UserProvider by lazy { UserProvider(this, makeLoading()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.handler = this
        checkPermission()
    }

    fun login() {
        val usr = "${binding.usr.text()}"
        val pass = "${binding.pass.text()}"
        if (usr == "" || pass == "") {
            toast(R.string.login_form)
            return
        }
        provider.login(usr, pass) { user ->

            if(user.validado){
                if(user.vehiculos.isNotEmpty())
                    startActivity<MapActivity>()
                else
                    startActivity<AddCarActivity>(AddCarActivity.EXTRA_FIRST_TIME to true)

            }else
                startActivity<PassActivity>()
            finish()
        }
    }

    fun clear() {
        binding.city = null
    }

    fun goToRegister(){
        startActivityForResult<RegisterActivity>(101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                startActivity<AddCarActivity>(AddCarActivity.EXTRA_FIRST_TIME to true)
                finish()
            }
        }
    }

    fun checkPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA
                    , Manifest.permission.SEND_SMS
                    , Manifest.permission.CALL_PHONE), 101)

        }
    }

    companion object{
        @JvmStatic
        @BindingAdapter("app:roboto")
        fun loadRoboto(view: TextView, name:String){
            val assetManager: AssetManager = view.context.assets
            val typeface: Typeface = Typeface.createFromAsset(assetManager,"fonts/$name.ttf")
            view.typeface = typeface
        }
    }
}