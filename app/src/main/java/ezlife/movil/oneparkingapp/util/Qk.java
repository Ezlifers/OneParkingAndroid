package ezlife.movil.oneparkingapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Toast;

import ezlife.movil.oneparkingapp.R;

/**
 * Created by Dario Chamorro on 26/09/2016.
 */

public class Qk {

    public static String getText(EditText editText){
        return editText.getText().toString();
    }

    public static String getText(TextInputLayout textInputLayout){
        return textInputLayout.getEditText().getText().toString();
    }

    public static void showToast(Context context, @StringRes  int msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog makeLoading(Context context){
        ProgressDialog loading =  new ProgressDialog(context);
        loading.setMessage(context.getString(R.string.loading));
        return loading;
    }

    public static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(Preference.NAME, Context.MODE_PRIVATE);
    }
}
