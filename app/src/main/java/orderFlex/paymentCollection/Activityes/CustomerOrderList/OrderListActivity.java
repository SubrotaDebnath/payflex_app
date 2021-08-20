package orderFlex.paymentCollection.Activityes.CustomerOrderList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import orderFlex.paymentCollection.Activityes.APIDebugLog.DebugLogs;
import orderFlex.paymentCollection.Activityes.Offers.Offers;
import orderFlex.paymentCollection.Activityes.Profile.ProfileActivity;
import orderFlex.paymentCollection.Model.APICallings.GetProductList;
import orderFlex.paymentCollection.Model.APICallings.PullCustomerOrderList;
import orderFlex.paymentCollection.Model.APICallings.PullPlantList;
import orderFlex.paymentCollection.Model.APICallings.SaveOrderHandler;
import orderFlex.paymentCollection.Model.APICallings.UpdatePassword;
import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataRequest;
import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.SaveOrderData.PlantListResponse;
import orderFlex.paymentCollection.Model.SaveOrderData.SaveOrderDetails;
import orderFlex.paymentCollection.Model.SaveOrderData.SaveOrderRequestBody;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.Model.TodayOrder.UpdateOrderResponse;
import orderFlex.paymentCollection.Model.UserData.PassChangeReqBody;
import orderFlex.paymentCollection.Model.UserData.PassChangeResponseBody;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.Activityes.login.UserLogin;

