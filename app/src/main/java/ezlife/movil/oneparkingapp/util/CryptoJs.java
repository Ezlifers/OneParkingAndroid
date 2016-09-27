package ezlife.movil.oneparkingapp.util;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by Dario Chamorro on 25/09/2016.
 */

public class CryptoJs {

    public interface OnEncryptAES{
        void onEncryptedAES(String aes);
    }

    OnEncryptAES onEncryptAES;
    WebView webView;

    public CryptoJs(Context context){
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/crypto/index.html");
        webView.addJavascriptInterface(this, "andClient");
    }

    public void encryptAES(String txt, String secret, OnEncryptAES onEncryptAES){
        this.onEncryptAES = onEncryptAES;
        webView.loadUrl("javascript:encyptAES('" + txt + "', '" + secret+ "')");
    }

    @JavascriptInterface
    public void onEncryptedAES(String aes){
        onEncryptAES.onEncryptedAES(aes);
    }





}
