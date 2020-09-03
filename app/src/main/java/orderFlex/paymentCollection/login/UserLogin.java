package orderFlex.paymentCollection.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

import orderFlex.paymentCollection.Model.APICallings.LoginAPICalling;
import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class UserLogin extends BaseActivity
        implements LoginAPICalling.LoginListener{
    private EditText username, password;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
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
                apiCalling.loginCall(u_name,u_pass);
            }
            else
                {
                helper.showSnakBar(containerView,"Please check your internet connection!");
            }
        }

    }

    @Override
    public void onResponse(LoginResponse response,int code) {
        if (response!=null && code==202){
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
                Intent intent=new Intent(UserLogin.this, OrderListActivity.class);
                startActivity(intent);
                finish();

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
}
