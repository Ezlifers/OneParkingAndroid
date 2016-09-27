package ezlife.movil.oneparkingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.databinding.LoginBinding;
import ezlife.movil.oneparkingapp.net.api.login.ClientRes;
import ezlife.movil.oneparkingapp.net.api.login.LoginApi;
import ezlife.movil.oneparkingapp.net.http.HttpError;
import ezlife.movil.oneparkingapp.util.CryptoJs;
import ezlife.movil.oneparkingapp.util.Preference;
import ezlife.movil.oneparkingapp.util.Qk;
import ezlife.movil.oneparkingapp.util.Settings;

public class LoginActivity extends AppCompatActivity implements CryptoJs.OnEncryptAES, LoginApi.OnLoginListener {

    private static final int REQUEST_REG = 101;

    LoginBinding binding;
    LoginApi api;

    SharedPreferences preferences;
    String city;

    CryptoJs cryptoJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setHandler(this);

        preferences = getSharedPreferences(Preference.NAME, MODE_PRIVATE);
        city = preferences.getString(Preference.CITY, null);
        binding.setCity(city);

        cryptoJs = new CryptoJs(this);

        api = new LoginApi(this, Qk.makeLoading(this));
    }

    public void clear() {
        city = null;
        binding.setCity(null);
    }

    public void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_REG);
    }

    public void login() {
        String usr = Qk.getText(binding.usr);
        String pass = Qk.getText(binding.pass);
        if (usr.equals("") || pass.equals("")) {
            Qk.showToast(this, R.string.login_form);
        } else {
            if (city == null) {
                city = binding.city.getSelectedItem().toString();
            }

            String auth = usr + "_&&_" + pass + "_&&_" + System.currentTimeMillis();
            cryptoJs.encryptAES(auth, Settings.SECRET, this);
        }
    }

    @Override
    public void onEncryptedAES(String aes) {
        api.login(aes, this);
    }

    @Override
    public void onLogin(boolean success, ClientRes.Client usuario, int error) {
        if (success) {
            Intent intent;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preference.CITY, city);
            editor.apply();
            if (usuario.isValidado()) {
                if(usuario.getVehiculos().size() > 0)
                    intent = new Intent(this, MapActivity.class);
                else{
                    intent = new Intent(this, AddCarActivity.class);
                    intent.putExtra(AddCarActivity.EXTRA_FIRST_TIME, true);
                }
            } else {
                intent = new Intent(this, PassActivity.class);
            }
            startActivity(intent);
            finish();

        } else if (!success && error == HttpError.NO_ERROR) {
            Qk.showToast(this, R.string.login_fail);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_REG){
            if(resultCode == RESULT_OK){
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra(AddCarActivity.EXTRA_FIRST_TIME, true);
                startActivity(intent);
                finish();
            }
        }
    }
}

