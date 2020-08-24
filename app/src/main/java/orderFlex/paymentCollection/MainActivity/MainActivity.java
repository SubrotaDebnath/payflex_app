package orderFlex.paymentCollection.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Model.APICallings.GetProductList;
import orderFlex.paymentCollection.Model.APICallings.PullPaymentsList;
import orderFlex.paymentCollection.Model.APICallings.PullTotadyOrder;
import orderFlex.paymentCollection.Model.APICallings.SaveOrderHandler;
import orderFlex.paymentCollection.Model.APICallings.UpdateOrderHandler;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
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
        PullPaymentsList.PullPaymentsListListener,UpdateOrderHandler.UpdateOrderListener, GetProductList.GetProductListListener,
        AdapterOrderTakeForm.UpdateTotalBill, SaveOrderHandler.SaveOrderListener {

    LinearLayout addNewPayment;
    private RecyclerView orderList,paymentList,takeOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager prefManager;
    private String TAG="MainActivity";
    private PullTotadyOrder pullTotadyOrder;
    private Helper helper;
    private AdapterOrderList adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address,orderTitle;
    private View containerVied;
    private TodayOrderResponse orderResponse=null;
    private LinearLayout orderDetailsBlock,orderTakeSegment;
    private CardView updateOrder,saveOrder;
    private TextView noOrder,totalTakenBill,orderCode,orderDate;
    private PullPaymentsList pullPaymentsList;
    private AdapterPaymentList adapterPaymentList;
    private List<UpdateOrderRequestBody> updateOrderRequestBodyList=new ArrayList<>();
    private UpdateOrderHandler updateOrderHandler=new UpdateOrderHandler(this);
    private AdapterOrderTakeForm adapterOrderTakeForm;
    private List<SaveOrderRequest> saveOrderRequestsBody;
    private SaveOrderHandler saveOrderHandler;

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
        orderTitle=findViewById(R.id.orderTitle);
        orderCode=findViewById(R.id.orderCode);
        orderDate=findViewById(R.id.orderDate);

        orderTakeSegment=findViewById(R.id.orderTakeSegment);
        takeOrderList=findViewById(R.id.takeOrderList);
        totalTakenBill=findViewById(R.id.totalTakenBill);
        saveOrder=findViewById(R.id.saveOrder);
        saveOrderHandler=new SaveOrderHandler(this);

        updateProfile();
        pullTotadyOrder=new PullTotadyOrder(this);
        pullPaymentsList=new PullPaymentsList(this);

        helper=new Helper(this);
        Log.i(TAG,"Client ID: "+prefManager.getClientId());
        Log.i(TAG,"Username: "+prefManager.getUsername());
        Log.i(TAG,"Password: "+prefManager.getUserPassword());
        TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());

        orderDate.setText(helper.getDate());

        try {
            Intent intent=getIntent();
            String message=intent.getStringExtra("payment_massege");
            if (message == null){
                helper.showSnakBar(containerVied,"Refreshing the dashboard...!");
            }else {
                helper.showSnakBar(containerVied,message);
            }
        }catch (Exception e){
            Log.i(TAG,"Intent Exception: "+ e.toString());
        }

        if (helper.isInternetAvailable())
        {
            pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
        }
        else
            {
            helper.showSnakBar(containerVied,"No internet! Please check your internet connection!");
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
                    helper.showSnakBar(containerVied,"You don't have previous order! Please save an order first");
                }
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateOrderRequestBodyList.size()>0){
//                    updateOrderHandler.pushUpdatedOrder("admin@total.com","abcdtotal",updateOrderRequestBodyList);
                    updateOrderHandler.pushUpdatedOrder(prefManager.getUsername(),prefManager.getUserPassword(),updateOrderRequestBodyList);
                }
            }
        });

        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isInternetAvailable()){
                    Gson gson=new Gson();
                    String response=gson.toJson(saveOrderRequestsBody);
                    Log.i(TAG,"Response Body: "+response);
                    saveOrderHandler.pushSaveOrder(prefManager.getUsername(),prefManager.getUserPassword(),saveOrderRequestsBody);
                }else {
                    helper.showSnakBar(containerVied,"No internet! Please check your internet connection!");
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
                    helper.showSnakBar(containerVied,"Refreshing the dashboard...!");
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
                orderTitle.setText("ORDER DETAILS: ");
                orderResponse=response;
                adapter=new AdapterOrderList(this,response.getOrderDetails());
                layoutManager = new LinearLayoutManager(this);
                orderList.setLayoutManager(layoutManager);
                orderList.setAdapter(adapter);

                orderDetailsBlock.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.GONE);
                orderTakeSegment.setVisibility(View.GONE);
                saveOrder.setVisibility(View.GONE);
                orderCode.setText(orderResponse.getOrderDetails().get(0).getOrderCode());
                orderDate.setText(orderResponse.getOrderDetails().get(0).getDeliveryDate());
                orderCode.setText(response.getOrderDetails().get(0).getOrderCode());

                PaymentListRequest listRequest=new PaymentListRequest(prefManager.getClientId(),response.getOrderDetails().get(0).getOrderCode());
                pullPaymentsList.pullPaymentListCall(prefManager.getUsername(),prefManager.getUserPassword(),listRequest);
            }else {
                //noOrder.setVisibility(View.VISIBLE);
                orderDetailsBlock.setVisibility(View.GONE);
                orderTakeSegment.setVisibility(View.VISIBLE);
                saveOrder.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.GONE);
                new GetProductList(this).pullProductListCall(prefManager.getUsername(),prefManager.getUserPassword());
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerVied,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerVied,"Server not Responding! Please check your internet connection.");
            }

            noOrder.setVisibility(View.VISIBLE);
            orderDetailsBlock.setVisibility(View.GONE);
            noOrder.setVisibility(View.GONE);
            saveOrder.setVisibility(View.GONE);
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

    @Override
    public void onPaymentListResponse(PaymentListResponse response, int code) {
        if (response!=null && code==202){
            adapterPaymentList=new AdapterPaymentList(this,response.getPaymentList(),containerVied);
            layoutManager = new LinearLayoutManager(this);
            paymentList.setLayoutManager(layoutManager);
            paymentList.setAdapter(adapterPaymentList);
        }else {
            if (code==401){
                helper.showSnakBar(containerVied,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerVied,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onUpdateResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());
            helper.showSnakBar(containerVied,response.getMessage());
            if (helper.isInternetAvailable()){
                pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
                updateOrder.setVisibility(View.GONE);
            }else {
                helper.showSnakBar(containerVied,"Please check your internet connection!");
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerVied,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerVied,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onProductListResponse(ProductListResponse response, int code) {
        if (response!=null&&code==202){
            orderTitle.setText("NEW ORDER DETAILS: ");
            final List<SaveOrderRequest> orderRequestList=new ArrayList<>();

            int count=0;
            for (final ProductListResponse.ProductList product:response.getProductList()) {
                SaveOrderRequest order=new SaveOrderRequest(helper.makeUniqueID()+String.valueOf(count),
                        product.getId(),product.getPName(),product.getPType(),
                        "0",prefManager.getClientId(),prefManager.getHandlerId(),
                        helper.getDate(),"1",helper.getDate(),"2");
                orderRequestList.add(order);
                Log.i(TAG,"Name: "+product.getPName()+" Type: "+product.getPType());
                count++;
            }
            count=0;

//            for (int i=0;i<orderRequestList.size();i++){
//                if (i%2==0){
//                    Log.i(TAG, "Check Index: "+i);
//                    SaveOrderRequest temp=orderRequestList.get(i);
//                    orderRequestList.set(0,orderRequestList.get(i+1));
//                    orderRequestList.set(i+1,temp);
//                    Log.i(TAG,"Index: "+i+" Name: "+orderRequestList.get(i).getProduct_name()+" Type: "+orderRequestList.get(i).getProduct_type());
//                    Log.i(TAG,"Index: "+(i+1)+" Name: "+orderRequestList.get(i+1).getProduct_name()+" Type: "+orderRequestList.get(i+1).getProduct_type());
//                }
//            }

            for (SaveOrderRequest data:orderRequestList) {
                Log.i(TAG,"Recheck: "+" Name: "+data.getProduct_name()+" Type: "+data.getProduct_type());
            }
            //adapter operation 01726574448 hadibur zaman
            Log.i(TAG,"Total products: "+response.getProductList().size());
            adapterOrderTakeForm=new AdapterOrderTakeForm(this,orderRequestList,response.getProductList());
            layoutManager = new LinearLayoutManager(this);
            takeOrderList.setLayoutManager(layoutManager);
            takeOrderList.setAdapter(adapterOrderTakeForm);
            //
        }else {
            if (code==401){
                helper.showSnakBar(containerVied,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerVied,"Server not Responding! Please check your internet connection.");
            }
        }
    }
    @Override
    public void saveBillUpdate(List<SaveOrderRequest> list, float totalTaka, boolean change) {
        saveOrderRequestsBody=list;
        totalTakenBill.setText(String.valueOf(totalTaka));
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
    public void onSaveResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderRequest request=new TodayOrderRequest(prefManager.getClientId(),helper.getDate());
            helper.showSnakBar(containerVied,response.getMessage());
            if (helper.isInternetAvailable()){
                pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
                saveOrder.setVisibility(View.GONE);
            }else {
                helper.showSnakBar(containerVied,"Please check your internet connection!");
            }
        }else {
            if (code==401){
            helper.showSnakBar(containerVied,"Unauthorized Username or Password!");
        }else {
            helper.showSnakBar(containerVied,"Server not Responding! Please check your internet connection.");
        }
        }
    }
}
