package orderFlex.paymentCollection.CustomerOrderList;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import orderFlex.paymentCollection.Model.APICallings.GetProductList;
import orderFlex.paymentCollection.Model.APICallings.PullCustomerOrderList;
import orderFlex.paymentCollection.Model.APICallings.SaveOrderHandler;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

public class OrderListActivity extends BaseActivity
        implements
        PullCustomerOrderList.OrderListListener,
        GetProductList.GetProductListListener,
        AdapterOrderForm.UpdateTotalBill,
        SaveOrderHandler.SaveOrderListener
{

    private SharedPrefManager prefManager;
    private Helper helper;
    private Context context=this;
    private String TAG="OrderListActivity";
    private AdapterOrderForm adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address,listTitle,totalTakenBill;
    private View containerView;
    private LinearLayout orderCodeView,orderTakeSegment;
    private TextView orderDate;
    private RecyclerView bookedOrderList,takeCustomerOrderList;
    private TextView warningText;
    //adapter objects
    private RecyclerView.LayoutManager layoutManager;
    private AdapterListOfOrder adapterListOfOrder;
    private AdapterOrderForm adapterOrderForm;
    private List<SaveOrderRequest> saveOrderRequestsBody;
    //Order Save/Cancel
    private CardView cancelOrder,saveOrder,addNewOrder;
    private SaveOrderHandler saveOrderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        prefManager=new SharedPrefManager(this);
        helper=new Helper(this);
        containerView=findViewById(R.id.orderListActivity);
        //segment view
        warningText=findViewById(R.id.warningText);
        bookedOrderList=findViewById(R.id.bookedOrderList);
        orderTakeSegment=findViewById(R.id.orderTakeSegment);
        listTitle=findViewById(R.id.listTitle);
        takeCustomerOrderList=findViewById(R.id.takeCustomerOrderList);
        //new order segment
        totalTakenBill=findViewById(R.id.totalTakenBill);

        //default operations
        updateProfile();

        //open order form
        addNewOrder=findViewById(R.id.addNewOrder);
        try {
            Intent intent=getIntent();
            String add_order = intent.getStringExtra("add_order");
            Log.i(TAG,"Intent: "+add_order);

            if (add_order.equals("take_order")){
                addNewOrderFormCall();
            }else {
                operationOrderPull();
            }
        }catch (Exception e){

        }
        addNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOrderFormCall();
            }
        });
        //save order
        saveOrder=findViewById(R.id.save_action);
        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalBill=Double.parseDouble(totalTakenBill.getText().toString());
                if (helper.isInternetAvailable()){
                    if (totalBill>0.0){
                        Gson gson=new Gson();
                        String response=gson.toJson(saveOrderRequestsBody);
                        Log.i(TAG,"Response Body: "+response);
                        saveOrderHandler=new SaveOrderHandler(context);
                        saveOrderHandler.pushSaveOrder(prefManager.getUsername(),prefManager.getUserPassword(),saveOrderRequestsBody);
                    }else {
                        helper.showSnakBar(containerView,"You did not give any quantity of any product!");
                    }
                }else {
                    helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
                }
            }
        });
        //cancel
        cancelOrder=findViewById(R.id.cancel_action);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationOrderPull();
            }
        });
    }

    private void updateProfile(){
        clientCode=findViewById(R.id.clientCode);
        name=findViewById(R.id.name);
        presenterName=findViewById(R.id.presenterName);
        phoneNo=findViewById(R.id.phoneNo);
        address=findViewById(R.id.address);
        orderCodeView=findViewById(R.id.orderCodeView);
        orderDate=findViewById(R.id.orderDate);

        clientCode.setText(prefManager.getClientCode());
        name.setText(prefManager.getClientName());
        presenterName.setText(prefManager.getPresenterName());
        phoneNo.setText(prefManager.getClientContactNumber());
        address.setText(prefManager.getClientAddress());
        orderCodeView.setVisibility(View.GONE);
        orderDate.setText(helper.getDateInEnglish());
        listTitle.setText("Order List");
    }
    private void operationOrderPull(){
        warningText.setVisibility(View.GONE);
        bookedOrderList.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.GONE);
        listTitle.setText("Order List");

        CustomerOrderListRequest request=new CustomerOrderListRequest(prefManager.getClientId(),helper.getDateInEnglish(),helper.getDateInEnglish());
        if (helper.isInternetAvailable()){
            helper.showSnakBar(containerView,"Refreshing the dashboard...!");
            new PullCustomerOrderList(this).pullCustomerOrderListCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
        }else {
            helper.showSnakBar(containerView,"Please check your internet connection!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                operationOrderPull();
                //updateOrder.setVisibility(View.GONE);
                break;
            case R.id.logout:
                prefManager.setLoggedInFlag(false);
                Intent intent=new Intent(OrderListActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
                break;
            case R.id.change_language:
                selectLanguage(this);
                break;
        }
        return true;
    }

    private void addNewOrderFormCall(){
        warningText.setVisibility(View.GONE);
        bookedOrderList.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.VISIBLE);
        addNewOrder.setVisibility(View.GONE);
        listTitle.setText("Create new order");
        new GetProductList(this).pullProductListCall(prefManager.getUsername(),prefManager.getUserPassword());
    }
//pre booked order list
    @Override
    public void onCustomerOrderListResponse(CustomerOrderListResponse response, int code) {
        Log.i(TAG,"Order List Response Code: "+code+" Array size: "+response.getOrderDetails().size());
        if (response.getOrderDetails().size()>0){
            warningText.setVisibility(View.GONE);
            bookedOrderList.setVisibility(View.VISIBLE);
            orderTakeSegment.setVisibility(View.GONE);
            addNewOrder.setVisibility(View.VISIBLE);
            listTitle.setText("Order List");

            adapterListOfOrder=new AdapterListOfOrder(this,response.getOrderDetails());
            layoutManager = new LinearLayoutManager(this);
            bookedOrderList.setLayoutManager(layoutManager);
            bookedOrderList.setAdapter(adapterListOfOrder);

        }else {
            listTitle.setText("Order List");
            warningText.setVisibility(View.VISIBLE);
            addNewOrder.setVisibility(View.VISIBLE);
            orderTakeSegment.setVisibility(View.GONE);
            bookedOrderList.setVisibility(View.GONE);
        }
    }
//new order taking form
    @Override
    public void onProductListResponse(ProductListResponse response, int code) {
        if (response!=null&&code==202){
            listTitle.setText("NEW ORDER DETAILS");
            warningText.setVisibility(View.GONE);
            orderTakeSegment.setVisibility(View.VISIBLE);
            addNewOrder.setVisibility(View.GONE);
            bookedOrderList.setVisibility(View.GONE);

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
            adapterOrderForm=new AdapterOrderForm(this,orderRequestList,response.getProductList());
            layoutManager = new LinearLayoutManager(this);
            takeCustomerOrderList.setLayoutManager(layoutManager);
            takeCustomerOrderList.setAdapter(adapterOrderForm);
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
    public void saveBillUpdate(List<SaveOrderRequest> list, double totalTaka, boolean change) {
        saveOrderRequestsBody=list;
        totalTakenBill.setText(String.valueOf(totalTaka));
    }

    @Override
    public void onSaveResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderDetailsByDataRequest request=new TodayOrderDetailsByDataRequest(prefManager.getClientId(),helper.getDateInEnglish());
            helper.showSnakBar(containerView,response.getMessage());
            if (helper.isInternetAvailable()){
                operationOrderPull();
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
    protected void onResume() {
        super.onResume();
        operationOrderPull();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        operationOrderPull();
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