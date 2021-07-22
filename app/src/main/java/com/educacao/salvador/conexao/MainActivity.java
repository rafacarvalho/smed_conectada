package com.educacao.salvador.conexao;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.datami.smi.SdState;
import com.datami.smi.SmiVpnSdk;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    WebView myWebView;
    AlertDialog.Builder builder;

    private String webURL;
    private String callingClass;

    public boolean lastActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            ((MainApplication)getApplicationContext()).setCurrentActivity(this);

            getSupportActionBar().setTitle("Voltar ao início");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ActionBar actionBar = getActionBar();

            myWebView = new WebView(this);
            myWebView.setWebViewClient(new MyWebViewClient());
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            //improve webView performance
            myWebView.setWebChromeClient(new MyChrome());
            myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            myWebView.getSettings().setAppCacheEnabled(true);
            myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            webSettings.setSaveFormData(true);

            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            Locale.setDefault(new Locale("pt", "BR"));
            setContentView(myWebView);

            Intent currentIntent = getIntent();
            webURL = currentIntent.getStringExtra(HomeActivity.EXTRA_URL);
            //webURL = "https://app.arvore.com.br/";

            if (savedInstanceState == null) {
                myWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        myWebView.loadUrl(webURL);
                    }
                });
            }

        }
        catch (Exception e){
            SmiVpnSdk.stopSponsoredData();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Log.d("SMED",  String.format("value = %d", KeyEvent.ACTION_DOWN));
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                    //String url = "https://www.escolamais.com/salvador";
                    String url = "http://educacao.salvador.ba.gov.br/";
                    String url2 = "https://app.arvore.com.br/";
                    String webUrl = myWebView.getUrl();

                    if (!url.equals(webUrl) && !url2.equals(webUrl)) {
                        if (myWebView.canGoBack()) {
                            myWebView.goBack();
                        }
                    }else{
                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Canvas")
                                .setMessage("Deseja retornar à tela inicial da aplicação?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        backPress();
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }

                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backPress(){ super.onBackPressed(); }

    public void ExibirMensagem() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(builder == null) {
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("SMED Conectada");
                        builder.setMessage("A conexão com dados os dados patrocinados foi perdida. Deseja tentar novamente?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder = null;
                                try{
                                    //if(!SmiVpnSdk.isVpnPermissionAccepted()) {
                                    SmiVpnSdk.startSponsoredData();
                                    //}
                                }catch(Exception e){

                                }
                            }
                        });

                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder = null;
                                finishAndRemoveTask();
                                System.exit(0);
                            }
                        });

                        builder.show();
                    }
                } catch (WindowManager.BadTokenException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        myWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        myWebView.restoreState(savedInstanceState);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl(
                    "javascript:(function f() {" +
                            "var email = document.getElementsByName('identifier');" +
                            "email[0].oninput = function(value) {" +
                            "if(!/^\\w+([\\.-]?\\w+)*(@)?((e(d(u(c(a(d(o(r)?)?)?)?)?)?)?)?|(a(l(u(n(o)?)?)?)?))?(\\.)?(e(d(u(\\.(e(s(\\.(g(o(v(\\.(b(r)?)?)?)?)?)?)?)?)?)?)?)?)?$/.test(email[0].value)){" +
                            "email[0].value = '';" +
                            "email.parentNode.parentNode.parentNode.insertAdjacentHTML('afterend', 'Apenas domínio edu.es.gov.br!');" +
                            "return false;" +
                            "}" +
                            "}" +
                            "})()"
            );
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            try {
                if(url.startsWith("javascript"))
                    return false;

                if (url.startsWith("http") || url.startsWith("https"))
                {
                    if(MainApplication.sdState == SdState.SD_AVAILABLE)
                    {
                        URL urlEntrada = null;
                        urlEntrada = new URL(url);
                        List<String> urlsPermitidas = new ArrayList<String>(25);
                        urlsPermitidas.add("escolamais.instructure.com");
                        urlsPermitidas.add("escolamais.com");
                        urlsPermitidas.add("docs.google.com");
                        urlsPermitidas.add("arvoredelivros.com.br");
                        urlsPermitidas.add("arvore.com.br");
                        urlsPermitidas.add("www2.arvoredelivros.com.br");
                        urlsPermitidas.add("app.arvore.com.br");
                        urlsPermitidas.add("livros.arvore.com.br");
                        urlsPermitidas.add("leitor.arvore.com.br");
                        urlsPermitidas.add("musicanaescola.educacao.salvador.ba.gov.br");
                        urlsPermitidas.add("educacao.salvador.ba.gov.br");
                        urlsPermitidas.add("meet.google.com");
                        urlsPermitidas.add("accounts.google.com");
                        urlsPermitidas.add("classroom.google.com");
                        urlsPermitidas.add("google.com");
                        urlsPermitidas.add("gstatic.com");
                        urlsPermitidas.add("googleusercontent.com");
                        urlsPermitidas.add("accounts.youtube.com");
                        urlsPermitidas.add("meet.app.goo.gl");

                        //TODO: fazer um filtro inteligente de URLs
                        for (int i = 0; i <= urlsPermitidas.size() -1; i++)
                        {
                            if(urlEntrada.getAuthority().contains(urlsPermitidas.get(i))) {
                                return false;
                            }
                        }
                        Log.d("ControleAcesso", "Acesso patrocinado negado a " + url);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(getApplicationContext(), "Acesso patrocinado negado a " + url, duration);
                        toast.show();
                        return true;
                    }
                    else
                        return false;
                }

                if (url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        PackageManager packageManager = webView.getContext().getPackageManager();
                        if (intent != null) {
                            webView.stopLoading();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                webView.getContext().startActivity(intent);
                            } else {
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                                        Uri.parse("market://details?id=" + intent.getPackage()));
                                if (marketIntent.resolveActivity(packageManager) != null) {
                                    getApplicationContext().startActivity(marketIntent);
                                    return true;
                                }
                            }
                            return true;
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
            return true;
        }

        /*private final String TAG = MainActivity.class.getName();
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if(MainApplication.sdState != SdState.SD_AVAILABLE && MainApplication.sdState != SdState.WIFI) {
                callWebView();
            }
            Log.i(TAG, "GOT Page error : code : " + errorCode + " Desc : " + description);

            //TODO We can show customized HTML page when page not found/ or server not found error.
            super.onReceivedError(view, errorCode, description, failingUrl);
        }*/

    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }


}
