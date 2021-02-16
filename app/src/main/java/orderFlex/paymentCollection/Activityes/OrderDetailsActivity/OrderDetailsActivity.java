package orderFlex.paymentCollection.Activityes.OrderDetailsActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Activityes.Offers.Offers;
import orderFlex.paymentCollection.Model.APICallings.GetProductList;
import orderFlex.paymentCollection.Model.APICallings.OrderReviseSubmit;
import orderFlex.paymentCollection.Model.APICallings.PullOrderDetailsByOrderCode;
import orderFlex.paymentCollection.Model.APICallings.PullPaymentsList;
import orderFlex.paymentCollection.Model.APICallings.PullPlantList;
import orderFlex.paymentCollection.Model.APICallings.SaveOrderHandler;
import orderFlex.paymentCollection.Model.APICallings.UpdateOrderHandler;
import orderFlex.paymentCollection.Model.OrderRevise.OrderReviseRequest;
import orderFlex.paymentCollection.Model.OrderRevise.OrderReviseResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.SaveOrderData.PlantListResponse;
import orderFlex.paymentCollection.Model.SaveOrderData.SaveOrderDetails;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataRequest;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataResponse;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByCodeRequest;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.Activityes.PaymentActivity.PaymentActivity;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.Activityes.login.UserLogin;

