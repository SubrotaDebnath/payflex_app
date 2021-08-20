package orderFlex.paymentCollection.Activityes.Offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Activityes.CustomerOrderList.OrderListActivity;
import orderFlex.paymentCollection.Activityes.OrderDetailsActivity.OrderDetailsActivity;
import orderFlex.paymentCollection.Model.APICallings.PullOfferList;
import orderFlex.paymentCollection.Model.OffersListDataClass.OffersListPojo;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class Offers extends AppCompatActivity implements PullOfferList.OfferListListener {
    private static final String TAG = "Offers";
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Helper helper;
    private SharedPrefManager prefManager;
    private LinearLayout homeBTN;
    private String destination;

    //private List<OffersListPojo> offersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destination = extras.getString("id");
        }

        homeBTN = findViewById(R.id.offerToHomeLL);

        helper = new Helper(this);
        prefManager = new SharedPrefManager(this);
        new PullOfferList(this).offerListCall(prefManager.getUsername(), prefManager.getUserPassword());
        recyclerView = findViewById(R.id.offers_recyclerView);
        layoutManager = new LinearLayoutManager(this);

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Offers.this, OrderListActivity.class));
                finish();
                Log.i(TAG, "Home Button Click");
            }
        });
    }

    @Override
    public void onOfferListResponse(OffersListPojo response, int code) {
        Log.i(TAG, "response Code: " + code);
        Gson gson=new Gson();
        String res=gson.toJson(response);
        Log.i(TAG,"Response: "+res);
        if (response!=null && code==202){
            OffersAdapter adapter = new OffersAdapter(this, response.getData());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }else {
            if (code==401){
                //helper.showSnakBar(containerView,"Unauthorized Username or Password!");
            }else {
                //helper.showSnakBar(containerView,"Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (destination.equals("orderList")){
            startActivity(new Intent(Offers.this, OrderListActivity.class));
            finish();
        }else if (destination.equals("orderDetails")){
            startActivity(new Intent(Offers.this, OrderDetailsActivity.class));
            finish();
        }else {
            startActivity(new Intent(Offers.this, OrderListActivity.class));
            finish();
        }


    }
}