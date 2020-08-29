package orderFlex.paymentCollection.CustomerOrderList;

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

import orderFlex.paymentCollection.Model.PaymentAndBillData.ProductListResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.SaveOrderRequest;
import orderFlex.paymentCollection.R;

public class AdapterOrderForm extends RecyclerView.Adapter<AdapterOrderForm.ViewHolder>{
    private Context context;
    private List<SaveOrderRequest> list;
    List<ProductListResponse.ProductList> productLists;
    private UpdateTotalBill updateTotalBill;
    private double totalBills = 0;
    private String TAG="AdapterOrderForm";
    private int counter=0;
    private boolean change=false;

    public AdapterOrderForm(Context context, List<SaveOrderRequest> list, List<ProductListResponse.ProductList> productLists) {
        this.context = context;
        this.list = list;
        this.productLists=productLists;
        Log.i(TAG,"Products Size: "+list.size());
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
        if (list.get(position).getProduct_type().equals("1")){
            holder.productName.setText(list.get(position).getProduct_name() + " New Cylinder");
        } else if (list.get(position).getProduct_type().equals("2")){
            holder.productName.setText(list.get(position).getProduct_name() + " Refill Cylinder");
        }
        holder.unitePrice.setText(productLists.get(position).getPWholesalePrice());
        holder.quantity.setText(list.get(position).getQuantities());
        double orderedPrice=(Double.valueOf(productLists.get(position).getPWholesalePrice()))*(Double.valueOf(list.get(position).getQuantities()));
        totalBills=totalBills+orderedPrice;
        //Log.i(TAG,"Cum.Bill: "+totalBills);
        holder.total.setText(String.valueOf(orderedPrice));
        final int index=position;
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                counter++;
                if (!holder.quantity.getText().toString().isEmpty())
                {
                    list.get(index).setQuantities(holder.quantity.getText().toString());
                }else {
                    list.get(index).setQuantities("0");
                }
                if (counter==2){
                    Log.i(TAG,"Changed to: ");
                    Log.i(TAG,"Index: "+index);
                    Log.i(TAG,"Quantity: "+list.get(index).getQuantities()+" Price: "+productLists.get(index).getPWholesalePrice());
                    change=true;
                    billCalculation(list);
//                    notifyDataSetChanged();
                    double orderedPrice=
                            (Double.valueOf(list.get(index).getQuantities()))*
                                    (Double.valueOf(productLists.get(index).getPWholesalePrice()));
                    holder.total.setText(String.valueOf(orderedPrice));
                    counter=0;
                }
                billCalculation(list);
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
        public void saveBillUpdate(List<SaveOrderRequest> list, double totalTaka, boolean change);
    }
    private void billCalculation(List<SaveOrderRequest> list){
        totalBills=0;
        int count=0;
        for (SaveOrderRequest details:list) {
            double orderedPrice=(Double.valueOf(productLists.get(count).getPWholesalePrice()))*(Double.valueOf(details.getQuantities()));
            totalBills=totalBills+orderedPrice;
            Log.i(TAG,"Order: "+details.getQuantities()+" Rate: "+productLists.get(count).getPWholesalePrice()+" Qum. Bill: "+totalBills);
            count++;
        }
        updateTotalBill.saveBillUpdate(list,totalBills,change);
    }
}
