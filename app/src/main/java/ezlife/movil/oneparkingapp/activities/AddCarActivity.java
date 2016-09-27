package ezlife.movil.oneparkingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.databinding.AddCarBinding;
import ezlife.movil.oneparkingapp.net.api.car.CarApi;
import ezlife.movil.oneparkingapp.net.models.Car;
import ezlife.movil.oneparkingapp.util.Lt;
import ezlife.movil.oneparkingapp.util.Preference;
import ezlife.movil.oneparkingapp.util.Qk;

public class AddCarActivity extends AppCompatActivity implements CarApi.OnInsertCarListener {

    public static final String EXTRA_FIRST_TIME = "firstTime";

    AddCarBinding binding;
    boolean firstTime;
    CarApi api;
    Car newCar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car);
        binding.setHandler(this);

        preferences = Qk.getPreference(this);
        api = new CarApi(this, Qk.makeLoading(this));

        firstTime = false;
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            firstTime = extras.getBoolean(EXTRA_FIRST_TIME);

        binding.setFistTime(firstTime);

        setUpActionBar();
    }

    private void setUpActionBar() {
        if(!firstTime){
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void add(){
        String plate = Qk.getText(binding.plate);
        String brand = Qk.getText(binding.brand);
        String nickname = Qk.getText(binding.nickname);

        newCar = new Car();
        newCar.setApodo(nickname);
        newCar.setMarca(brand);
        newCar.setPlaca(plate);
        api.insert(newCar, this);
    }

    @Override
    public void onInsertedCar(boolean success, int error) {
        if(success) {
            Lt.getCars().add(newCar);
            Qk.showToast(this, R.string.car_insert);
            if (firstTime) {
                SharedPreferences.Editor editor =  preferences.edit();
                editor.putBoolean(Preference.USER_LOGGED, true);
                editor.apply();
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            }
            finish();
        }else{
            Qk.showToast(this, R.string.car_insert_error);
        }
    }

    @Override
    public void onOutRangeCar() {
        Qk.showToast(this, R.string.car_insert_error_2);
        finish();
    }
}
