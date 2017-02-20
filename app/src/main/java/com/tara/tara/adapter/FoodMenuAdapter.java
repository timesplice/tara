package com.tara.tara.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tara.tara.R;
import com.tara.tara.model.CategoriesModel;
import com.tara.tara.model.FoodMenuModel;

import java.util.List;

/**
 * Created by M1032467 on 2/17/2017.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.MenuViewHolder> {

    private List<FoodMenuModel> data;

    public FoodMenuAdapter(List<FoodMenuModel> data, String hotelId, String tableId) {
        this.data = data;
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
        ImageView imageView = holder.imageView;
        TextView foodTitle = holder.foodTitle;
        TextView foodDesc = holder.foodDesc;
        TextView foodPrice = holder.foodPrice;

        foodTitle.setText(data.get(position).getName());
        foodDesc.setText(data.get(position).getDesc());
        foodPrice.setText("" + data.get(position).getPrice());
        Context context = imageView.getContext();
        Uri uri = Uri.parse(data.get(position).getImageUrl());
        Picasso.with(context)
                .load(uri)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
