package com.example.lab2firebase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_Distance {

    private Context mContext;
    private FoodsAdapter mFoodsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Distance> dailyOffers, List<String> keys) {
        mContext = context;
        mFoodsAdapter = new FoodsAdapter(dailyOffers, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFoodsAdapter);
    }

    class DailyOfferItemView extends RecyclerView.ViewHolder {
        private TextView mDistance;
        private TextView mRiderName;
        private TextView mListOfFood;
        private TextView mProfile;
        private TextView mComment;
        private ImageView mImgRestaurant;
        private String key;


        public DailyOfferItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.nearest_rider_item, parent, false));
            mDistance = itemView.findViewById(R.id.tvDistance1);
            mRiderName = itemView.findViewById(R.id.tvRestaurantName1);
            mProfile = itemView.findViewById(R.id.tvProfileOfRestaurant);
            mListOfFood = itemView.findViewById(R.id.tvFoods);
            mImgRestaurant = itemView.findViewById(R.id.imgRestaurant1);
            mComment = itemView.findViewById(R.id.tvRestaurantComment1);

            //Open profile of that restaurant we may create a new one
            mProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(mContext, ProfileOfRestaurantActivity.class);
                    //  intent.putExtra("key", key);
                    // mContext.startActivity(intent);
                }
            });
            //Open only list of foods belong to this restaurant
            mListOfFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(mContext, SpecificFoodListActivity.class);
                    //  intent.putExtra("key", key);
                    // mContext.startActivity(intent);
                }
            });

        }

        public void bind(Distance distance, String key) {
            Double distance2=Double.valueOf(distance.getDistance());
            mDistance.setText(String.format("%.2f", distance2)+ " Km");
            DatabaseReference mReferenceRestaurant = FirebaseDatabase.getInstance()
                    .getReference().child("RidersProfile").child(key);
            mReferenceRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RidersProfile ridersProfile = dataSnapshot.getValue(RidersProfile.class);
                    mRiderName.setText(ridersProfile.getName());
                    mComment.setText(ridersProfile.getShortdescription());

                    Picasso.get()
                            .load(ridersProfile.getImageUrl())
                            .placeholder(R.drawable.personal)
                            .fit()
                            .centerCrop()
                            .into(mImgRestaurant);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            this.key = key;
        }
    }

    class FoodsAdapter extends RecyclerView.Adapter<DailyOfferItemView> {
        private List<Distance> mDailyOfferList;
        private List<String> mKeys;

        public FoodsAdapter(List<Distance> mDailyOfferList, List<String> mKeys) {
            this.mDailyOfferList = mDailyOfferList;
            this.mKeys = mKeys;
        }


        @Override
        public DailyOfferItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DailyOfferItemView(parent);
        }

        @Override
        public void onBindViewHolder(DailyOfferItemView holder, int position) {
            holder.bind(mDailyOfferList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mDailyOfferList.size();
        }
    }

}