public class OrderDetailsActivity
        extends BaseActivity
        implements
        PullOrderDetailsByOrderCode.TodayOrderListener,
        AdapterOrderedProductList.UpdateTotalBill,
        PullPaymentsList.PullPaymentsListListener,
        UpdateOrderHandler.UpdateOrderListener,
        GetProductList.GetProductListListener,
        AdapterOrderTakeForm.UpdateTotalBill,
        SaveOrderHandler.SaveOrderListener,
        OrderReviseSubmit.OrderReviseListener,
        PullPlantList.PlantListListener,
        AdapterView.OnItemSelectedListener{

    private LinearLayout addNewPayment;
    private RecyclerView orderList,paymentList,takeOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager prefManager;
    private String TAG="OrderDetailsActivity", booked_code="";
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
    private List<SaveOrderDetails> saveOrderRequestsBody;
    private SaveOrderHandler saveOrderHandler;
    private boolean isEditable =false, isSubmitted=false;
    private ImageView proImg,pickDate;
    private MenuItem submitMenu;
    //////////////Subrota
    private PlantListResponse plantData;
    private boolean isPlantSelected = false;
    private String plantId;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        prefManager=new SharedPrefManager(this);
        helper=new Helper(this);

        addNewPayment=findViewById(R.id.addNewPayment);
        orderList=findViewById(R.id.orderList);
        totalBill=findViewById(R.id.totalBill);
        orderDetailsBlock=findViewById(R.id.orderDetailsBlock);
        noOrder=findViewById(R.id.noOrder);
        containerView =findViewById(R.id.mainActivity);
        updateOrder=findViewById(R.id.updateOrder);
        updateTV = findViewById(R.id.updateTV);
        updateOrder.setVisibility(View.GONE);
        paymentList=findViewById(R.id.paymentList);
        orderTitle=findViewById(R.id.orderTitle);
        orderCode=findViewById(R.id.orderCode);
        orderDate=findViewById(R.id.orderDate);

        orderTakeSegment=findViewById(R.id.orderTakeSegment);
        takeOrderList=findViewById(R.id.takeOrderList);
        totalTakenBill=findViewById(R.id.totalTakenBill);
        pickDate=findViewById(R.id.pickDate);
        pickDate.setVisibility(View.GONE);

//        saveOrder=findViewById(R.id.saveOrder);
        saveOrderHandler=new SaveOrderHandler(this);
        updateProfile();
        pullPaymentsList=new PullPaymentsList(this);
        try {
            Intent intent=getIntent();
            String message=intent.getStringExtra("payment_massege");
            booked_code=intent.getStringExtra("booked_code");
            isEditable =intent.getBooleanExtra("is_editable",false);
            isSubmitted =intent.getBooleanExtra("is_submitted",false);
            if (isSubmitted){
                Log.i(TAG,"Submitted");
            }else {
                Log.i(TAG,"Not submitted");
            }
            if (message == null){
                helper.showSnakBar(containerView,"Refreshing the order detail...!");
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
                    if (isSubmitted){
                        Intent intent =new Intent(OrderDetailsActivity.this, PaymentActivity.class);
                        Log.i(TAG,"Order Code: "+orderResponse.getOrderDetails().get(0).getOrderCode());
                        intent.putExtra("order_code",orderResponse.getOrderDetails().get(0).getOrderCode());
                        startActivity(intent);
                    }else {
                        helper.showSnakBar(containerView,"Please submitted your revision from the menu option");
                    }
                }else {
                    helper.showSnakBar(containerView,"You don't have previous order! Please save an order first");
                }
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlantSelected){
                    updateTV.setText("Next");
                    selectPlant();

                }if (isPlantSelected){
                    updateTV.setText("Update");
                    if (updateOrderRequestBodyList.size()>0){
                        updateOrderHandler.pushUpdatedOrder(prefManager.getUsername(),prefManager.getUserPassword(),updateOrderRequestBodyListWithPlantId);
                    }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        submitMenu=menu.findItem(R.id.revision_order);
        if (isSubmitted){
            submitMenu.setEnabled(false);
        }
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
            case R.id.revision_order:
                OrderReviseRequest requestBody=new OrderReviseRequest(booked_code,prefManager.getClientId());
                new OrderReviseSubmit(this).reviseSubmitCall(prefManager.getUsername(),prefManager.getUserPassword(),requestBody);
                break;
//            case R.id.my_offer:
//                Intent intent2 = new Intent(OrderDetailsActivity.this, Offers.class);
//                intent2.putExtra("id", "orderDetails");
//                startActivity(intent2);
//                break;

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
                adapter=new AdapterOrderedProductList(this,response.getOrderDetails(), isEditable);
                layoutManager = new LinearLayoutManager(this);
                orderList.setLayoutManager(layoutManager);
                orderList.setAdapter(adapter);

                orderDetailsBlock.setVisibility(View.VISIBLE);
                noOrder.setVisibility(View.GONE);
                orderTakeSegment.setVisibility(View.GONE);
//                saveOrder.setVisibility(View.GONE);
                orderCode.setText(orderResponse.getOrderDetails().get(0).getOrderCode());
                orderDate.setText(orderResponse.getOrderDetails().get(0).getDeliveryDate());
                orderCode.setText(response.getOrderDetails().get(0).getOrderCode());
                address.setText(response.getOrderDetails().get(0).getPlantName());
                PaymentListRequest listRequest=new PaymentListRequest(prefManager.getClientId(),response.getOrderDetails().get(0).getOrderCode());
                pullPaymentsList.pullPaymentListCall(prefManager.getUsername(),prefManager.getUserPassword(),listRequest);
            }else {
                //noOrder.setVisibility(View.VISIBLE);
                orderDetailsBlock.setVisibility(View.GONE);
                orderTakeSegment.setVisibility(View.VISIBLE);
//                saveOrder.setVisibility(View.VISIBLE);
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
//            saveOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void billUpdate(List<TodayOrderDetailsByDataResponse.OrderDetail> list, float totalTaka, boolean change) {
        totalBill.setText(String.valueOf(totalTaka));
        Log.i(TAG,"Total bill: "+totalTaka);
        if (change){
            updateOrder.setVisibility(View.VISIBLE);
            updateTV.setText("Next");
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
            final List<SaveOrderDetails> orderRequestList=new ArrayList<>();

            int count=0;
            for (final ProductListResponse.ProductList product:response.getProductList()) {
                SaveOrderDetails order=new SaveOrderDetails(helper.makeUniqueID()+String.valueOf(count),
                        product.getId(),product.getPName(),product.getPType(),
                        "0",prefManager.getClientId(),prefManager.getHandlerId(),
                        helper.getDateInEnglish(),"1",helper.getDateInEnglish(),"2");
                orderRequestList.add(order);
                Log.i(TAG,"Name: "+product.getPName()+" Type: "+product.getPType());
                count++;
            }
            count=0;
            for (SaveOrderDetails data:orderRequestList) {
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
    public void saveBillUpdate(List<SaveOrderDetails> list, float totalTaka, boolean change) {
        saveOrderRequestsBody=list;
        totalTakenBill.setText(String.valueOf(totalTaka));
    }

    private void updateProfile(){
        clientCode=findViewById(R.id.clientCode);
        name=findViewById(R.id.name);
        presenterName=findViewById(R.id.presenterName);
        phoneNo=findViewById(R.id.phoneNo);
        address=findViewById(R.id.address);
        proImg=findViewById(R.id.proImg);

        clientCode.setText(prefManager.getClientCode());
        name.setText(prefManager.getClientName());
        presenterName.setText(prefManager.getPresenterName());
        phoneNo.setText(prefManager.getClientContactNumber());
        address.setText(prefManager.getClientAddress());
        if (prefManager.getProImgUrl()!=null){
            Picasso.get()
                    .load(prefManager.getProImgUrl())
                    .placeholder(R.drawable.ic_person)
//                            .resize(100, 100)
                    .priority(Picasso.Priority.HIGH)
                    .into(proImg);
            Log.i(TAG,"Image URL: "+prefManager.getProImgUrl());
        }else {
            Log.i(TAG,"No Image found!");
        }
    }

    @Override
    public void onSaveResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderDetailsByDataRequest request=new TodayOrderDetailsByDataRequest(prefManager.getClientId(),helper.getDateInEnglish());
            helper.showSnakBar(containerView,response.getMessage());
            if (helper.isInternetAvailable()){
                operationOrderDetail();
//                saveOrder.setVisibility(View.GONE);
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

    @Override
    public void onReviseResponse(OrderReviseResponse response, int code) {
        if (response!=null && code==202){
            helper.showSnakBar(containerView,response.getMessage());
            isEditable=false;
            isSubmitted=true;
            operationOrderDetail();
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderDetailsActivity.this, OrderListActivity.class));
    }

    /////////////////////Subrota
    private void selectPlant(){
        List<String> plantNameList=new ArrayList<>();
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.plant_selection_spinner,null);
        Spinner plantSpinner=customRoot.findViewById(R.id.plantSpinner);
        CardView save=customRoot.findViewById(R.id.save_action);
        CardView cancel=customRoot.findViewById(R.id.cancel_action);
        int plantCount=0;
        for (PlantListResponse.PlantList plant: plantData.getPlantList()) {
            plantNameList.add(plant.getPlant());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,plantNameList);
        plantSpinner.setAdapter(adapter);
        plantSpinner.setSelection(0);
        plantSpinner.setOnItemSelectedListener(this);

        builder.setTitle(R.string.plant_title);
        builder.setView(customRoot);
        builder.setCancelable(true);
        alertDialog= builder.create();
        alertDialog.show();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executePlantUpdate();
                updateTV.setText("Update");
                isPlantSelected = true;
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlantSelected = false;
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onPlantListResponse(PlantListResponse response, int code) {
        if (response!=null && code==202){
            plantData=response;
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        plantId = plantData.getPlantList().get(position).getId();

        /* String plantID = plantData.getPlantList().get(position).getId();
        Log.i(TAG,"Selected Plant ID: "+plantID+" Name: "+plantData.getPlantList().get(position).getPlant());
        for (int i=0;i<updateOrderRequestBodyList.size();i++){
           // saveOrderDetails.get(i).setPlant(plantID);
            updateOrderRequestBodyList.get(i).setPlant(plantID);
        }*/
        //Log.i(TAG,"Plant Selected Details: "+new Gson().toJson(updateOrderRequestBodyList));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void executePlantUpdate(){
        updateOrderRequestBodyListWithPlantId = new ArrayList<>();

        for (UpdateOrderRequestBody updateData:updateOrderRequestBodyList){
            UpdateOrderRequestBody data=new UpdateOrderRequestBody(
                    updateData.getTxID(),
                    updateData.getProduct_id(),
                    updateData.getProduct_name(),
                    updateData.getProduct_type(),
                    updateData.getQuantities(),
                    updateData.getClient_id(),
                    updateData.getTaker_id(),
                    updateData.getDelevery_date(),
                    plantId,
                    updateData.getOrdered_date(),
                    updateData.getOrder_type());
            updateOrderRequestBodyListWithPlantId.add(data);
        }
        Log.i(TAG,"Plant Selected Details: "+new Gson().toJson(updateOrderRequestBodyList));
        Log.i(TAG,"Plant Selected Details after plant update: "+new Gson().toJson(updateOrderRequestBodyListWithPlantId));
    }
}
