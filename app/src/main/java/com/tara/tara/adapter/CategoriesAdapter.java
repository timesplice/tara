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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tara.tara.Login;
import com.tara.tara.R;
import com.tara.tara.model.CategoriesModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by M1032467 on 2/17/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<CategoriesModel> data;

    public CategoriesAdapter(List<CategoriesModel> data) {
        this.data = data;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView categoryTitle;

        CategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.category_image);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public CategoriesAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_food_category, parent, false);

        CategoryViewHolder myViewHolder = new CategoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.CategoryViewHolder holder, int position) {
        final ImageView imageView = holder.imageView;
        TextView categoryTitle = holder.categoryTitle;

        categoryTitle.setText(data.get(position).getCategoryName());
        final Context context = imageView.getContext();

        FirebaseStorage.getInstance().getReference().child(data.get(position).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
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
