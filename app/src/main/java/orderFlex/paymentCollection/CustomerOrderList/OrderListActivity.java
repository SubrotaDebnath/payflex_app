package orderFlex.paymentCollection.CustomerOrderList;

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

import orderFlex.paymentCollection.Model.APICallings.PullCustomerOrderList;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListRequest;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

public class OrderListActivity extends AppCompatActivity implements PullCustomerOrderList.OrderListListener{

    private SharedPrefManager prefManager;
    private Helper helper;
    private String TAG="OrderListActivity";
    private AdapterOrderForm adapter;
    private TextView totalBill,clientCode,name,presenterName,phoneNo,address,listTitle;
    private View containerView;
    private LinearLayout orderCodeView,orderTakeSegment;
    private TextView orderDate;
    private RecyclerView bookedOrderList,takeCustomerOrderList;
    private TextView warningText;
    //adapter objects
    private RecyclerView.LayoutManager layoutManager;
    private AdapterListOfOrder adapterListOfOrder;
    private AdapterOrderForm adapterOrderForm;

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

        //default operations
        updateProfile();
        OperationOrderPull();
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
        orderDate.setText(helper.getDate());
        listTitle.setText("Order List");
    }
    private void OperationOrderPull(){
        CustomerOrderListRequest request=new CustomerOrderListRequest(prefManager.getClientId(),helper.getDate(),helper.getDate());
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
                OperationOrderPull();
                //updateOrder.setVisibility(View.GONE);
                break;
            case R.id.logout:
                prefManager.setLoggedInFlag(false);
                Intent intent=new Intent(OrderListActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
                break;
            case R.id.addOrder:
                addNewOrderFormCall();
                break;
            case R.id.userProfile:
                break;
        }
        return true;
    }

    private void addNewOrderFormCall(){
        warningText.setVisibility(View.GONE);
        orderTakeSegment.setVisibility(View.VISIBLE);
        bookedOrderList.setVisibility(View.GONE);
        listTitle.setText("Create new order");
    }

    @Override
    public void onCustomerOrderListResponse(CustomerOrderListResponse response, int code) {
        Log.i(TAG,"Order List Response Code: "+code+" Array size: "+response.getOrderDetails().size());
        if (response.getOrderDetails().size()>0){
            warningText.setVisibility(View.GONE);
            orderTakeSegment.setVisibility(View.GONE);
            bookedOrderList.setVisibility(View.VISIBLE);
            listTitle.setText("Order List");

            adapterListOfOrder=new AdapterListOfOrder(this,response.getOrderDetails());
            layoutManager = new LinearLayoutManager(this);
            bookedOrderList.setLayoutManager(layoutManager);
            bookedOrderList.setAdapter(adapterListOfOrder);

        }else {
            listTitle.setText("Order List");
            warningText.setVisibility(View.VISIBLE);
            bookedOrderList.setVisibility(View.GONE);
            orderTakeSegment.setVisibility(View.GONE);
        }
    }
}