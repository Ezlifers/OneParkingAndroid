package ezlife.movil.oneparkingapp.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.databinding.RegisterBinding;
import ezlife.movil.oneparkingapp.net.api.login.LoginApi;
import ezlife.movil.oneparkingapp.net.models.User;
import ezlife.movil.oneparkingapp.util.CryptoJs;
import ezlife.movil.oneparkingapp.util.Qk;
import ezlife.movil.oneparkingapp.util.Settings;

public class RegisterActivity extends AppCompatActivity implements CryptoJs.OnEncryptAES, LoginApi.OnSigninListener {

    RegisterBinding binding;

    String name, identity, cel, email, pass;
    boolean dis;

    CryptoJs cryptoJs;
    LoginApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        binding.setHandler(this);
        binding.setSecondScreen(false);

        cryptoJs =  new CryptoJs(this);
        api =  new LoginApi(this, Qk.makeLoading(this));
    }

    public void next(){
        name = Qk.getText(binding.name);
        identity = Qk.getText(binding.identity);
        cel = Qk.getText(binding.cel);
        dis =  binding.dis.isChecked();
        if(name.equals("") || identity.equals("") || cel.equals("")){
            Qk.showToast(this, R.string.reg_form);
        }else{
            binding.setSecondScreen(true);
        }

    }

    public void back(){
        binding.setSecondScreen(false);
    }

    public void register(){
        email = Qk.getText(binding.email);
        pass = Qk.getText(binding.pass);
        String pass2 = Qk.getText(binding.pass2);

        if(email.equals("") || pass.equals("") || pass2.equals("")){
            Qk.showToast(this, R.string.reg_form);
        }else{
            if(pass.equals(pass2)){
                cryptoJs.encryptAES(pass, Settings.SECRET, this);
            }else{
                Qk.showToast(this, R.string.reg_pass_invalid);
            }
        }

    }

    @Override
    public void onEncryptedAES(String aes) {
        User req = new User();
        req.setCedula(identity);
        req.setCelular(cel);
        req.setEmail(email);
        req.setNombre(name);
        req.setTipo(Settings.TYPE);
        req.setUsuario(email);
        req.setPassword(aes);
        api.signin(req, this);
    }

    @Override
    public void onSignin(boolean success, int error) {
        if(success){
            Qk.showToast(this, R.string.reg_success);
            setResult(RESULT_OK);
            finish();
        }else{
            if(error == LoginApi.ERROR_EXIST){
                Qk.showToast(this, R.string.reg_exist);
            }
        }

    }
}
