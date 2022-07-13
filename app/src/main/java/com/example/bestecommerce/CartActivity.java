package com.example.bestecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bestecommerce.Adapters.CartAdapters;
import com.example.bestecommerce.databinding.ActivityCartBinding;
import com.example.bestecommerce.models.ProductModels;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
ActivityCartBinding binding;
ArrayList<ProductModels>cartitems;
CartAdapters cartadapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();
        cartitems = new ArrayList<>();

        if(cart.isCartEmpty()){
            Toast.makeText(this, "Please add to cart", Toast.LENGTH_SHORT).show();
        }else{
        for(Map.Entry<Item,Integer>item: cart.getAllItemsWithQty().entrySet()){
            ProductModels productModels1 = (ProductModels) item.getKey();
            int quantity = item.getValue();
            productModels1.setQuantity(quantity);
            cartitems.add(productModels1);
        }
      }
//        cartitems = new ArrayList<>();
//        cartitems.add(new ProductModels("Cosmetics Set-2","https://tutorials.mianasad.com/ecommerce/uploads/product/1657343550623.png","1",420,3,1));
//        cartitems.add(new ProductModels("Cosmetic Set-1","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341628534.jpg","1",555,3,2));
//        cartitems.add(new ProductModels("Himalaya Face Wash","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341431417.jpg","1",420,3,3));
//        cartitems.add(new ProductModels("Fash Wahs Set-1","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341342319.jpg","1",300,3,4));
//        cartitems.add(new ProductModels("Indain No.1 Parfume","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341281901.jpg","1",300,3,5));
//        cartitems.add(new ProductModels("Best Parfume","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341188811.jpg","1",300,3,6));
//        cartitems.add(new ProductModels("Diary Milk Gift Chocolate Box","https://tutorials.mianasad.com/ecommerce/uploads/product/1657341110088.jpg","1",300,3,7));
//        cartitems.add(new ProductModels("Diary Milk Silk Bubbly Chocolate","https://tutorials.mianasad.com/ecommerce/uploads/product/1657340890845.jpg","1",300,3,8));

        cartadapters = new CartAdapters(cartitems, this, new CartAdapters.CartListener() {
            @Override
            public void onQuantityChangeListener() {
                binding.totalprice.setText("Total Price:- "+cart.getTotalPrice().toString());
            }
        });

        binding.continuee.setOnClickListener(v->{
            startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
        });

        binding.totalprice.setText(cart.getTotalPrice().toString());
        binding.shoppingitemsrecyclerview.setAdapter(cartadapters);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.shoppingitemsrecyclerview.setLayoutManager(linearLayoutManager);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}