package orderFlex.paymentCollection.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.R;

public class AdapterPaymentList extends RecyclerView.Adapter<AdapterPaymentList.Holder>{
    private Context context;
    private List<PaymentListResponse.PaymentList> list;

    public AdapterPaymentList(Context context, List<PaymentListResponse.PaymentList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.row_payments, parent,false);
        return new AdapterPaymentList.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.index.setText(String.valueOf(position+1)+".");
        holder.payedAmount.setText(list.get(position).getAmount());
        holder.paymentDate.setText(list.get(position).getPaymentDateTime());
        holder.referenceNo.setText(list.get(position).getReferenceNo());
        holder.bankName.setText(list.get(position).getBankName());
        holder.methodName.setText(list.get(position).getMethodeName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView methodName,bankName,referenceNo,paymentDate,payedAmount,index;
        public Holder(@NonNull View itemView) {
            super(itemView);
            methodName=itemView.findViewById(R.id.methodName);
            bankName=itemView.findViewById(R.id.bankName);
            referenceNo=itemView.findViewById(R.id.referenceNo);
            paymentDate=itemView.findViewById(R.id.paymentDate);
            payedAmount=itemView.findViewById(R.id.payedAmount);
            index=itemView.findViewById(R.id.index);
        }
    }
}
