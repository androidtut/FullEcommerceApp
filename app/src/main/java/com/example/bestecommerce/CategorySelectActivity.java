package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bestecommerce.Adapters.CategoriesAdapters;
import com.example.bestecommerce.Adapters.ProductAdapters;
import com.example.bestecommerce.databinding.ActivityCategorySelectBinding;
import com.example.bestecommerce.models.CategoriesModels;
import com.example.bestecommerce.models.ProductModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategorySelectActivity extends AppCompatActivity {
ActivityCategorySelectBinding binding;
private ArrayList<ProductModels> productlist;
private ProductAdapters productAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("catname"));

        Intent intent = getIntent();
        int id = intent.getIntExtra("cid",0);

        Productselect(id);

        productlist = new ArrayList<>();

        productAdapters = new ProductAdapters(productlist,this);
        binding.categoryselectitemrecyclerview.setAdapter(productAdapters);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        binding.categoryselectitemrecyclerview.setLayoutManager(gridLayoutManager);
      }

void Productselect(int cid){
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://tutorials.mianasad.com/ecommerce/services/listProduct?category_id="+cid,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject mainobj = new JSONObject(response);
                        String status = mainobj.getString("status");
                        if(status.equals("success")){
                            JSONArray pdata = mainobj.getJSONArray("products");
                            for(int i=0; i<pdata.length(); i++){
                                JSONObject d1 = pdata.getJSONObject(i);

                                ProductModels productdata = new ProductModels(
                                        d1.getString("name"),
                                        Constants.PRODUCTS_IMAGE_URL+d1.getString("image"),
                                        d1.getString("status"),
                                        d1.getDouble("price"),
                                        d1.getInt("stock"),
                                        d1.getInt("id")
                                );
                                productlist.add(productdata);
                                productAdapters.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(CategorySelectActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(CategorySelectActivity.this, "Error while loading products", Toast.LENGTH_SHORT).show();
        }
    });

// Add the request to the RequestQueue.
    queue.add(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}