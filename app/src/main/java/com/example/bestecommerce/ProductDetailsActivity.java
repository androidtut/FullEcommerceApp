package com.example.bestecommerce;

import static android.net.Uri.parse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bestecommerce.Fragments.BottomSheetFragment;
import com.example.bestecommerce.databinding.ActivityProductDetailsBinding;
import com.example.bestecommerce.models.ProductModels;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ProductDetailsActivity extends AppCompatActivity {
    ActivityProductDetailsBinding binding;
    private String desc;
    ProductModels productModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price",0d);
        String image = intent.getStringExtra("image");
        int id = intent.getIntExtra("Id", 223);

        getSupportActionBar().setTitle(name);
        Glide.with(this).load(image).into(binding.pimages);
        getProductDetails(id);

        Cart cart = TinyCartHelper.getCart();
        binding.addtocart.setOnClickListener(v->{
            cart.addItem(productModels,1);
            binding.addtocart.setEnabled(false);
            binding.addtocart.setText("Added in Cart");
            Toast.makeText(this, "Item is Added to Cart", Toast.LENGTH_SHORT).show();
        });

    }

    private void getProductDetails(int id){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_PRODUCT_DETAILS_URL+"?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if(mainobject.getString("status").equals("success")) {
                        JSONObject obj1 = mainobject.getJSONObject("product");
                        desc = obj1.getString("description");
                        binding.name.setText(Html.fromHtml(desc));
                        binding.status.setText("Status:-   "+obj1.getString("status"));
                        binding.stock.setText("Stock:-    "+obj1.getString("stock"));

                        binding.category.setTextColor(Color.BLUE);

                        JSONArray carray = obj1.getJSONArray("categories");
                        for(int i=0; i<carray.length(); i++){
                            JSONObject obj2 = carray.getJSONObject(i);
                           binding.category.setText("Category:-  "+obj2.getString("name"));
                        }
                        productModels = new ProductModels(
                                obj1.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL+obj1.getString("image"),
                                obj1.getString("status"),
                                obj1.getDouble("price"),
                                obj1.getInt("stock"),
                                obj1.getInt("id")
                        );

                    }
                 } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                startActivity(new Intent(ProductDetailsActivity.this,CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}