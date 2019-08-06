package com.psalms34.travelmatics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.dealViewHolder> {
    ArrayList<traveldeal> deals;
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    private ChildEventListener datachildlistener;
    private ImageView imageDeal;
    public DealAdapter(){
        //Firebaseutil.openfbReference("traveldeal");
        fbDatabase = Firebaseutil.fbDatabase;
        dbRef = Firebaseutil.dbRef;
        this.deals=Firebaseutil.deals;
        datachildlistener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                traveldeal td = dataSnapshot.getValue(traveldeal.class);
                Log.d("Deal:",td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRef.addChildEventListener(datachildlistener);


    }
    @NonNull
    @Override
    public dealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rvrow,parent , false);
        return new dealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull dealViewHolder holder, int position) {
       traveldeal deal = deals.get(position);
       holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class dealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        public dealViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDescription = (TextView) itemView.findViewById(R.id.description);
            tvPrice = (TextView) itemView.findViewById(R.id.price);
            imageDeal = (ImageView) itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }
        public void bind(traveldeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageurl());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            traveldeal selectedDeal = deals.get(position);
            Intent intent = new Intent(v.getContext(), AdminActivity.class);
            intent.putExtra("Deal", selectedDeal);
            v.getContext().startActivity(intent);
        }
        private void showImage(String url) {
            if (url != null && url.isEmpty()==false) {
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(width, 200)
                        .centerCrop()
                        .into(imageDeal);
            }
        }
    }
}
