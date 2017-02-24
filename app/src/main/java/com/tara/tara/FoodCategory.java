package com.tara.tara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tara.tara.adapter.CategoriesAdapter;
import com.tara.tara.model.CategoriesModel;
import com.tara.tara.model.FoodMenuModel;
import com.tara.tara.model.ScanModel;
import com.tara.tara.util.ScanPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;

    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModels = new ArrayList<>();
    private String hotelId, tableId;
    private HashMap<String, ArrayList<FoodMenuModel>> foodMenu;
    private ScanModel scanModel;
    private ScanPreference scanPreference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        foodMenu = new HashMap<String, ArrayList<FoodMenuModel>>();

        setTitle("Categories");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        scanPreference = new ScanPreference(this);
        scanModel = scanPreference.getScanDetails();

        hotelId = scanModel.getHotelId();
        tableId = scanModel.getTableId();

       // getFacebookProfileImage();
        getMenuFromFirebase();
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        recyclerView.setHasFixedSize(true);

        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

        categoriesAdapter = new CategoriesAdapter(categoriesModels, foodMenu, hotelId, tableId);
        recyclerView.setAdapter(categoriesAdapter);
    }

    private void getFacebookProfileImage(){
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                System.out.println("FACEBOOK DATA::"+data);
                                if (data.has("cover")) {
                                    String profilePicUrl = data.getJSONObject("cover").getString("source");
                                    //profilePicUrl
                                    categoriesModels.add(new CategoriesModel("profileImageId",
                                            "My Interest", profilePicUrl));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        getMenuFromFirebase();
                    }
                }).executeAsync();
    }

    private void getMenuFromFirebase() {
        Login.fDatabase.getReference("menus/" + hotelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getKey() != null) {
                    System.out.println("DATASNAPSHOT:" + dataSnapshot.getKey());
                    Set<String> categories = new HashSet<>();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        FoodMenuModel food = childSnapshot.getValue(FoodMenuModel.class);

                        System.out.println("ARRAY:" + childSnapshot.getKey());
                        String category = food.getCategory().toLowerCase().replace(" ", "") + ".jpg";
                        if (!categories.contains(category)) {
                            ArrayList<FoodMenuModel> newCategoryFoods = new ArrayList<>();
                            newCategoryFoods.add(food);
                            foodMenu.put(food.getCategory(), newCategoryFoods);

                            categoriesModels.add(new CategoriesModel(childSnapshot.getKey(),
                                    food.getCategory(),
                                    hotelId + "/category/" + category));
                            categories.add(category);
                        } else {
                            foodMenu.get(food.getCategory()).add(food);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    categoriesAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("DATASNAPSHOT: null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_signin) {
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            intent.putExtra("hotelId", hotelId);
            intent.putExtra("tableId", tableId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
