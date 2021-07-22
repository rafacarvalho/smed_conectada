package com.educacao.salvador.conexao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.datami.smi.*;
import com.datami.smi.internal.MessagingType;
import com.educacao.salvador.conexao.R;

public class MainApplication extends Application implements SdStateChangeListener {


    private Context context;
    public Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        callSDK();
        context = getApplicationContext();
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100*1000);
                }catch (Exception e){}

                if (currentActivity != null && currentActivity instanceof SplashScreenActivity) {
                    ((SplashScreenActivity)currentActivity).ExibirMensagem();
                }

            }
        }.start();
    }

    public void callSDK() {
        String mySdkKey = getString(R.string.SDK_API_KEY); //Use the SDK API access key given by Datami.
        int sdIconId = R.drawable.ic_launcher_foreground;
        SmiVpnSdk.initSponsoredData(mySdkKey, this, sdIconId, MessagingType.NONE);
    }

    private static final String TAG = MainActivity.class.getName();
    public static SdState sdState;
    @Override
    public void onChange(SmiResult currentSmiResult) {
        if(currentSmiResult != null) {
            sdState = currentSmiResult.getSdState();
        } else {
            sdState = null;
        }
        Log.d(TAG, "sponsored data state : "+sdState);
        if(sdState == SdState.SD_AVAILABLE || sdState == SdState.WIFI) {
            int cont = 0;
            while(currentActivity == null) {
                try {
                    Thread.sleep(1*1000);
                }catch (Exception e){}
                cont++;
                if(cont > 10) {
                    break;
                }
            }
            if (currentActivity != null && currentActivity instanceof SplashScreenActivity) {
                //((SplashScreenActivity)currentActivity).abrirMain();
                ((SplashScreenActivity)currentActivity).abrirHome();
            }
        } else if(sdState == SdState.SD_NOT_AVAILABLE) {
            if (currentActivity != null && currentActivity instanceof SplashScreenActivity) {
                ((SplashScreenActivity)currentActivity).ExibirMensagem();
            } else {
                if (currentActivity != null && currentActivity instanceof MainActivity) {
                    ((MainActivity) currentActivity).ExibirMensagem();
                }
            }
        }
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }
}




