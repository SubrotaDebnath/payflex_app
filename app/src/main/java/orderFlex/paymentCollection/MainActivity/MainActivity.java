package orderFlex.paymentCollection.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Model.APICallings.PullPaymentsList;
import orderFlex.paymentCollection.Model.APICallings.PullTotadyOrder;
import orderFlex.paymentCollection.Model.APICallings.UpdateOrderHandler;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderResponse;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.PaymentActivity.PaymentActivity;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

public class MainActivity extends AppCompatActivity implements PullTotadyOrder.TodayOrderListener,AdapterOrderList.UpdateTotalBill,
        PullPaymentsList.PullPaymentsListListener,UpdateOrderHandler.UpdateOrderListener {

    LinearLayout addNewPayment;
    private RecyclerView orderList,paymentList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager prefManager;
    private String TAG="MainActivity";
    private PullTotadyOrder pullTotadyOrder;
    private Helper helper;
    private AdapterOrderList adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address;
    private View containerVied;
    private TodayOrderResponse orderResponse=null;
    private LinearLayout orderDetailsBlock;
    private CardView updateOrder;
    private TextView noOrder;
    private PullPaymentsList pullPaymentsList;
    private AdapterPaymentList adapterPaymentList;
    List<UpdateOrderRequestBody> updateOrderRequestBodyList=new ArrayList<>();
    private UpdateOrderHandler updateOrderHandler=new UpdateOrderHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNewPayment=findViewById(R.id.addNewPayment);
        orderList=findViewById(R.id.orderList);
        totalBill=findViewById(R.id.totalBill);
        prefManager=new SharedPrefManager(this);
        orderDetailsBlock=findViewById(R.id.orderDetailsBlock);
        noOrder=findViewById(R.id.noOrder);
        containerVied=findViewById(R.id.mainActivity);
        updateOrder=findViewById(R.id.updateOrder);
        updateOrder.setVisibility(View.GONE);
        paymentList=findViewById(R.id.paymentList);

        updateProfile();
        pullTotadyOrder=new PullTotadyOrder(this);
        pullPaymentsList=new PullPaymentsList(this);

        helper=new Helper(this);
        Log.i(TAG,"Client ID: "+prefManager.getClientId());
        Log.i(TAG,"Username: "+prefManager.getUsername());
        Log.i(TAG,"Password: "+prefManager.getUserPassword());
        TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());
        if (helper.isInternetAvailable())
        {
            pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
        }
        else
            {
            helper.showSnakBar(containerVied,"Please check your internet connection!");
        }
        addNewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderResponse!=null){
                    Intent intent =new Intent(MainActivity.this, PaymentActivity.class);
                    Log.i(TAG,"Order Code: "+orderResponse.getOrderDetails().get(0).getOrderCode());
                    intent.putExtra("order_code",orderResponse.getOrderDetails().get(0).getOrderCode());
                    startActivity(intent);
                }else {
//                    Intent intent =new Intent(MainActivity.this, PaymentActivity.class);
//                    intent.putExtra("order_id",orderResponse.getOrderDetails().get(0).getOrderCode());
//                    startActivity(intent);
                }
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateOrderRequestBodyList.size()>0){
                    updateOrderHandler.pushUpdatedOrder("admin@total.com","abcdtotal",updateOrderRequestBodyList);
                }
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
                //updateOrder.setVisibility(View.GONE);
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
        if (response!=null && code==202){
            if (response.getOrderDetails().size()>0){
                orderResponse=response;
                adapter=new AdapterOrderList(this,response.getOrderDetails());
                layoutManager = new LinearLayoutManager(this);
                orderList.setLayoutManager(layoutManager);
                orderList.setAdapter(adapter);
                orderDetailsBlock.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.GONE);
                PaymentListRequest listRequest=new PaymentListRequest(prefManager.getClientId(),response.getOrderDetails().get(0).getOrderCode());
                pullPaymentsList.pullPaymentListCall(prefManager.getUsername(),prefManager.getUserPassword(),listRequest);
            }else {
                noOrder.setVisibility(View.VISIBLE);
                orderDetailsBlock.setVisibility(View.GONE);
            }
        }else {
            helper.showSnakBar(containerVied,"Server not Responding");
            noOrder.setVisibility(View.VISIBLE);
            orderDetailsBlock.setVisibility(View.GONE);
        }

    }

    @Override
    public void billUpdate(List<TodayOrderResponse.OrderDetail> list,float totalTaka, boolean change) {
        totalBill.setText(String.valueOf(totalTaka));
        Log.i(TAG,"Total bill: "+totalTaka);
        if (change){
            updateOrder.setVisibility(View.VISIBLE);
            Log.i(TAG,"Bill changed");
        }else {
            updateOrder.setVisibility(View.GONE);
            Log.i(TAG,"Bill has no change");
        }

        for (TodayOrderResponse.OrderDetail updateData:list){
            UpdateOrderRequestBody data=new UpdateOrderRequestBody(updateData.getTxid(),updateData.getProductId(),updateData.getPName(),
                    updateData.getPType(),updateData.getQuantityes(),updateData.getOrderForClientId(),updateData.getTakerId(),updateData.getDeliveryDate(),
                    updateData.getPlant(),updateData.getTakingDate(),updateData.getOrderType());

            updateOrderRequestBodyList.add(data);
        }
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

    @Override
    public void onPaymentListResponse(PaymentListResponse response, int code) {
        if (response!=null && code==202){
            adapterPaymentList=new AdapterPaymentList(this,response.getPaymentList());
            layoutManager = new LinearLayoutManager(this);
            paymentList.setLayoutManager(layoutManager);
            paymentList.setAdapter(adapterPaymentList);
        }
    }

    @Override
    public void onUpdateResponse(UpdateOrderResponse response, int code) {

    }
}
