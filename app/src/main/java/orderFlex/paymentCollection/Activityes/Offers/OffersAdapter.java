package orderFlex.paymentCollection.Activityes.Offers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Model.OffersListDataClass.OffersListPojo;
import orderFlex.paymentCollection.R;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder>{
    private Context context;
    private List<OffersListPojo.Datum> datumList = new ArrayList<>();

    public OffersAdapter(Context context, List<OffersListPojo.Datum> datumList) {
        this.context = context;
        this.datumList = datumList;

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offer_list_row_view, parent, false);
        return new OffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.offerTitle.setText(datumList.get(position).getOfferTitle());
            holder.offerDescription.setText(datumList.get(position).getOfferDiscription());
        if (datumList.get(position).getImageUrl()!=null && !datumList.get(position).getImageUrl().equals("")){
            Picasso.get()
                    .load(datumList.get(position).getImageUrl())
//                    .placeholder(R.drawable.filter_loader)
                    .placeholder(R.drawable.loading_wh)
//                        .resize(75, 75)
                    .priority(Picasso.Priority.HIGH)
//                        .centerCrop()
                    .into(holder.offerImage);
//            Log.i(TAG,"Image URL: "+datumList.get(position).getImageUrl());
        }else {
//            Log.i(TAG,"No Image found!");
        }

        holder.offerAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.offerDenied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView offerTitle, offerDescription;
        ImageView offerImage, offerAccepted, offerDenied;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerTitle = itemView.findViewById(R.id.offerTitle);
            offerDescription = itemView.findViewById(R.id.offerDescription);
            offerImage = itemView.findViewById(R.id.offerImage);
            offerAccepted = itemView.findViewById(R.id.offerAccepted);
            offerDenied = itemView.findViewById(R.id.offerDenied);
        }
    }
}
