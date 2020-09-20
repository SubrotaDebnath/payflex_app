package orderFlex.paymentCollection.OrderDetailsActivity;

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

import orderFlex.paymentCollection.Model.OrderDetailDataSet.TodayOrderDetailsByDataResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;

public class AdapterOrderedProductList extends RecyclerView.Adapter<AdapterOrderedProductList.ViewHolder>{
    private Context context;
    private List<TodayOrderDetailsByDataResponse.OrderDetail> list;
    private UpdateTotalBill updateTotalBill;
    private float totalBills = 0;
    private String TAG="AdapterOrderList";
    private int counter=0;
    private boolean change=false;
    private Helper helper;
    private boolean isIndented=false;

    public AdapterOrderedProductList(Context context, List<TodayOrderDetailsByDataResponse.OrderDetail> list,boolean isIndented) {
        this.context = context;
        this.list = list;
        this.isIndented=isIndented;
        updateTotalBill= (UpdateTotalBill) context;
        helper=new Helper(context);
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
        //check is order can be updated
        if (helper.isEditableOrderOrPayment(helper.getDateInEnglish(),list.get(position).getDeliveryDate())){
            if (isIndented){
                holder.quantity.setEnabled(false);
            }else {
                holder.quantity.setEnabled(true);
            }
        }else {
            holder.quantity.setEnabled(false);
        }
        holder.quantity.setText(list.get(position).getQuantityes());

        //price calculation
        float orderedPrice=(Float.valueOf(list.get(position).getPWholesalePrice()))*(Float.valueOf(list.get(position).getQuantityes()));
        totalBills=totalBills+orderedPrice;
        //Log.i(TAG,"Cum.Bill: "+totalBills);
        holder.total.setText(String.valueOf(orderedPrice));
        final int index=position;
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                counter++;
                if (!holder.quantity.getText().toString().isEmpty()){
                    list.get(index).setQuantityes(holder.quantity.getText().toString());
                }else {
                    list.get(index).setQuantityes("0");
                }

                if (counter==2){
                    Log.i(TAG,"Changed to: ");
                    Log.i(TAG,"Index: "+index);
                    Log.i(TAG,"Quantity: "+list.get(index).getQuantityes()+" Price: "+list.get(index).getPWholesalePrice());
                    change=true;
                    billCalculation(list);
                    //notifyDataSetChanged();
                    float orderedPrice=
                            (Float.valueOf(list.get(index).getQuantityes()))*
                                    (Float.valueOf(list.get(index).getPWholesalePrice()));
                    holder.total.setText(String.valueOf(orderedPrice));
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
        public void billUpdate(List<TodayOrderDetailsByDataResponse.OrderDetail> list, float totalTaka, boolean change);
    }

    private void billCalculation(List<TodayOrderDetailsByDataResponse.OrderDetail> list){
        totalBills=0;
        for (TodayOrderDetailsByDataResponse.OrderDetail details:list) {
            float orderedPrice=(Float.valueOf(details.getPWholesalePrice()))*(Float.valueOf(details.getQuantityes()));
            totalBills=totalBills+orderedPrice;
            Log.i(TAG,"Order: "+details.getQuantityes()+" Rate: "+details.getPWholesalePrice()+" Qum. Bill: "+totalBills);
        }
        updateTotalBill.billUpdate(list,totalBills,change);
    }
}
