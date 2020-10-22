package orderFlex.paymentCollection.Activityes.APIDebugLog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.List;

import orderFlex.paymentCollection.Model.APILog.APILogData;
import orderFlex.paymentCollection.Model.DataBase.DatabaseOperation;
import orderFlex.paymentCollection.R;

public class DebugLogs extends AppCompatActivity {

    private RecyclerView debugLog;
    private DatabaseOperation dbOperation;
    //adapter objects
    private RecyclerView.LayoutManager layoutManager;
    private AdapterDebugLog adapterDebugLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_logs);
        debugLog=findViewById(R.id.debugLog);
        dbOperation=new DatabaseOperation(this);
        List<APILogData> logData=dbOperation.getAPILogData();
        Collections.reverse(logData);
        adapterDebugLog=new AdapterDebugLog(this,logData);
        layoutManager = new LinearLayoutManager(this);
        debugLog.setLayoutManager(layoutManager);
        debugLog.setAdapter(adapterDebugLog);
    }

    public void deleteAllLogs(View view) {
        dbOperation.deleteAPILogs();
        List<APILogData> logData=dbOperation.getAPILogData();
        adapterDebugLog=new AdapterDebugLog(this,logData);
        layoutManager = new LinearLayoutManager(this);
        debugLog.setLayoutManager(layoutManager);
        debugLog.setAdapter(adapterDebugLog);
    }
}