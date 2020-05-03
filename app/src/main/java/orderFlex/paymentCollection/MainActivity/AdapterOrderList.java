package orderFlex.paymentCollection.MainActivity;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import orderFlex.paymentCollection.Model.TodayOrder.TodayOrderResponse;
import orderFlex.paymentCollection.R;

public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.ViewHolder>{
    private Context context;
    private List<TodayOrderResponse.OrderDetail> list;
    private UpdateTotalBill updateTotalBill;
    private float totalBills = 0;
    private String TAG="AdapterOrderList";
    private int counter=0;
    private boolean change=false;

    public AdapterOrderList(Context context, List<TodayOrderResponse.OrderDetail> list) {
        this.context = context;
        this.list = list;
        updateTotalBill= (UpdateTotalBill) context;
        billCalculation(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.row_order_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.index.setText(String.valueOf(position+1));
        if (list.get(position).getPType().equals("1")){
            holder.productName.setText(list.get(position).getPName() + " New Cylinder");
        } else if (list.get(position).getPType().equals("2")){
            holder.productName.setText(list.get(position).getPName() + " Refill Cylinder");
        }
        holder.unitePrice.setText(list.get(position).getPWholesalePrice());
        holder.quantity.setText(list.get(position).getQuantityes());
        float orderedPrice=(Float.valueOf(list.get(position).getPWholesalePrice()))*(Float.valueOf(list.get(position).getQuantityes()));
        totalBills=totalBills+orderedPrice;
        //Log.i(TAG,"Cum.Bill: "+totalBills);
        holder.total.setText(String.valueOf(orderedPrice));
        final int index=position;
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                counter++;
                list.get(index).setQuantityes(holder.quantity.getText().toString());
                if (counter==2){
                    Log.i(TAG,"Changed to: ");
                    Log.i(TAG,"Index: "+index);
                    Log.i(TAG,"Quantity: "+list.get(index).getQuantityes()+" Price: "+list.get(index).getPWholesalePrice());
                    change=true;
                    billCalculation(list);
                    notifyDataSetChanged();
                    counter=0;
                }
                //billCalculation(list);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView index,productName,unitePrice,total;
        EditText quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            index=itemView.findViewById(R.id.index);
            productName=itemView.findViewById(R.id.productName);
            unitePrice=itemView.findViewById(R.id.unitePrice);
            quantity=itemView.findViewById(R.id.quantity);
            total=itemView.findViewById(R.id.total);
        }
    }
    public interface UpdateTotalBill{
        public void billUpdate(List<TodayOrderResponse.OrderDetail> list,float totalTaka, boolean change);
    }
    private void billCalculation(List<TodayOrderResponse.OrderDetail> list){
        totalBills=0;
        for (TodayOrderResponse.OrderDetail details:list) {
            float orderedPrice=(Float.valueOf(details.getPWholesalePrice()))*(Float.valueOf(details.getQuantityes()));
            totalBills=totalBills+orderedPrice;
            Log.i(TAG,"Order: "+details.getQuantityes()+" Rate: "+details.getPWholesalePrice()+" Qum. Bill: "+totalBills);
        }
        updateTotalBill.billUpdate(list,totalBills,change);
    }
}
