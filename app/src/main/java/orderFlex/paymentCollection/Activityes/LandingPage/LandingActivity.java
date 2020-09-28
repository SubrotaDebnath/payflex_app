package orderFlex.paymentCollection.Activityes.LandingPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Activityes.login.UserLogin;
import orderFlex.paymentCollection.Model.APICallings.PullAppSetup;
import orderFlex.paymentCollection.Model.AppSetup.AppSetupRequestBody;
import orderFlex.paymentCollection.Model.AppSetup.AppSetupResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class LandingActivity extends AppCompatActivity implements PullAppSetup.AppSetupListener{
    private Helper helper;
    private SharedPrefManager prefManager;
    private String TAG="LandingActivity";
    private View containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        getSupportActionBar().hide();
        containerView=findViewById(R.id.landingActivity);

        helper=new Helper(this);
        prefManager=new SharedPrefManager(this);
        Log.i(TAG,helper.getAnrdoidID());

        checkApplication();

    }

    private void checkApplication(){
        AppSetupRequestBody.ScreenDimensions screenDimensions=myScreen();
        AppSetupRequestBody requestBody=new AppSetupRequestBody("1v1",helper.getAnrdoidID(),"",
                "",helper.getDateTimeInEnglish(),screenDimensions);
        new PullAppSetup(this).pullSetup("app.admin@payflex","@ppd0t@dm1n",requestBody);
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
                    if (prefManager.isLoggedIn()){
                    Intent intent =new Intent(LandingActivity.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                    }else {
                        Intent intent2 =new Intent(LandingActivity.this, OrderListActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                }
                if (response.getData().getIsMessageForUser()){
                   Log.i(TAG,"user have message");
                }
            }else {
                helper.showSnakBar(containerView,"App not Updated!");
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Login error");
            }
        }
    }
}