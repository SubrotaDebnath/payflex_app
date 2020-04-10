package orderFlex.paymentCollection.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import orderFlex.paymentCollection.MainActivity.MainActivity;
import orderFlex.paymentCollection.Model.APICallings.LoginAPICalling;
import orderFlex.paymentCollection.Model.LoginData.LoginResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class UserLogin extends AppCompatActivity implements LoginAPICalling.LoginListener{
    private EditText username, password;
    private TextView status;
    private LoginAPICalling apiCalling;
    private Helper helper;
    private View containerView;
    private SharedPrefManager prefManager;
    private String u_name, u_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        status = findViewById(R.id.loginStatus);
        containerView=findViewById(R.id.login_activity);
        helper=new Helper(this);
        apiCalling=new LoginAPICalling(this);
        prefManager=new SharedPrefManager(this);
        if (prefManager.isLoggedIn()){
            Intent intent=new Intent(UserLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
        //if (helper.isInternetAvailable())
        {
            apiCalling.loginCall(u_name,u_pass);
        }
//        else {
//            helper.showSnakBar(containerView,"Please check you internet connection");
//        }
    }

    @Override
    public void onResponse(LoginResponse response,int code) {
        if (response!=null){
            if (code==202){
                helper.showSnakBar(containerView,"Successfully login!");
                prefManager.setLoggedInFlag(true);
                prefManager.setUserNane(u_name);
                prefManager.setUserPassword(u_pass);
                prefManager.setUserType(response.getUser_type());
                prefManager.setClientPairId(response.getClient_pairID());
                prefManager.setClientId(response.getClient_id());
                prefManager.setClientName(response.getName());
                prefManager.setClientEmail(response.getEmail());
                prefManager.setClientContactNumber(response.getContact_number());
                prefManager.setClientAddress(response.getAddress());
                prefManager.setPresenterName(response.getPresenter_name());
                prefManager.setClientCode(response.getClient_code());
                Intent intent=new Intent(UserLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                helper.showSnakBar(containerView,"Unauthorized login!");
            }
        }else {
            helper.showSnakBar(containerView,"Login error");
        }
    }
}
