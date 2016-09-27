package ezlife.movil.oneparkingapp.net.api.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.net.http.HttpApi;
import ezlife.movil.oneparkingapp.net.http.HttpAsyncTask;
import ezlife.movil.oneparkingapp.net.http.HttpError;
import ezlife.movil.oneparkingapp.net.http.HttpResponse;
import ezlife.movil.oneparkingapp.net.models.Response;
import ezlife.movil.oneparkingapp.net.models.User;
import ezlife.movil.oneparkingapp.util.Preference;
import ezlife.movil.oneparkingapp.util.Settings;

/**
 * Created by Dario Chamorro on 25/09/2016.
 */

public class LoginApi extends HttpApi {

    public static final int ERROR_EXIST = 10;

    //region Callbacks & Request
    private static final int SIGNIN = 0;
    private static final int LOGIN = 1;
    private static final int PASSWORD = 2;


    public interface OnSigninListener {
        void onSignin(boolean success, int error);
    }

    public interface OnLoginListener {
        void onLogin(boolean success, ClientRes.Client usuario, int error);
    }

    public interface OnPassListener {
        void onPass(boolean success, int error);
    }

    private OnSigninListener onSigninListener;
    private OnLoginListener onLoginListener;
    private OnPassListener onPassListener;
    //endregion

    User userReq;

    public LoginApi(Context context, ProgressDialog loading) {
        super(context, loading);
    }

    @Override
    public void onResponse(int requestCode, HttpResponse response) {
        hideLoading();
        if (validateError(response)) {
            switch (requestCode) {
                case SIGNIN:
                    processSignin(response);
                    break;
                case LOGIN:
                    processLogin(response);
                    break;
                case PASSWORD:
                    processChangePassword(response);
                    break;
            }
        }

    }

    //region Signin
    public void signin(User req, OnSigninListener onSigninListener) {
        this.onSigninListener = onSigninListener;
        this.userReq = req;
        String url = makeUrl(R.string.url_signin);
        excuteTask(SIGNIN, HttpAsyncTask.METHOD_POST, url, gson.toJson(req), null);
    }

    private void processSignin(HttpResponse response) {
        try {
            JSONObject json = new JSONObject(response.getMsg());
            boolean success = json.getBoolean("success");
            if (success) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preference.TOKEN, json.getString("token"));
                editor.putString(Preference.USER_ID, json.getString("id"));
                editor.putString(Preference.USER_CEL, userReq.getCelular());
                editor.putString(Preference.USER_NAME, userReq.getNombre());
                editor.apply();
                onSigninListener.onSignin(true, response.getError());
            } else {
                boolean exist = json.getBoolean("exist");
                if (exist)
                    onSigninListener.onSignin(false, ERROR_EXIST);
                else
                    onSigninListener.onSignin(false, response.getError());
            }
        } catch (JSONException e) {
            onSigninListener.onSignin(false, HttpError.UNKNOWN);
        }

    }
    //endregion

    //region Login
    public void login(String auth, OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
        String url = makeUrl(R.string.url_login);
        JsonObject json = new JsonObject();
        json.addProperty("auth", auth);
        json.addProperty("type", Settings.TYPE);
        excuteTask(LOGIN, HttpAsyncTask.METHOD_POST, url, json.toString(), null);
    }

    private void processLogin(HttpResponse response) {

        ClientRes res = gson.fromJson(response.getMsg(), ClientRes.class);
        if (res.isSuccess()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preference.TOKEN, res.getToken());
            ClientRes.Client user = res.getUsuario();
            editor.putString(Preference.USER_ID, user.get_id());
            editor.putString(Preference.USER_NAME, user.getNombre());
            editor.putString(Preference.USER_CEL, user.getCelular());
            if(user.getVehiculos().size()>0)
                editor.putBoolean(Preference.USER_LOGGED, true);
            editor.apply();
            onLoginListener.onLogin(true, user, response.getError());
        } else {
            onLoginListener.onLogin(false, null, response.getError());
        }

    }
    //endregion

    //region ChangePassword
    public void changePassword(String newPass, OnPassListener onPassListener) {
        this.onPassListener = onPassListener;
        String url = makeUrl(R.string.url_pass, preferences.getString(Preference.USER_ID, "0"));
        JsonObject json = new JsonObject();
        json.addProperty("password", newPass);
        excuteTask(PASSWORD, HttpAsyncTask.METHOD_POST, url, json.toString(), makeToken());
    }

    private void processChangePassword(HttpResponse response) {
        Response res = gson.fromJson(response.getMsg(), Response.class);
        onPassListener.onPass(res.isSuccess(), response.getError());
    }
    //endregion
}
