package orderFlex.paymentCollection.Activityes.APIDebugLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import orderFlex.paymentCollection.Model.APILog.APILogData;
import orderFlex.paymentCollection.R;

public class AdapterDebugLog extends RecyclerView.Adapter<AdapterDebugLog.ViewHolder>{
    private Context context;
    private List<APILogData> logData;

    public AdapterDebugLog(Context context, List<APILogData> logData) {
        this.context = context;
        this.logData = logData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.debug_log_row, parent,false);
        return new AdapterDebugLog.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.apiName.setText(logData.get(position).getCallName());
        holder.apiUrl.setText(logData.get(position).getCallURL());
        holder.callTime.setText(logData.get(position).getCallTime());
        holder.requestBody.setText(logData.get(position).getRequestBody());
        holder.responseCode.setText(logData.get(position).getResponseCode());
        holder.responseBody.setText(logData.get(position).getResponseBody());
        holder.exception.setText(logData.get(position).getException());
        holder.responseTime.setText(logData.get(position).getResponseTime());
    }

    @Override
    public int getItemCount() {
        return logData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView apiName,apiUrl,callTime,requestBody,responseCode,responseBody,exception,responseTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            apiName=itemView.findViewById(R.id.apiName);
            apiUrl=itemView.findViewById(R.id.apiURL);
            callTime=itemView.findViewById(R.id.callTime);
            requestBody=itemView.findViewById(R.id.requestBody);
            responseCode=itemView.findViewById(R.id.responseCode);
            responseBody=itemView.findViewById(R.id.responseBody);
            exception=itemView.findViewById(R.id.exception);
            responseTime=itemView.findViewById(R.id.responseTime);
        }
    }
}
