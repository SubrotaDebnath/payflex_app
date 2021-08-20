package orderFlex.paymentCollection.Activityes.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import orderFlex.paymentCollection.Model.APICallings.LoginAPICalling;
import orderFlex.paymentCollection.Model.APICallings.Send_OTP_ForVerification;
import orderFlex.paymentCollection.Model.LoginData.LoginClientRequestBody;
import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Model.LoginData.OTP_Response;
import orderFlex.paymentCollection.Model.LoginData.OTP_verificationRequestBody;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class UserLogin extends BaseActivity
        implements LoginAPICalling.LoginListener, Send_OTP_ForVerification.OTP_ResponseListener {
    private TextInputEditText password,username;
//    private TextView status;
    private LoginAPICalling apiCalling;
    private Helper helper;
    private View containerView;
    private SharedPrefManager prefManager;
    private String u_name, u_pass;
    private Button login;
    private AppCompatActivity mContext;
    private CardView bangle_key,english_key;
    private AppCompatActivity context;
    private static final String TAG = "UserLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
//        status = findViewById(R.id.loginStatus);
        containerView=findViewById(R.id.login_activity);
        helper=new Helper(this);
        apiCalling=new LoginAPICalling(this);
        prefManager=new SharedPrefManager(this);
        login=findViewById(R.id.login_btn);
        context=this;

        login.setText(R.string.login);

//        setNewLocale(this, LocaleManager.BANGLA);

        if (prefManager.isLoggedIn()){
            Intent intent=new Intent(UserLogin.this, OrderListActivity.class);
            startActivity(intent);
            finish();
        }
        bangle_key=findViewById(R.id.bangle_key);
        english_key=findViewById(R.id.english_key);
        bangle_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewLocale(context,LocaleManager.BANGLA);
            }
        });
        english_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewLocale(context,LocaleManager.ENGLISH);
            }
        });
    }


    public void Login(View view) {
        u_name=username.getText().toString();
        u_pass=password.getText().toString();
        if (u_name.isEmpty()){
            helper.showSnakBar(containerView,"Empty username");
            return;
        }
        if (u_pass.isEmpty()){
            helper.showSnakBar(containerView,"Empty password");
            return;
        }
        {
            if(helper.isInternetAvailable())
            {
                LoginClientRequestBody body = new LoginClientRequestBody();
                body.setAndroidId(helper.getAnrdoidID());
                apiCalling.loginCall(u_name,u_pass, body);
            }
            else
                {
                helper.showSnakBar(containerView,"Please check your internet connection!");
            }
        }

    }

    @Override
    public void onResponse(final LoginResponse response, int code) {
        if (response!=null && code==202){
            if (response.getNewDevice() && response.getValidDevice()){
                //////////////////////////////////////////////////////////////////////////////////////
                prefManager.setLoggedInFlag(false);
                prefManager.setUserNane(u_name);
                prefManager.setUserPassword(u_pass);
                prefManager.setUserType(response.getUserType());
                prefManager.setClientPairId(response.getClientPairID());
                prefManager.setClientId(response.getClientId());
                prefManager.setClientName(response.getName());
                if(response.getContacts().size()>0){
                    for (LoginResponse.Contact contact:response.getContacts()) {
                        if (contact.getContactTypeId().equals("1")){
                            prefManager.setClientContactNumber(contact.getContactValue());
                        }else{
                            prefManager.setClientContactNumber("");
                        }
                        if (contact.getContactTypeId().equals("4")){
                            prefManager.setClientAddress(contact.getContactValue());
                        }else {
                            prefManager.setClientAddress("");
                        }
                        if (contact.getContactTypeId().equals("")){
                            prefManager.setClientEmail(contact.getContactValue());
                        }else {
                            prefManager.setClientEmail("");
                        }
                    }
                }else {
                    prefManager.setClientContactNumber("");
                    prefManager.setClientAddress("");
                    prefManager.setClientEmail("");
                }
                prefManager.setPresenterName(response.getRepresentativeName());
                prefManager.setClientCode(response.getClientCode());
                prefManager.setHandlerId(response.getHandlerId());
                prefManager.setClientVirtualAccountNumber(response.getVirtualAccountNo());
                prefManager.setProImgUrl(response.getImage_url());
                /////////////////////////////////////////////////////////////////////////////////////////////////////
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("OTP");
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout root = (LinearLayout) inflater.inflate(R.layout.otp_et, null);
                final EditText otpET = root.findViewById(R.id.otpET);
                final Button okBTN = root.findViewById(R.id.otpOkBTN);
                builder.setView(root);
                builder.setCancelable(false);
                okBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String otp = otpET.getText().toString();
                        if (otp.length()>0){
                            new Send_OTP_ForVerification(context).otpVerification(prefManager.getUsername(),
                                    prefManager.getUserPassword(),
                                    new OTP_verificationRequestBody(prefManager.getClientId(),
                                            helper.getAnrdoidID(), otpET.getText().toString()));
                            Log.i(TAG, "OTP: "+otpET.getText().toString());

                        }else {
                            otpET.setError("OTP field can't be empty");
                        }
                    }
                });
               /* builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (otpET.getText().toString()!= null || !otpET.getText().toString().equals("")){
                                new Send_OTP_ForVerification(context).otpVerification(response.getUsername(),
                                        response.getPassword(), new OTP_verificationRequestBody(response.getClientId(), helper.getAnrdoidID(), otpET.getText().toString()));

                            }else {
                                otpET.setError("OTP field can't be empty");
                                builder.show();
                            }
                        }catch (Exception e){
                            helper.showSnakBar(containerView, e.getMessage());
                        }

                    }
                });*/
                builder.show();
            }else if (!response.getValidDevice() && response.getNewDevice()){
                helper.showSnakBar(containerView,"You have reached your device limit. Please contact to the head office for add a new device.");
            }else if (!response.getValidDevice() && !response.getNewDevice()){
                helper.showSnakBar(containerView,"You have reached your device limit. Please contact to the head office for add a new device.");
            }else if (response.getValidDevice() && !response.getNewDevice()){
                helper.showSnakBar(containerView,"Successfully login!");
                prefManager.setLoggedInFlag(true);
                prefManager.setUserNane(u_name);
                prefManager.setUserPassword(u_pass);
                prefManager.setUserType(response.getUserType());
                prefManager.setClientPairId(response.getClientPairID());
                prefManager.setClientId(response.getClientId());
                prefManager.setClientName(response.getName());
                if(response.getContacts().size()>0){
                    for (LoginResponse.Contact contact:response.getContacts()) {
                        if (contact.getContactTypeId().equals("1")){
                            prefManager.setClientContactNumber(contact.getContactValue());
                        }else{
                            prefManager.setClientContactNumber("");
                        }
                        if (contact.getContactTypeId().equals("4")){
                            prefManager.setClientAddress(contact.getContactValue());
                        }else {
                            prefManager.setClientAddress("");
                        }
                        if (contact.getContactTypeId().equals("")){
                            prefManager.setClientEmail(contact.getContactValue());
                        }else {
                            prefManager.setClientEmail("");
                        }
                    }
                }else {
                    prefManager.setClientContactNumber("");
                    prefManager.setClientAddress("");
                    prefManager.setClientEmail("");
                }
                prefManager.setPresenterName(response.getRepresentativeName());
                prefManager.setClientCode(response.getClientCode());
                prefManager.setHandlerId(response.getHandlerId());
                prefManager.setClientVirtualAccountNumber(response.getVirtualAccountNo());
                prefManager.setProImgUrl(response.getImage_url());
                Intent intent=new Intent(UserLogin.this, OrderListActivity.class);
                startActivity(intent);
                finish();
            }

        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Login error");
            }
        }

    }


    //For multi-language operation
    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(mContext, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        Locale locale = new Locale(LocaleManager.getLanguagePref(this));
        Locale.setDefault(locale);
        overrideConfiguration.setLocale(locale);
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    public void onResponse(OTP_Response response, int code) {
        if (response !=null && code == 202){
            if (response.getIsValid()){
                helper.showSnakBar(containerView,"Successfully login!");
                prefManager.setLoggedInFlag(true);
                startActivity(new Intent(UserLogin.this, OrderListActivity.class));
                finish();
            }else {
                helper.showSnakBar(containerView,"login denied!");
                showOTP_Dialog();
            }
        }else {
            if (code == 401){
                helper.showSnakBar(containerView,"Authentication Error");
            }else {
                helper.showSnakBar(containerView,"Server not found");
            }

        }
    }

    void showOTP_Dialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("OTP");
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout root = (LinearLayout) inflater.inflate(R.layout.otp_et, null);
        final EditText otpET = root.findViewById(R.id.otpET);
        final Button okBTN = root.findViewById(R.id.otpOkBTN);
        builder.setView(root);
        builder.setCancelable(false);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpET.getText().toString();
                if (otp.length()>0){
                    new Send_OTP_ForVerification(context).otpVerification(prefManager.getUsername(),
                            prefManager.getUserPassword(), new OTP_verificationRequestBody(prefManager.getClientId(), helper.getAnrdoidID(), otpET.getText().toString()));

                    Log.i(TAG, "OTP: "+otpET.getText().toString());

                }else {
                    otpET.setError("OTP field can't be empty");
                }
            }
        });
        builder.show();
    }

}
