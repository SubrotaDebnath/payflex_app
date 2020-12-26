package orderFlex.paymentCollection.Activityes.Offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
    //private List<OffersListPojo> offersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        helper = new Helper(this);
        prefManager = new SharedPrefManager(this);
        new PullOfferList(this).offerListCall(prefManager.getUsername(), prefManager.getUserPassword());
        recyclerView = findViewById(R.id.offers_recyclerView);
        layoutManager = new LinearLayoutManager(this);
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
}