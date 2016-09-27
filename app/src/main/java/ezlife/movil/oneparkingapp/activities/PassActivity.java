package ezlife.movil.oneparkingapp.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.databinding.PassBinding;
import ezlife.movil.oneparkingapp.net.api.login.LoginApi;
import ezlife.movil.oneparkingapp.util.CryptoJs;
import ezlife.movil.oneparkingapp.util.Qk;
import ezlife.movil.oneparkingapp.util.Settings;

public class PassActivity extends AppCompatActivity implements CryptoJs.OnEncryptAES, LoginApi.OnPassListener {

    PassBinding binding;
    LoginApi api;
    CryptoJs cryptoJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pass);

        binding.setHandler(this);
        api = new LoginApi(this, Qk.makeLoading(this));
        cryptoJs = new CryptoJs(this);
    }

    public void changePass(){
        String pass1 = Qk.getText(binding.pass1);
        String pass2 = Qk.getText(binding.pass2);

        if(pass1 == null || pass2 == null || !pass1.equals(pass2)){
            Qk.showToast(this,R.string.pass_error );
        }else{
            String pass = pass1+"_&&_"+ System.currentTimeMillis();
            cryptoJs.encryptAES(pass, Settings.SECRET, this);
        }
    }

    @Override
    public void onEncryptedAES(String aes) {
        api.changePassword(aes, this);
    }

    @Override
    public void onPass(boolean success, int error) {
        if(success){
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }else{
            Qk.showToast(this,R.string.pass_error_2 );
        }
    }

}
