package orderFlex.paymentCollection.Activityes.LandingPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
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
        prefManager.setDebugMode(true);

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
        AppSetupRequestBody.ScreenDimensions screenDimensions=myScreen();
        AppSetupRequestBody requestBody=new AppSetupRequestBody("1v1",helper.getAnrdoidID(),"",
                "",helper.getDateTimeInEnglish(),screenDimensions);
        if (helper.isInternetAvailable()){
            new PullAppSetup(this).pullSetup("app.admin@payflex","@ppd0t@dm1n",requestBody);
        }else {
            helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
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
                        showMessageDialog("Important Massage!",response.getData().getCustomWebViewURL(),
                                "To see the message press the GO button","GO","1");
                    }else {
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
                }
                if (response.getData().getIsMessageForUser()){
                   Log.i(TAG,"user have message");
                }
            }else {
                showMessageDialog("Update!",response.getData().getUpdatedAppLink(),
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

    private void showMessageDialog(String title, final String url, String msg, String done, final String msgType){
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.load_message_view,null);

        CardView skip_key=customRoot.findViewById(R.id.skip_key);
        CardView done_key=customRoot.findViewById(R.id.done_key);
        TextView btnDone=customRoot.findViewById(R.id.btnDone);
        btnDone.setText(done);

        TextView msgView=customRoot.findViewById(R.id.messageText);
        msgView.setText(msg);
        builder.setView(customRoot);
        builder.setTitle(title);
        builder.setCancelable(true);
        alertDialog= builder.create();
        alertDialog.show();
        skip_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgType.equals("1")){
                    if (!prefManager.isLoggedIn()){
                        Intent intent =new Intent(LandingActivity.this, UserLogin.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent2 =new Intent(LandingActivity.this, OrderListActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                }else if (msgType.equals("2")){
                    checkApplication();
                }
                alertDialog.dismiss();
            }
        });
        done_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgType.equals("1")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }else if (msgType.equals("2")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
                alertDialog.dismiss();
            }
        });
    }
}