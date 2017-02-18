package com.tara.tara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tara.tara.adapter.CategoriesAdapter;
import com.tara.tara.model.CategoriesModel;

import java.util.List;

public class FoodCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;
    private List<CategoriesModel> categories;
    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);

        recyclerView = (RecyclerView) findViewById(R.id.categories);
        recyclerView.setHasFixedSize(true);

        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

        categoriesAdapter = new CategoriesAdapter(categoriesModels);
        recyclerView.setAdapter(categoriesAdapter);
    }
}
