package orderFlex.paymentCollection.CustomerOrderList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.OrderDetailsActivity.OrderDetailsActivity;
import orderFlex.paymentCollection.Model.TodayOrder.CustomerOrderListResponse;
import orderFlex.paymentCollection.R;

public class AdapterListOfOrder extends RecyclerView.Adapter<AdapterListOfOrder.ViewHolder>{
    private Context context;
    private List<CustomerOrderListResponse.Order_list> list=new ArrayList<>();

    public AdapterListOfOrder(Context context, List<CustomerOrderListResponse.Order_list> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.booked_order_row, parent,false);
        return new AdapterListOfOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.booked_orderCode.setText(list.get(position).getOrderCode());
        holder.booked_date.setText(list.get(position).getDeliveryDate());
        holder.booked_amount.setText(list.get(position).getTotal_costs());
        holder.bookedRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("booked_code",list.get(position).getOrderCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView booked_orderCode,booked_date,booked_amount;
        CardView bookedRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            booked_orderCode=itemView.findViewById(R.id.booked_orderCode);
            booked_date=itemView.findViewById(R.id.booked_date);
            booked_amount=itemView.findViewById(R.id.booked_amount);
            bookedRow=itemView.findViewById(R.id.bookedRow);
        }
    }
}
