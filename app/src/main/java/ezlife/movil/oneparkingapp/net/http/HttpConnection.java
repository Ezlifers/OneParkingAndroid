package ezlife.movil.oneparkingapp.net.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dario Chamorro on 11/05/2016.
 */
public class HttpConnection {

    private final static int TIMEOUT_CONNECT=5000;
    private final static int TIMEOUT_READ=7000;



    public HttpResponse requestByGet(String url, String token) throws IOException {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();

        con.setConnectTimeout(TIMEOUT_CONNECT);
        con.setReadTimeout(TIMEOUT_READ);

        if(token!=null)
            con.setRequestProperty("Authorization", token);

        con.setRequestMethod("GET");
        con.setDoInput(true);
        con.connect();

        InputStream in = con.getInputStream();

        HttpResponse response = new HttpResponse();
        response.setMsg(streamToString(in));
        response.setStatusCode(con.getResponseCode());
        response.setError(HttpError.NO_ERROR);

        return response;
    }

    public HttpResponse requestByPost(String url, String json, String token) throws IOException {
        return request(url,json,"POST",token);
    }

    public HttpResponse requestByDelete(String url, String json, String token) throws IOException {
        return request(url,json,"DELETE",token);
    }

    public HttpResponse requestByPut(String url, String json, String token) throws IOException {
        return request(url,json,"PUT",token);
    }

    private HttpResponse request(String url, String json, String method, String token) throws IOException {
        URL u =  new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();

        if(token!=null)
            con.setRequestProperty("Authorization", token);

        con.setConnectTimeout(TIMEOUT_CONNECT);
        con.setReadTimeout(TIMEOUT_READ);

        con.setRequestMethod(method);
        con.setDoInput(true);


        if(json!=null) {
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

        }

        con.connect();

        if(json != null) {
            DataOutputStream writer = new DataOutputStream(con.getOutputStream());
            writer.write(json.getBytes());
            writer.flush();
            writer.close();
        }

        HttpResponse response = new HttpResponse();
        response.setMsg(streamToString(con.getInputStream()));
        response.setStatusCode(con.getResponseCode());
        response.setError(HttpError.NO_ERROR);

        return response;
    }

    private String streamToString(InputStream in) throws IOException {

        InputStreamReader reader = new InputStreamReader(in);
        ByteArrayOutputStream out =  new ByteArrayOutputStream();

        int ch;

        while((ch = reader.read())!= -1){
            out.write(ch);
        }

        String rta = new String(out.toByteArray());
        return rta;
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
