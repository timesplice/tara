package com.tara.tara.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tara.tara.Cart;
import com.tara.tara.R;
import com.tara.tara.model.FoodMenuModel;

import java.util.List;

/**
 * Created by M1032467 on 11/8/2016.
 */

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartViewHolder> {

    List<FoodMenuModel> data;
    Context context;

    public CartItemsAdapter(List<FoodMenuModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodDesc;
        Button remove;
        LinearLayout cartItemLayout;

        CartViewHolder(View itemView) {
            super(itemView);

            foodImage = (ImageView) itemView.findViewById(R.id.productimage);
            foodName = (TextView) itemView.findViewById(R.id.producttitle);
            foodPrice = (TextView) itemView.findViewById(R.id.productprice);
            foodDesc = (TextView) itemView.findViewById(R.id.productdesc);
            remove = (Button) itemView.findViewById(R.id.remove_item);
            cartItemLayout = (LinearLayout) itemView.findViewById(R.id.cart_item_layout);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    FoodMenuModel foodItem = data.get(position);
                    Cart cart = (Cart)getRequiredActivity(v);
                    removeFromAdapter(position);
                    removeFromView(cart,foodItem);
                }
            });

            cartItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                }
            });
        }

        private void removeFromView(final Cart cart, final FoodMenuModel cartItem) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    cart.removeItem(cartItem);
                }
            }, 500);
        }

        private void removeFromAdapter(int position) {
            data.remove(position);
            notifyItemRemoved(position);
        }

        private Activity getRequiredActivity(View req_view) {
            Context context = req_view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
            return null;
        }
    }

    @Override
    public CartItemsAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_cart, parent, false);

        CartItemsAdapter.CartViewHolder myViewHolder = new CartItemsAdapter.CartViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CartItemsAdapter.CartViewHolder holder, int position) {
        TextView productTitle = holder.foodName;
        TextView productAmount = holder.foodPrice;
        TextView foodDescription = holder.foodDesc;
        final ImageView foodImage = holder.foodImage;

        productTitle.setText(data.get(position).getName());
        productAmount.setText(data.get(position).getPrice() + " ₹");
        foodDescription.setText(data.get(position).getDesc());

        FirebaseStorage.getInstance().getReference().child(data.get(position).getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .into(foodImage);
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
