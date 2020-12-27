package orderFlex.paymentCollection.Activityes.LandingPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Activityes.Offers.Offers;
import orderFlex.paymentCollection.Activityes.login.UserLogin;
import orderFlex.paymentCollection.Model.APICallings.PullAppSetup;
import orderFlex.paymentCollection.Model.AppSetup.AppSetupRequestBody;
import orderFlex.paymentCollection.Model.AppSetup.AppSetupResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.OnSwipeTouchListener;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class LandingActivity extends AppCompatActivity implements PullAppSetup.AppSetupListener{
    private Helper helper;
    private SharedPrefManager prefManager;
    private String TAG="LandingActivity";
    private View containerView;
    private ConstraintLayout mainView;
    private ImageView splashImage;
    public static final String USER_AGENT_FAKE = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    private String user_name="", user_pass="";
    public ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        getSupportActionBar().hide();
        containerView=findViewById(R.id.landingActivity);
        mainView=findViewById(R.id.landingActivity);

        helper=new Helper(this);
        prefManager=new SharedPrefManager(this);
        //activate and deactivate debugging mode
        prefManager.setDebugMode(false);

        Log.i(TAG,helper.getAnrdoidID());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkApplication();
            }
        }, 3000);

        mainView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                Log.i(TAG,"TOP");
                checkApplication();
            }
            public void onSwipeRight() {
                Log.i(TAG,"RIGHT");
                checkApplication();
            }
            public void onSwipeLeft() {
                Log.i(TAG,"LEFT");
                checkApplication();
            }
            public void onSwipeBottom() {
                Log.i(TAG,"BOTTOM");
                checkApplication();
            }

        });
    }

    private void checkApplication(){
        if (prefManager.isLoggedIn()){
            AppSetupRequestBody.ScreenDimensions screenDimensions=myScreen();
            AppSetupRequestBody requestBody=new AppSetupRequestBody("1v1.2",helper.getAnrdoidID(),"",
                    "",helper.getDateTimeInEnglish(),screenDimensions,prefManager.getClientId());
            if (helper.isInternetAvailable()){
                new PullAppSetup(this).pullSetup("app.admin@payflex","@ppd0t@dm1n",requestBody);
            }else {
                helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
            }
        }else {
            startActivity(new Intent(LandingActivity.this, UserLogin.class));
        }

    }

    public AppSetupRequestBody.ScreenDimensions myScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        AppSetupRequestBody.ScreenDimensions screenDimensions=new AppSetupRequestBody.ScreenDimensions(widthPixels,heightPixels,densityDpi,xdpi,ydpi);
        Log.i(TAG, "widthPixels  = " + widthPixels);
        Log.i(TAG, "heightPixels = " + heightPixels);
        Log.i(TAG, "densityDpi   = " + densityDpi);
        Log.i(TAG, "xdpi         = " + xdpi);
        Log.i(TAG, "ydpi         = " + ydpi);
        return screenDimensions;
    }

    @Override
    public void onAppSetupResponse(AppSetupResponse response, int code) {
        if (response!=null && code==202){
            if (response.getData().getIsUpdatedApp()){
                if (response.getData().getIsSystemUnderMaintenance()){
                    helper.showSnakBar(containerView,"Server under maintenance!");
                }else {
                    if (response.getData().getIsMessageForUser()){
                        showPopUp("Important Massage!",response.getData().getCustomWebViewURL(),
                                "To see the message press the GO button","GO","1");
                    }else {
                        if (!prefManager.isLoggedIn()){
                            Intent intent =new Intent(LandingActivity.this, UserLogin.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if (response.getData().getOffer()){
                                Intent intent3 =new Intent(LandingActivity.this, Offers.class);
                                startActivity(intent3);
                                finish();
                            }else {
                                Intent intent2 =new Intent(LandingActivity.this, OrderListActivity.class);
                                startActivity(intent2);
                                finish();
                            }
                        }
                    }
                }
                if (response.getData().getIsMessageForUser()){
                   Log.i(TAG,"user have message");
                }
            }else {
                showPopUp("Update!",response.getData().getUpdatedAppLink(),
                        "Sorry! You are using the older version of application. Please press the UPDATE button to get the updated application","UPDATE","2");
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,String.valueOf(code)+": Loading error, Server not responding");
            }
        }
    }


    private void showPopUp(String title, final String url, String msg, String done, final String msgType){
        progressBar = ProgressDialog.show(this, "Offer", "Loading...");
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.offer_popup_window, null);
        //TextView heading=popupView.findViewById(R.id.popup_heading);
        ImageView popUpCancel=popupView.findViewById(R.id.popUpCancel);

        //set webview
        WebView popupWebView=popupView.findViewById(R.id.popupWebView);
        webViewOperation(popupWebView,url);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (!prefManager.isLoggedIn()){
                    Intent intent =new Intent(LandingActivity.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent2 =new Intent(LandingActivity.this, OrderListActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

    void webViewOperation(WebView popupWebView, String url){
        WebSettings setting= popupWebView.getSettings();
        setting.setUserAgentString(USER_AGENT_FAKE);
        popupWebView.setFocusable(true);
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        popupWebView.loadUrl(url);
        CookieManager.getInstance().setAcceptCookie(true);
        popupWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(user_name,user_pass);
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith(".mp4")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    startActivity(intent);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss(); }
            }
        });
    }
}







//
//    private void showMessageDialog(String title, final String url, String msg, String done, final String msgType){
//        final AlertDialog alertDialog;
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.load_message_view,null);
//
//        CardView skip_key=customRoot.findViewById(R.id.skip_key);
//        CardView done_key=customRoot.findViewById(R.id.done_key);
//        TextView btnDone=customRoot.findViewById(R.id.btnDone);
//        btnDone.setText(done);
//
//        TextView msgView=customRoot.findViewById(R.id.messageText);
//        msgView.setText(msg);
//        builder.setView(customRoot);
//        builder.setTitle(title);
//        builder.setCancelable(true);
//        alertDialog= builder.create();
//        alertDialog.show();
//        skip_key.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (msgType.equals("1")){
//                    if (!prefManager.isLoggedIn()){
//                        Intent intent =new Intent(LandingActivity.this, UserLogin.class);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        Intent intent2 =new Intent(LandingActivity.this, OrderListActivity.class);
//                        startActivity(intent2);
//                        finish();
//                    }
//                }else if (msgType.equals("2")){
//                    checkApplication();
//                }
//                alertDialog.dismiss();
//            }
//        });
//        done_key.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (msgType.equals("1")){
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(browserIntent);
//                }else if (msgType.equals("2")){
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(browserIntent);
//                }
//                alertDialog.dismiss();
//            }
//        });
//    }