public class OrderListActivity extends BaseActivity
        implements
        PullCustomerOrderList.OrderListListener,
        GetProductList.GetProductListListener,
        AdapterOrderForm.UpdateTotalBill,
        SaveOrderHandler.SaveOrderListener,
        UpdatePassword.PassChangeListener,
        PullPlantList.PlantListListener,
        AdapterView.OnItemSelectedListener
{
    private SharedPrefManager prefManager;
    private Helper helper;
    private Context context=this;
    private String TAG="OrderListActivity";
    private AdapterOrderForm adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address,listTitle,totalTakenBill;
    private View containerView;
    private LinearLayout orderTakeSegment;
    private CardView orderCodeView,deliveryLocation;
    private TextView orderDate;
    private RecyclerView bookedOrderList,takeCustomerOrderList;
    private TextView warningText;
    //adapter objects
    private RecyclerView.LayoutManager layoutManager;
    private AdapterListOfOrder adapterListOfOrder;
    private AdapterOrderForm adapterOrderForm;
    private List<SaveOrderDetails> saveOrderDetails=new ArrayList<>();
    //Order Save/Cancel
    private CardView cancelOrder,saveOrder,addNewOrder;
    private SaveOrderHandler saveOrderHandler;
    private int orderCount=1;
    private PlantListResponse plantData;
    private ImageView proImg,pickDate;
    Locale english=null;

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
        deliveryLocation=findViewById(R.id.deliveryLocation);
        //new order segment
        totalTakenBill=findViewById(R.id.totalTakenBill);
        pickDate=findViewById(R.id.pickDate);

        //default operations
        updateProfile();

        //open order form
        addNewOrder=findViewById(R.id.addNewOrder);
        try {
            Intent intent=getIntent();
            String add_order=" ";
            add_order = intent.getStringExtra("add_order");
            Log.i(TAG,"Intent: "+add_order);
            if (add_order!=null){
                if (add_order.equals("take_order")){
                    addNewOrderFormCall();
                }
            }else {
                operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
            }
        }catch (Exception e){
            Log.i(TAG,"Intent Exception"+e.getMessage());
        }
        addNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setDate=orderDate.getText().toString();
                String currentDate=helper.getDateInEnglish();
                if (helper.isDate1LessDate2(setDate,currentDate)){
                    helper.showSnakBar(containerView,"Sorry, You can't place order at previous date");
                }else if (helper.isDate1GreaterDate2(setDate,currentDate)){
                    helper.showSnakBar(containerView,"Sorry, You can't place order at advance date");
                } else if (helper.isDate1EqualDate2(setDate, currentDate)) {
                    addNewOrderFormCall();
                }
            }
        });
        //save order
        saveOrder=findViewById(R.id.save_action);
        saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlant();
            }
        });
        //cancel
        cancelOrder=findViewById(R.id.cancel_action);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderAccordingToDate(orderDate);
            }
        });
    }

    private void selectPlant(){
        List<String> plantNameList=new ArrayList<>();
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
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
                executeSaveRequest();
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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
        proImg=findViewById(R.id.proImg);

        clientCode.setText(prefManager.getClientCode());
        name.setText(prefManager.getClientName());
        presenterName.setText(prefManager.getPresenterName());
        phoneNo.setText(prefManager.getClientContactNumber());
        address.setText(prefManager.getClientAddress());

        orderCodeView.setVisibility(View.GONE);
        deliveryLocation.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.GONE);

        orderDate.setText(helper.getDateInEnglish());
        listTitle.setText("Order List");
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

    private void executeSaveRequest(){
        double totalBill=Double.parseDouble(totalTakenBill.getText().toString());
        if (helper.isInternetAvailable()){
            if (totalBill>0.0){
                SaveOrderRequestBody requestBody=new SaveOrderRequestBody(
                        helper.getDateInEnglish(),
                        saveOrderDetails.get(0).getDelevery_date(),
                        prefManager.getClientCode()+"-"+helper.getShortDateInEnglish()+"-"+String.valueOf(orderCount+1),
                        helper.makeUniqueID(),
                        saveOrderDetails.get(0).getTaker_id(),
                        saveOrderDetails.get(0).getClient_id(),
                        saveOrderDetails
                );
                Gson gson=new Gson();
                String response=gson.toJson(saveOrderDetails);
                Log.i(TAG,"Response Body: "+response);
                saveOrderHandler=new SaveOrderHandler(context);
                //need to change in API call for new order code
                saveOrderHandler.pushSaveOrder(prefManager.getUsername(),prefManager.getUserPassword(), requestBody);
            }else {
                helper.showSnakBar(containerView,"You did not give any quantity of any product!");
            }
        }else {
            helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
        }
    }

    private void operationOrderPull(String startDate,String endDate){
        warningText.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.GONE);
        bookedOrderList.setVisibility(View.GONE);
        orderDate.setText(startDate);
        listTitle.setText("Order List");
        addNewOrder.setVisibility(View.VISIBLE);

        CustomerOrderListRequest request=new CustomerOrderListRequest(prefManager.getClientId(),startDate,endDate);
        if (helper.isInternetAvailable()){
            helper.showSnakBar(containerView,"Refreshing the dashboard...!");
            new PullCustomerOrderList(this).pullCustomerOrderListCall(prefManager.getUsername(),prefManager.getUserPassword(),request);
        }else {
            warningText.setVisibility(View.VISIBLE);
            warningText.setText("No Internet! Please check your internet connection and refresh again");
            helper.showSnakBar(containerView,"No Internet! Please check your internet connection");
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
                operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
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
//            case R.id.calendarData:
//                getOrderAccordingToDate(orderDate);
//                break;
            case R.id.change_password:
                changePassword(this);
                break;
            case R.id.userProfile:
                Intent intent1=new Intent(OrderListActivity.this, ProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.my_offer:
                Intent intent2 = new Intent(OrderListActivity.this, Offers.class);
                intent2.putExtra("id", "orderList");
                startActivity(intent2);
                break;
            /*case R.id.debugLog:
                Intent intent2=new Intent(OrderListActivity.this, DebugLogs.class);
                startActivity(intent2);
                break;*/
        }
        return true;
    }
    public String getOrderAccordingToDate(final TextView setPosition){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            english = Locale.forLanguageTag("en");
        }
        final Calendar calendar;
        int month, year, day;
        final String[] date = {""};
        //calender
        calendar= Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",english);

                        calendar.set(i,i1,i2);
                        date[0] = sdf.format(calendar.getTime());
                        //Log.i(TAG,"At pick date: "+ date[0]);
                        setPosition.setText(date[0]);
                        operationOrderPull(date[0],date[0]);
                    }
                }, year, month, day);

        datePickerDialog.show();
        return date[0];
    }

    private void addNewOrderFormCall(){
        warningText.setVisibility(View.GONE);
        bookedOrderList.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.VISIBLE);
        addNewOrder.setVisibility(View.GONE);
        listTitle.setText("Create new order");
        new GetProductList(this).pullProductListCall(prefManager.getUsername(),prefManager.getUserPassword());
        new PullPlantList(this).plantListCall(prefManager.getUsername(),prefManager.getUserPassword());
    }

