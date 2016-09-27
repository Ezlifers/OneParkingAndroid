package ezlife.movil.oneparkingapp.net.api.report;

import android.app.ProgressDialog;
import android.content.Context;

import ezlife.movil.oneparkingapp.R;
import ezlife.movil.oneparkingapp.net.http.HttpApi;
import ezlife.movil.oneparkingapp.net.http.HttpAsyncTask;
import ezlife.movil.oneparkingapp.net.http.HttpResponse;
import ezlife.movil.oneparkingapp.net.models.Response;


/**
 * Created by Dario Chamorro on 30/05/2016.
 */
public class ReportApi extends HttpApi {

    private static final int NOTIFY = 0;

    //region Callbacks
    public interface OnReportListener {
        void onSendedReport(boolean success, int error);
    }

    private OnReportListener onReportListener;
    //endregion

    public ReportApi(Context context, ProgressDialog loading) {
        super(context, loading);
    }

    @Override
    public void onResponse(int requestCode, HttpResponse response) {
        if (validateError(response)) {
            switch (requestCode) {
                case NOTIFY:
                    processReport(response);
                    break;
            }
        }
    }

    //region Notificar Incidencia
    public void notify(ReportReq reportReq, OnReportListener onReportListener) {
        this.onReportListener = onReportListener;
        String url = makeUrl(R.string.url_report);
        excuteTask(NOTIFY, HttpAsyncTask.METHOD_POST, url, gson.toJson(reportReq), makeToken());
    }

    private void processReport(HttpResponse response) {
        Response res = gson.fromJson(response.getMsg(), Response.class);
        onReportListener.onSendedReport(res.isSuccess(), response.getError());
    }
    //endregion


}
