package ezlife.movil.oneparkingapp.net.http;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by Dario Chamorro on 11/05/2016.
 */
public class HttpAsyncTask extends AsyncTask<String, Integer, HttpResponse> {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;
    public static final int METHOD_PUT = 2;
    public static final int METHOD_DELETE = 3;

    public interface OnResponseListener{
        void onResponse(int requestCode, HttpResponse response);
    }

    private HttpConnection con;

    private Context context;
    private int method;
    private int requestCode;
    private OnResponseListener onResponseListener;



    public HttpAsyncTask(Context context,int method,int requestCode, OnResponseListener onResponseListener){
        con = new HttpConnection();
        this.context = context;
        this.method = method;
        this.requestCode = requestCode;
        this.onResponseListener = onResponseListener;
    }

    @Override
    protected HttpResponse doInBackground(String... params) {

        HttpResponse rta = null;
        if(HttpConnection.isConnected(context)){
            try{
                switch (method){
                    case METHOD_GET:
                        rta = con.requestByGet(params[0], params[1] );
                        break;
                    case METHOD_POST:
                        rta = con.requestByPost(params[0], params[1], params[2]);
                        break;
                    case METHOD_PUT:
                        rta = con.requestByPut(params[0], params[1], params[2]);
                        break;
                    case METHOD_DELETE:
                        rta = con.requestByDelete(params[0], params[1], params[2]);
                        break;
                }
            }catch (SocketTimeoutException e){
                e.printStackTrace();
                rta =  new HttpResponse();
                rta.setError(HttpError.TIMEOUT);
            }catch(IOException e){
                e.printStackTrace();
                rta =  new HttpResponse();
                rta.setError(HttpError.FAIL);
            }
        }else{
            rta = new HttpResponse();
            rta.setError(HttpError.NO_INTERNET);
        }
        return rta;
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        onResponseListener.onResponse(requestCode,response);
    }

}
