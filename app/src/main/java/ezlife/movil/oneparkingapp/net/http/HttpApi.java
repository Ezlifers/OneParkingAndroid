package ezlife.movil.oneparkingapp.net.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.util.Preference;


/**
 * Created by Dario Chamorro on 15/05/2016.
 */
public abstract class HttpApi implements HttpAsyncTask.OnResponseListener {


    private String urlBase;
    protected Context context;
    protected SharedPreferences preferences;
    private String token;
    protected Gson gson;

    private ProgressDialog loading;

    public HttpApi(Context context, ProgressDialog loading) {
        this.context = context;
        this.loading = loading;
        preferences = context.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE);
        token = preferences.getString(Preference.TOKEN, "");
        urlBase = context.getString(R.string.url_base);
        gson = new Gson();
    }

    protected String makeUrl(int urlResource, Object... data) {
        String url = urlBase + context.getString(urlResource);
        return String.format(url, data);
    }

    protected String makeToken() {
        String tk = token + "_&&_" + System.currentTimeMillis();
        return Base64.encodeToString(tk.getBytes(), Base64.NO_WRAP);
    }

    protected void excuteTask(int request, int method, String url, String body, String token) {
        HttpAsyncTask task = makeTask(method, request);
        task.execute(url, body, token);
    }

    protected void excuteTask(int request, int method, String url, String token) {

        HttpAsyncTask task = makeTask(method, request);
        task.execute(url, token);
    }

    private HttpAsyncTask makeTask(int method, int request) {
        if (loading != null)
            loading.show();
        return new HttpAsyncTask(context, method, request, this);
    }

    protected void hideLoading() {
        if (loading != null)
            loading.dismiss();
    }

    protected boolean validateError(HttpResponse response) {
        int error = response.getError();
        if (error == HttpError.NO_ERROR) {
            int code = response.getStatusCode();
            if (code == 200) {
                return true;
            } else if (code == 401) {
                return returnError(response, HttpError.NO_AUTHORIZED, R.string.http_error_401);
            } else if (code == 404) {
                return returnError(response, HttpError.NO_FOUND, R.string.http_error_404);
            } else {
                return returnError(response, HttpError.FAIL, R.string.http_error_server);
            }

        } else if (error == HttpError.NO_INTERNET) {
            return returnError(response, HttpError.NO_INTERNET, R.string.http_error_internet);
        } else if (error == HttpError.TIMEOUT) {
            return returnError(response, HttpError.TIMEOUT, R.string.http_error_timeout);
        } else {
            return returnError(response, HttpError.FAIL, R.string.http_error_server);
        }
    }

    private boolean returnError(HttpResponse response, int error, int msg) {
        response.setError(error);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        return false;
    }

}