//pre booked order list
    @Override
    public void onCustomerOrderListResponse(CustomerOrderListResponse response, int code) {
      //  Log.i(TAG,"Order List Response Code: "+code+" Array size: "+response.getOrderDetails().size());
        if (response!=null){
            if (response.getOrderDetails().size()>0){
                warningText.setVisibility(View.GONE);
                bookedOrderList.setVisibility(View.VISIBLE);
                orderTakeSegment.setVisibility(View.GONE);
                addNewOrder.setVisibility(View.VISIBLE);
                listTitle.setText("Order List");
                orderCount=response.getOrderDetails().size()+1;

                adapterListOfOrder=new AdapterListOfOrder(this,response.getOrderDetails());
                layoutManager = new LinearLayoutManager(this);
                bookedOrderList.setLayoutManager(layoutManager);
                bookedOrderList.setAdapter(adapterListOfOrder);
            }else {
                warningText.setVisibility(View.VISIBLE);
                warningText.setText("You don't have any Order for today. Please book an Order first!");
            }
        }else {
            listTitle.setText("Order List");
            warningText.setVisibility(View.VISIBLE);
            addNewOrder.setVisibility(View.VISIBLE);
            orderTakeSegment.setVisibility(View.GONE);
            bookedOrderList.setVisibility(View.GONE);
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                if (code==202){
                    helper.showSnakBar(containerView,"No order found!");
                }else {
                    warningText.setText("Server not Responding! Please check your internet connection.");
                    helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
                }

            }
//            if (response==null){
//                helper.showSnakBar(containerView,"Server not responding! Please check you internet connection.");
//            }
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
    public void saveBillUpdate(List<SaveOrderDetails> list, double totalTaka, boolean change) {
        saveOrderDetails =list;
        totalTakenBill.setText(String.valueOf(totalTaka));
    }

    @Override
    public void onSaveResponse(UpdateOrderResponse response, int code) {
        if (response!=null && code==202){
            TodayOrderDetailsByDataRequest request=new TodayOrderDetailsByDataRequest(prefManager.getClientId(),helper.getDateInEnglish());
            helper.showSnakBar(containerView,response.getMessage());
            if (helper.isInternetAvailable()){
                operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
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
//        operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
        updateProfile();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        operationOrderPull(helper.getDateInEnglish(),helper.getDateInEnglish());
        updateProfile();
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

    private void changePassword(final Context context){
        final AlertDialog alertDialog2;
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.update_password_view,null);

        CardView cancel_key=customRoot.findViewById(R.id.cancel_key);
        CardView update_key=customRoot.findViewById(R.id.update_key);
        final EditText currentPass= customRoot.findViewById(R.id.current_pass);
        final EditText newPass=customRoot.findViewById(R.id.new_pass);
        final EditText reNewPass=customRoot.findViewById(R.id.re_new_pass);

        builder2.setView(customRoot);
        builder2.setTitle(R.string.menu_pass);
        builder2.setCancelable(false);
        alertDialog2= builder2.create();
        alertDialog2.show();

        cancel_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
        update_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c_pass="",n_pass="",rn_pass="";
                c_pass=currentPass.getText().toString();
                n_pass=newPass.getText().toString();
                rn_pass=reNewPass.getText().toString();
                if (c_pass.isEmpty()||n_pass.isEmpty()||rn_pass.isEmpty()){
                    helper.showSnakBar(containerView,"Some fields are empty!");
                }else {
                    if (n_pass.equals(rn_pass)){
                        if (helper.isInternetAvailable()){
                            PassChangeReqBody reqBody=new PassChangeReqBody(prefManager.getUsername(),c_pass,n_pass,rn_pass);
                            new UpdatePassword(context).updatePassCall(prefManager.getUsername(),prefManager.getUserPassword(),reqBody);
                            alertDialog2.dismiss();
                        }else {
                            helper.showSnakBar(containerView,"No internet! Please check your internet connection!");
                        }
                    }else {
                        helper.showSnakBar(containerView,"New password does not match!");
                    }
                }

            }
        });
    }

    @Override
    public void onPasswordUpdateResponse(PassChangeResponseBody response, int code) {
        if (response!=null && code==202){
            helper.showSnakBar(containerView,response.getMessage());
        }else {
            if (code==401){
                helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
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
        String plantID = plantData.getPlantList().get(position).getId();
        Log.i(TAG,"Selected Plant ID: "+plantID+" Name: "+plantData.getPlantList().get(position).getPlant());
        for (int i=0;i<saveOrderDetails.size();i++){
            saveOrderDetails.get(i).setPlant(plantID);
        }
        Log.i(TAG,"Plant Selected Details: "+new Gson().toJson(saveOrderDetails));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_baseline_info);
        builder.setTitle("");
        builder.setMessage(R.string.application_exit);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.no, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                finish();
            }
        });
        builder.show();
        return;

    }
}