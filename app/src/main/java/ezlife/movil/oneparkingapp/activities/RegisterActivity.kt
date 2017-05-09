package ezlife.movil.oneparkingapp.activities

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.databinding.RegisterBinding
import ezlife.movil.oneparkingapp.providers.RegisterReq
import ezlife.movil.oneparkingapp.providers.UserProvider
import ezlife.movil.oneparkingapp.util.text

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: RegisterBinding
    val provider: UserProvider by lazy { UserProvider(this, makeLoading()) }

    var name = ""
    var identity = ""
    var cel = ""
    var email = ""
    var pass = ""
    var dis = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.handler = this
        binding.secondScreen = false
    }

    fun next() {
        name = "${binding.name.text()}"
        identity = "${binding.identity.text()}"
        cel = "${binding.cel.text()}"
        dis = binding.dis.isChecked

        if (name == "" || identity == "" || cel == "") {
            toast(R.string.reg_form)
            return
        }

        binding.secondScreen = true

    }

    fun back() {
        binding.secondScreen = false
    }

    fun register() {
        email = "${binding.email.text()}"
        pass = "${binding.pass.text()}"
        val pass2 = "${binding.pass2.text()}"

        if (email == "" || pass == "" || pass2 == "") {
            toast(R.string.reg_form)
            return
        }
        if (pass != pass2) {
            toast(R.string.reg_pass_invalid)
            return
        }
        val user = RegisterReq("Cliente", name, identity, cel, email, email, pass, dis)
        provider.signin(user) { success, exists ->
            if (success) {
                toast(R.string.reg_success)
                setResult(Activity.RESULT_OK)
                finish()
            } else if (exists) {
                toast(R.string.reg_exist)
            }
        }
    }

}