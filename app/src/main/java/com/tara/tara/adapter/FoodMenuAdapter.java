package com.tara.tara.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tara.tara.FoodDescription;
import com.tara.tara.R;
import com.tara.tara.model.CategoriesModel;
import com.tara.tara.model.FoodMenuModel;

import java.util.List;

/**
 * Created by M1032467 on 2/17/2017.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.MenuViewHolder> {

    private List<FoodMenuModel> data;
    private String hotelId, tableId;

    public FoodMenuAdapter(List<FoodMenuModel> data, String hotelId, String tableId) {
        this.data = data;
        this.hotelId = hotelId;
        this.tableId = tableId;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView foodTitle;
        TextView foodDesc;
        TextView foodPrice;

        MenuViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.food_image);
            foodTitle = (TextView) itemView.findViewById(R.id.food_title);
            foodDesc = (TextView) itemView.findViewById(R.id.food_desc);
            foodPrice = (TextView) itemView.findViewById(R.id.food_price);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            FoodMenuModel foodItemModel = data.get(position);
            Intent intent = new Intent(v.getContext(), FoodDescription.class);
            intent.putExtra("foodItemModel", foodItemModel);
            intent.putExtra("hotelId", hotelId);
            intent.putExtra("tableId", tableId);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public FoodMenuAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_food_menu, parent, false);

        MenuViewHolder myViewHolder = new MenuViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(FoodMenuAdapter.MenuViewHolder holder, int position) {
        final ImageView imageView = holder.imageView;
        TextView foodTitle = holder.foodTitle;
        TextView foodDesc = holder.foodDesc;
        TextView foodPrice = holder.foodPrice;

        System.out.println("POSITION:" + position);
        System.out.println("IN ADAPTER:" + data.get(position).getName());

        foodTitle.setText(data.get(position).getName());
        foodDesc.setText(data.get(position).getDesc());
        foodPrice.setText(data.get(position).getPrice() + " ₹");

        final Context context = imageView.getContext();

        FirebaseStorage.getInstance().getReference().child(data.get(position).getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
