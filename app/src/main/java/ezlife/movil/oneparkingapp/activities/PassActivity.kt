package ezlife.movil.oneparkingapp.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.PassBinding
import ezlife.movil.oneparkingapp.providers.UserProvider
import ezlife.movil.oneparkingauxiliar.util.text

class PassActivity:AppCompatActivity(){

    lateinit var binding:PassBinding
    val provider:UserProvider by lazy { UserProvider(this, makeLoading())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pass)
        binding.handler = this
    }

    fun updatePass(){
        val pass1 = "${binding.pass1.text()}"
        val pass2 = "${binding.pass2.text()}"

        if(pass1 != pass2){
            toast(R.string.pass_error)
            return
        }
        provider.updatePassword(pass1){success ->
            if(success){
                startActivity<MapActivity>()
            }else{
                toast(R.string.pass_error_2)
            }
        }
    }
}