package orderFlex.paymentCollection.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import orderFlex.paymentCollection.Model.APICallings.PullTotadyOrder;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderResponse;
import orderFlex.paymentCollection.PaymentActivity.PaymentMethod;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

public class MainActivity extends AppCompatActivity implements PullTotadyOrder.TodayOrderListener,AdapterOrderList.UpdateTotalBill {

    LinearLayout addNewPayment;
    private RecyclerView orderList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager prefManager;
    private String TAG="MainActivity";
    private PullTotadyOrder pullTotadyOrder;
    private Helper helper;
    private AdapterOrderList adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address;
    private View containerVied;
    private TodayOrderResponse orderResponse=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNewPayment=findViewById(R.id.addNewPayment);
        orderList=findViewById(R.id.orderList);
        totalBill=findViewById(R.id.totalBill);
        prefManager=new SharedPrefManager(this);
        containerVied=findViewById(R.id.mainActivity);
        updateProfile();
        pullTotadyOrder=new PullTotadyOrder(this);
        helper=new Helper(this);
        Log.i(TAG,"Client ID: "+prefManager.getClientId());
        Log.i(TAG,"Username: "+prefManager.getUsername());
        Log.i(TAG,"Password: "+prefManager.getUserPassword());
        TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());
        if (helper.isInternetAvailable()){
            pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
        }else {
            helper.showSnakBar(containerVied,"Please check your internet connection!");
        }


        addNewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderResponse!=null){
                    Intent intent =new Intent(MainActivity.this, PaymentMethod.class);
                    intent.putExtra("order_id",orderResponse.getOrderDetails().get(0).getOrderForClientId());
                    startActivity(intent);
                }else {
                    Intent intent =new Intent(MainActivity.this, PaymentMethod.class);
                    intent.putExtra("order_id","test01");
                    startActivity(intent);
                }

                //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());
                if (helper.isInternetAvailable()){
                    pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
                }else {
                    helper.showSnakBar(containerVied,"Please check your internet connection!");
                }
                break;
            case R.id.logout:
                prefManager.setLoggedInFlag(false);
                Intent intent=new Intent(MainActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
                break;
            case R.id.userProfile:
                break;
        }
        return true;
    }

    @Override
    public void onResponse(TodayOrderResponse response, int code) {
        Log.i(TAG,"Response Code:"+code);
        if (response!=null&&code==202){
            adapter=new AdapterOrderList(this,response.getOrderDetails());
            layoutManager = new LinearLayoutManager(this);
            orderList.setLayoutManager(layoutManager);
            orderList.setAdapter(adapter);
        }else {
            helper.showSnakBar(containerVied,"Server not Responding");
        }

    }

    @Override
    public void billUpdate(float totalTaka) {
        totalBill.setText(String.valueOf(totalTaka));
        Log.i(TAG,"Total bill: "+totalTaka);
    }

    private void updateProfile(){
        clientCode=findViewById(R.id.clientCode);
        name=findViewById(R.id.name);
        presenterName=findViewById(R.id.presenterName);
        phoneNo=findViewById(R.id.phoneNo);
        address=findViewById(R.id.address);

        clientCode.setText(prefManager.getClientCode());
        name.setText(prefManager.getClientName());
        presenterName.setText(prefManager.getPresenterName());
        phoneNo.setText(prefManager.getClientContactNumber());
        address.setText(prefManager.getClientAddress());
    }
}
