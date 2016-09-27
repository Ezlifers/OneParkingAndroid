package ezlife.movil.oneparkingapp.net.http;

/**
 * Created by Dario Chamorro on 14/05/2016.
 */
public class HttpResponse {

    private String msg;
    private int statusCode;
    private int error;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
