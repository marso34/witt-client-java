//package com.gwnu.witt.Data;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
//import com.gwnu.witt.R;
//
//public class AdmobUtil {
//
//    private static final String TAG = "AdmobUtil";
//    private static final String AD_ID = R.string.mt;
//
//    private static AdmobUtil instance;
//    private Context context;
//    private InterstitialAd mInterstitialAd;
//
//    private AdmobUtil(Context context) {
//        this.context = context.getApplicationContext();
//    }
//    public void AdmobInit(Context context){
//        this.context = context.getApplicationContext();
//        MobileAds.initialize(this.context, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                loadAds();
//            }
//        });
//    }
//    public static synchronized AdmobUtil getInstance(Context context) {
//        if (instance == null) {
//            instance = new AdmobUtil(context);
//        }
//        return instance;
//    }
//
//    public void showAds(Activity activity) {
//        if (mInterstitialAd != null) {
//            mInterstitialAd.show(activity);
//        } else {
//            Log.d(TAG, "The interstitial ad wasn't ready yet.");
//        }
//    }
//
//    private void loadAds() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        InterstitialAd.load(context, AD_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                mInterstitialAd = interstitialAd;
//                Log.i(TAG, "onAdLoaded");
//
//                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        Log.d(TAG, "The ad was dismissed.");
//                        loadAds();
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        Log.d(TAG, "The ad failed to show.");
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        mInterstitialAd = null;
//                        Log.d(TAG, "The ad was shown.");
//                    }
//                });
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                Log.i(TAG, loadAdError.getMessage());
//                mInterstitialAd = null;
//            }
//        });
//    }
//}
