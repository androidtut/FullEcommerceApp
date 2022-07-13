package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
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
import com.example.bestecommerce.Adapters.CartAdapters;
import com.example.bestecommerce.databinding.ActivityCheckoutBinding;
import com.example.bestecommerce.models.ProductModels;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    ArrayList<ProductModels>cartitems;
    CartAdapters cartadapters;
    double totalprice = 0;
    final int tax = 11;
    private String name,email,phonenumber,address,date,comment;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();
        cartitems = new ArrayList<>();

            for(Map.Entry<Item,Integer>item: cart.getAllItemsWithQty().entrySet()){
                ProductModels productModels1 = (ProductModels) item.getKey();
                int quantity = item.getValue();
                productModels1.setQuantity(quantity);
                cartitems.add(productModels1);
            }

            binding.subtotal.setText(cart.getTotalPrice().toString());

            totalprice = (cart.getTotalPrice().doubleValue() * tax/100) +  + cart.getTotalPrice().doubleValue();
            binding.total.setText("Total:- "+totalprice);

            cartadapters = new CartAdapters(cartitems, this, new CartAdapters.CartListener() {
                @Override
                public void onQuantityChangeListener() {
                  binding.subtotal.setText(cart.getTotalPrice().toString());
                }
            });
            binding.userordersrecyclerview.setAdapter(cartadapters);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.userordersrecyclerview.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("wait while loading...");

        binding.checkbox.setOnClickListener(v->{
           name = binding.fullname.getText().toString();
           address = binding.addressbox.getText().toString();
           phonenumber = binding.phonebox.getText().toString();
           email = binding.emailbox.getText().toString();
           date = binding.dateshipping.getText().toString();
           comment = binding.comment.getText().toString();

           if(name.isEmpty() || name.length() < 3){
               binding.fullname.setError("name most be 3 character");
           }else if(email.isEmpty()){
               binding.emailbox.setError("please enter a email");
           }else if(phonenumber.isEmpty()){
               binding.phonebox.setError("please enter your phone number");
           }else if(address.isEmpty()){
               binding.phonebox.setError("please enter your address");
           }else{
//              processOrder();
               Intent intent = new Intent(CheckoutActivity.this,PaymentActivity.class);
               intent.putExtra("code","34384384ddf");
               startActivity(intent);
           }
        });


    }

    void processOrder(){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        JSONObject productobject = new JSONObject();
        try {
            jsonObject.put("name","hello world");
            jsonObject.put("email","hello world");
            jsonObject.put("address","hello world");
            jsonObject.put("phone","hello world");

            productobject.put("product_order",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, productobject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}