package orderFlex.paymentCollection.Activityes.OrderDetailsActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentListResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;

public class AdapterPaymentList extends RecyclerView.Adapter<AdapterPaymentList.Holder> implements Picasso.Listener{
    private Context context;
    private List<PaymentListResponse.PaymentList> list;
    private String TAG="AdapterPaymentList";
    private BillPaymentRequestBody paymentData;
    private Helper helper;
    private View alartView;
    private boolean editFlag;

    public AdapterPaymentList(Context context, List<PaymentListResponse.PaymentList> list,boolean editFlag, View alartView) {
        this.context = context;
        this.list = list;
        this.alartView=alartView;
        this.editFlag=editFlag;
        helper=new Helper(context);
        paymentData=new BillPaymentRequestBody();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.row_payments, parent,false);
        return new AdapterPaymentList.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.index.setText(String.valueOf(position+1)+".");
        holder.payedAmount.setText(list.get(position).getAmount());
        holder.paymentDate.setText(helper.dateParchYMDInEnglish(list.get(position).getPaymentDateTime()));
        holder.referenceNo.setText(list.get(position).getReferenceNo());
        holder.bankName.setText(list.get(position).getBankName());
        holder.methodName.setText(list.get(position).getMethodeName());
        if (list.get(position).getPaymentModeId().equals("12") || list.get(position).getPaymentModeId().equals("12")){
            holder.refImgView.setVisibility(View.GONE);
        }else {
            holder.refImgView.setVisibility(View.VISIBLE);
            if (list.get(position).getImage_url()!=null && !list.get(position).getImage_url().equals("")){
                Picasso.get()
                        .load(list.get(position).getImage_url())
//                    .placeholder(R.drawable.filter_loader)
                        .placeholder(R.drawable.loading_wh)
//                        .resize(75, 75)
                        .priority(Picasso.Priority.HIGH)
//                        .centerCrop()
                        .into(holder.refImgView);
                Log.i(TAG,"Image URL: "+list.get(position).getImage_url());
            }else {
                Log.i(TAG,"No Image found!");
            }
        }

        holder.paymentViewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getIsEditable()==1 && editFlag ){
                    paymentData.setSubmittedDateTime(list.get(position).getPaymentDateTime());
                    paymentData.setAmount(list.get(position).getAmount());
                    paymentData.setFinancial_institution_id(list.get(position).getFinancialInstitutionId());
                    paymentData.setOrderCode(list.get(position).getOrderCode());
                    paymentData.setPaymentModeId(list.get(position).getPaymentModeId());
                    paymentData.setReferenceNo(list.get(position).getReferenceNo());
                    paymentData.setTrxid(list.get(position).getTrxid());

                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("payment_data",paymentData);
                    intent.putExtra("img_url",list.get(position).getImage_url());
                    intent.putExtra("bank_name",list.get(position).getBankName());
                    intent.putExtra("method_name",list.get(position).getMethodeName());
                    intent.putExtra("payment_id",list.get(position).getId());
                    context.startActivity(intent);
                }else {
                    helper.showSnakBar(alartView,"This payment is locked!");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
        Log.i(TAG,"Image Loading Failed!");
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView methodName,bankName,referenceNo,paymentDate,payedAmount,index;
        ImageView refImgView;
        CardView paymentViewCard;
        RelativeLayout pay_image_container;
        public Holder(@NonNull View itemView) {
            super(itemView);
            methodName=itemView.findViewById(R.id.methodName);
            bankName=itemView.findViewById(R.id.bankName);
            referenceNo=itemView.findViewById(R.id.referenceNo);
            paymentDate=itemView.findViewById(R.id.paymentDate);
            payedAmount=itemView.findViewById(R.id.payedAmount);
            index=itemView.findViewById(R.id.index);
            refImgView=itemView.findViewById(R.id.refImgView);
            paymentViewCard =itemView.findViewById(R.id.paymentViewCrad);
            pay_image_container=itemView.findViewById(R.id.pay_image_container);
        }
    }
}
