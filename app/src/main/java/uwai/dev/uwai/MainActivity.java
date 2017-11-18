package uwai.dev.uwai;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webview = (WebView)findViewById(R.id.WebView);

        webview.clearCache(true);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                goToNoNetworkError(view);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("138.197.129.141")) {
                    if (isNetworkAvailable()) {
                        view.loadUrl(url);
                    }
                    else {
                        goToNoNetworkError(view);
                    }
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
                return true;
            }
        });
        if (isNetworkAvailable()) {
            webview.loadUrl("http://138.197.129.141/uwai/");
        }
        else {
            goToNoNetworkError(webview);
        }

    }

    private void goToNoNetworkError(WebView view) {
        view.loadUrl("file:///android_asset/NoNetworkError.html");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
