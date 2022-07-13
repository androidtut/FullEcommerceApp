package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bestecommerce.Adapters.CategoriesAdapters;
import com.example.bestecommerce.Adapters.ProductAdapters;
import com.example.bestecommerce.databinding.ActivityMainBinding;
import com.example.bestecommerce.models.CategoriesModels;
import com.example.bestecommerce.models.ProductModels;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<CategoriesModels>clist;
    private CategoriesAdapters cadapters;
    private RequestQueue queue;
    private ArrayList<ProductModels> productlist;
    private ProductAdapters productAdapters;
    private ActivityMainBinding binding;

    String image = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/20200203200336/13-Things-You-Should-Know-Before-You-Enter-In-Web-Development.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LoadCategory();
        LoadCarousel();
        LoadProducts();

        binding.searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                super.onSearchStateChanged(enabled);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent sintent = new Intent(MainActivity.this,SearchActivity.class);
                sintent.putExtra("searchtext",text.toString());
                startActivity(sintent);
                super.onSearchConfirmed(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                super.onButtonClicked(buttonCode);
            }
        });

    }

    private void fetchcategory(){
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        String url = Constants.GET_CATEGORIES_URL;

       // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.get("status").equals("success")){
                             JSONArray data = jsonObject.getJSONArray("categories");
                             for(int i=0; i<data.length(); i++){
                                JSONObject d1 = data.getJSONObject(i);

                                CategoriesModels category = new CategoriesModels(
                                        d1.getInt("id"),
                                        d1.getString("name"),
                                        d1.getString("brief"),
                                        Constants.CATEGORIES_IMAGE_URL+d1.getString("icon")
                                );
                                clist.add(category);
                                cadapters.notifyDataSetChanged();
                             }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    private void fetchProduct(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_PRODUCTS_URL+"?count=6",
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
                            }
                          } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error while loading products", Toast.LENGTH_SHORT).show();
                }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void fetchCarousels(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if(mainobj.get("status").equals("success")){
                        JSONArray cadata = mainobj.getJSONArray("news_infos");
                        for(int i=0; i<cadata.length(); i++){
                            JSONObject objdata = cadata.getJSONObject(i);
                            String image1 = Constants.NEWS_IMAGE_URL+objdata.getString("image");
                            String caption = objdata.getString("title");
                            binding.carousel.addData(new CarouselItem(image1,caption));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error while loading carousels", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void LoadCarousel() {
        fetchCarousels();
//        binding.carousel.addData(new CarouselItem("https://www.cnet.com/a/img/2022/05/27/807982ed-f8b7-4f15-9f8d-c7f1dd40ffc0/alert-promo-alt3-2.png","Best Laptop with less price"));
//        binding.carousel.addData(new CarouselItem("https://media.istockphoto.com/photos/unrecognizable-young-woman-at-home-working-on-laptop-studying-picture-id671327110?s=612x612","our teams"));
    }

    private void LoadProducts() {
        productlist = new ArrayList<>();
//        productlist.add(new ProductModels("Cosmetics Set-2","https://tutorials.mianasad.com/ecommerce/uploads/product/1657343550623.png","1",420,3,1));
//        productlist.add(new ProductModels("Cosmetic Set-1","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341628534.jpg","1",555,3,2));
//        productlist.add(new ProductModels("Himalaya Face Wash","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341431417.jpg","1",420,3,3));
//        productlist.add(new ProductModels("Fash Wahs Set-1","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341342319.jpg","1",300,3,4));
//        productlist.add(new ProductModels("Indain No.1 Parfume","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341281901.jpg","1",300,3,5));
//        productlist.add(new ProductModels("Best Parfume","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341188811.jpg","1",300,3,6));
//        productlist.add(new ProductModels("Diary Milk Gift Chocolate Box","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341110088.jpg","1",300,3,7));
//        productlist.add(new ProductModels("Diary Milk Silk Bubbly Chocolate","https://tutorials.mianasad.com/ecommerce/uploads/product/1657340890845.jpg","1",300,3,8));

        fetchProduct();

        productAdapters = new ProductAdapters(productlist,this);
        binding.productrecyclerview.setAdapter(productAdapters);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        binding.productrecyclerview.setLayoutManager(gridLayoutManager);
    }

    private void LoadCategory() {
        clist = new ArrayList<>();
//        clist.add(new CategoriesModels(1,"Chocolate","GridLayoutManager",image));
//        clist.add(new CategoriesModels(2,"Cosmetic","GridLayoutManager",image));
//        clist.add(new CategoriesModels(3,"Face Watch","GridLayoutManager",image));
//        clist.add(new CategoriesModels(4,"sport","GridLayoutManager",image));
//        clist.add(new CategoriesModels(5,"sport","GridLayoutManager",image));
//        clist.add(new CategoriesModels(6,"sport","GridLayoutManager",image));

        fetchcategory();
        cadapters = new CategoriesAdapters(clist,this);
        binding.categoriesrecyclerview.setAdapter(cadapters);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        binding.categoriesrecyclerview.setLayoutManager(gridLayoutManager);
    }

//    private void LoadCarousel() {
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1656917837537.jpg","Hello world"));
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1656917837537.jpg","Hello world"));
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1656622558486.jpg","Hello world"));
//    }

}