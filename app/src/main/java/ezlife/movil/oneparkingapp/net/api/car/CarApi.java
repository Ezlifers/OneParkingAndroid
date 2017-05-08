package ezlife.movil.oneparkingapp.net.api.car;

import android.app.ProgressDialog;
import android.content.Context;



import java.lang.reflect.Type;
import java.util.List;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.net.http.HttpApi;
import ezlife.movil.oneparkingapp.net.http.HttpAsyncTask;
import ezlife.movil.oneparkingapp.net.http.HttpResponse;
import ezlife.movil.oneparkingapp.net.models.Car;
import ezlife.movil.oneparkingapp.net.models.Response;
import ezlife.movil.oneparkingapp.util.Preference;

/**
 * Created by Dario Chamorro on 26/09/2016.
 */

public class CarApi extends HttpApi {

    //region Callbacks & Request
    private static final int INSERT = 0;
    private static final int LIST = 1;
    private static final int DELETE = 2;

    public interface OnInsertCarListener {
        void onInsertedCar(boolean success, int error);

        void onOutRangeCar();
    }

    public interface OnListCarListener {
        void onListCar(List<Car> cars, int error);
    }

    public interface OnDeleteCarListener {
        void onDeletedCar(boolean success, int error);
    }

    private OnInsertCarListener onInsertCar;
    private OnListCarListener onListCar;
    private OnDeleteCarListener onDeleteCar;
    //endregion

    public CarApi(Context context, ProgressDialog loading) {
        super(context, loading);
    }

    @Override
    public void onResponse(int requestCode, HttpResponse response) {
        if (validateError(response)) {
            switch (requestCode) {
                case INSERT:
                    processInsert(response);
                    break;
                case LIST:
                    processList(response);
                    break;
                case DELETE:
                    processDelete(response);
                    break;
            }
            hideLoading();
        }
    }

    //region Insert
    public void insert(Car car, OnInsertCarListener onInsertCar) {
        this.onInsertCar = onInsertCar;
        String url = makeUrl(R.string.url_car_insert);
        excuteTask(INSERT, HttpAsyncTask.METHOD_POST, url, gson.toJson(car), makeToken());
    }

    private void processInsert(HttpResponse response) {

        CarRes res = gson.fromJson(response.getMsg(), CarRes.class);
        if (res.isOutRange()) {
            onInsertCar.onOutRangeCar();
        } else {
            onInsertCar.onInsertedCar(res.isSuccess(), response.getError());
        }

    }
    //endregion

    //region List
    public void list(OnListCarListener onListCar) {
        this.onListCar = onListCar;
        String url = makeUrl(R.string.url_car_list, preferences.getString(Preference.USER_ID, ""));
        excuteTask(LIST, HttpAsyncTask.METHOD_GET, url, makeToken());

    }

    private void processList(HttpResponse response) {
        //Type type =  new TypeToken<List<Car>>(){}.getType();
        //List<Car> data =  gson.fromJson(response.getMsg(), type);
        //this.onListCar.onListCar(data, response.getError());
    }
    //endregion

    //region Delete
    public void delete(String carPlate, OnDeleteCarListener onDeleteCar) {
        this.onDeleteCar = onDeleteCar;
        String url = makeUrl(R.string.url_car_delete, carPlate);
        excuteTask(INSERT, HttpAsyncTask.METHOD_DELETE, url, makeToken());
    }

    private void processDelete(HttpResponse response) {
        Response res = gson.fromJson(response.getMsg(), Response.class);
        onDeleteCar.onDeletedCar(res.isSuccess(), response.getError());
    }
    //endregion

}
