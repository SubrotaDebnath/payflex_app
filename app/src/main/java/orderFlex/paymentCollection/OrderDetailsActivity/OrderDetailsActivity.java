package orderFlex.paymentCollection.OrderDetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import orderFlex.paymentCollection.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Model.APICallings.GetProductList;
import orderFlex.paymentCollection.Model.APICallings.PullOrderDetailsByOrderCode;
import orderFlex.paymentCollection.Model.APICallings.PullPaymentsList;
import orderFlex.paymentCollection.Model.APICallings.SaveOrderHandler;
import orderFlex.paymentCollection.Model.APICallings.UpdateOrderHandler;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataRequest;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataResponse;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByCodeRequest;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.PaymentActivity.PaymentActivity;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

public class OrderDetailsActivity
        extends BaseActivity
        implements
        PullOrderDetailsByOrderCode.TodayOrderListener,
        AdapterOrderedProductList.UpdateTotalBill,
        PullPaymentsList.PullPaymentsListListener,
        UpdateOrderHandler.UpdateOrderListener,
        GetProductList.GetProductListListener,
        AdapterOrderTakeForm.UpdateTotalBill,
        SaveOrderHandler.SaveOrderListener {

    LinearLayout addNewPayment;
    private RecyclerView orderList,paymentList,takeOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager prefManager;
    private String TAG="MainActivity",booked_code="";
    private PullOrderDetailsByOrderCode pullTotadyOrder;
    private Helper helper;
    private AdapterOrderedProductList adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address,orderTitle;
    private View containerView;
    private TodayOrderDetailsByDataResponse orderResponse=null;
    private LinearLayout orderDetailsBlock,orderTakeSegment;
    private CardView updateOrder,saveOrder;
    private TextView noOrder,totalTakenBill,orderCode,orderDate;
    private PullPaymentsList pullPaymentsList;
    private AdapterPaymentList adapterPaymentList;
    private List<UpdateOrderRequestBody> updateOrderRequestBodyList;
    private UpdateOrderHandler updateOrderHandler=new UpdateOrderHandler(this);
    private AdapterOrderTakeForm adapterOrderTakeForm;
    private List<SaveOrderRequest> saveOrderRequestsBody;
    private SaveOrderHandler saveOrderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager=new SharedPrefManager(this);
        helper=new Helper(this);

        addNewPayment=findViewById(R.id.addNewPayment);
        orderList=findViewById(R.id.orderList);
        totalBill=findViewById(R.id.totalBill);
        orderDetailsBlock=findViewById(R.id.orderDetailsBlock);
        noOrder=findViewById(R.id.noOrder);
        containerView =findViewById(R.id.mainActivity);
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
        pullPaymentsList=new PullPaymentsList(this);
        try {
            Intent intent=getIntent();
            String message=intent.getStringExtra("payment_massege");
            booked_code=intent.getStringExtra("booked_code");
            if (message == null){
                helper.showSnakBar(containerView,"Refreshing the dashboard...!");
            }else {
                helper.showSnakBar(containerView,message);
            }
        }catch (Exception e){
            Log.i(TAG,"Intent Exception: "+ e.toString());
        }

        orderDate.setText(helper.getDateInEnglish());

        if (helper.isInternetAvailable()) {
            operationOrderDetail();
        }
        else {
            helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
        }

        addNewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderResponse!=null){
                    Intent intent =new Intent(OrderDetailsActivity.this, PaymentActivity.class);
                    Log.i(TAG,"Order Code: "+orderResponse.getOrderDetails().get(0).getOrderCode());
                    intent.putExtra("order_code",orderResponse.getOrderDetails().get(0).getOrderCode());
                    startActivity(intent);
                }else {
                    helper.showSnakBar(containerView,"You don't have previous order! Please save an order first");
                }
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateOrderRequestBodyList.size()>0){
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
                    helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
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
                if (helper.isInternetAvailable()){
                    helper.showSnakBar(containerView,"Refreshing the dashboard...!");
                    operationOrderDetail();
                }else {
                    helper.showSnakBar(containerView,"Please check your internet connection!");
                }
                //updateOrder.setVisibility(View.GONE);
                break;
            case R.id.logout:
                prefManager.setLoggedInFlag(false);
                Intent intent=new Intent(OrderDetailsActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
                break;
            case R.id.change_language:
                selectLanguage(this);
                break;
            case R.id.add_new_order:
                Intent intent1=new Intent(OrderDetailsActivity.this, OrderListActivity.class);
                intent1.putExtra("add_order","take_order");
                startActivity(intent1);
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onResponse(TodayOrderDetailsByDataResponse response, int code) {
        Log.i(TAG,"Response Code:"+code);
        if (response!=null && code==202){
            if (response.getOrderDetails().size()>0){
                orderTitle.setText("ORDER DETAILS");
                orderResponse=response;
                adapter=new AdapterOrderedProductList(this,response.getOrderDetails());
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
//                new GetProductList(this).pullProductListCall(prefManager.getUsername(),prefManager.getUserPassword());
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
            noOrder.setVisibility(View.VISIBLE);
            orderDetailsBlock.setVisibility(View.GONE);
            noOrder.setVisibility(View.GONE);
            saveOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void billUpdate(List<TodayOrderDetailsByDataResponse.OrderDetail> list, float totalTaka, boolean change) {
        totalBill.setText(String.valueOf(totalTaka));
        Log.i(TAG,"Total bill: "+totalTaka);
        if (change){
            updateOrder.setVisibility(View.VISIBLE);
            Log.i(TAG,"Bill changed");
        }else {
            updateOrder.setVisibility(View.GONE);
            Log.i(TAG,"Bill has no change");
        }

        updateOrderRequestBodyList=new ArrayList<>();
        for (TodayOrderDetailsByDataResponse.OrderDetail updateData:list){
            UpdateOrderRequestBody data=new UpdateOrderRequestBody(updateData.getTxid(),updateData.getProductId(),updateData.getPName(),
                    updateData.getPType(),updateData.getQuantityes(),updateData.getOrderForClientId(),updateData.getTakerId(),updateData.getDeliveryDate(),
                    updateData.getPlant(),updateData.getTakingDate(),updateData.getOrderType());
            updateOrderRequestBodyList.add(data);
        }
    }

    @Override
    public void onPaymentListResponse(PaymentListResponse response, int code) {
        if (response!=null && code==202){
            boolean flag=helper.isEditableOrderOrPayment(helper.getDateInEnglish(),orderDate.getText().toString());
            adapterPaymentList=new AdapterPaymentList(this,response.getPaymentList(),flag, containerView);
            layoutManager = new LinearLayoutManager(this);
            paymentList.setLayoutManager(layoutManager);
            paymentList.setAdapter(adapterPaymentList);
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onUpdateResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderDetailsByDataRequest request=new TodayOrderDetailsByDataRequest(prefManager.getClientId(),helper.getDateInEnglish());
            helper.showSnakBar(containerView,response.getMessage());
            if (helper.isInternetAvailable()){
                operationOrderDetail();
                updateOrder.setVisibility(View.GONE);
            }else {
                helper.showSnakBar(containerView,"Please check your internet connection!");
            }
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onProductListResponse(ProductListResponse response, int code) {
        if (response!=null&&code==202){
            orderTitle.setText("NEW ORDER DETAILS");
            final List<SaveOrderRequest> orderRequestList=new ArrayList<>();

            int count=0;
            for (final ProductListResponse.ProductList product:response.getProductList()) {
                SaveOrderRequest order=new SaveOrderRequest(helper.makeUniqueID()+String.valueOf(count),
                        product.getId(),product.getPName(),product.getPType(),
                        "0",prefManager.getClientId(),prefManager.getHandlerId(),
                        helper.getDateInEnglish(),"1",helper.getDateInEnglish(),"2");
                orderRequestList.add(order);
                Log.i(TAG,"Name: "+product.getPName()+" Type: "+product.getPType());
                count++;
            }
            count=0;

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
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
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
            TodayOrderDetailsByDataRequest request=new TodayOrderDetailsByDataRequest(prefManager.getClientId(),helper.getDateInEnglish());
            helper.showSnakBar(containerView,response.getMessage());
            if (helper.isInternetAvailable()){
                operationOrderDetail();
                saveOrder.setVisibility(View.GONE);
            }else {
                helper.showSnakBar(containerView,"Please check your internet connection!");
            }
        }else {
            if (code==401){
            helper.showSnakBar(containerView,"Unauthorized Username or Password!");
        }else {
            helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
        }
        }
    }

    private void operationOrderDetail(){
        TodayOrderDetailsByCodeRequest request=new TodayOrderDetailsByCodeRequest(prefManager.getClientId(),booked_code);
        pullTotadyOrder=new PullOrderDetailsByOrderCode(this);
        pullTotadyOrder.pullOrderCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
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
    private void selectLanguage(final Context context){
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.language_selection_layout,null);

        CardView bangle_key=customRoot.findViewById(R.id.bangle_key);
        CardView english_key=customRoot.findViewById(R.id.english_key);
        builder.setView(customRoot);
        builder.setTitle(R.string.language_change_menu);
        builder.setCancelable(true);
        alertDialog= builder.create();
        alertDialog.show();
        bangle_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewLocale((AppCompatActivity) context,LocaleManager.BANGLA);
                alertDialog.dismiss();
            }
        });
        english_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewLocale((AppCompatActivity) context,LocaleManager.ENGLISH);;
                alertDialog.dismiss();
            }
        });
    }

}
