package com.example.bestecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestecommerce.ProductDetailsActivity;
import com.example.bestecommerce.R;
import com.example.bestecommerce.databinding.SampleProductBinding;
import com.example.bestecommerce.models.ProductModels;

import java.util.ArrayList;

public class ProductAdapters extends RecyclerView.Adapter<ProductAdapters.productViewHolder>{
    ArrayList<ProductModels> productlist;
    Context context;

    public ProductAdapters(ArrayList<ProductModels> productlist, Context context) {
        this.productlist = productlist;
        this.context = context;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new productViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
       final ProductModels productModels = productlist.get(position);
        Glide.with(context).load(productModels.getImage()).into(holder.binding.pimage);
        holder.binding.pprice.setText("PRK "+productModels.getPrice());
        holder.binding.ptitle.setText(productModels.getName());

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context.getApplicationContext(), ProductDetailsActivity.class);
            intent.putExtra("name",productModels.getName());
            intent.putExtra("image",productModels.getImage());
            intent.putExtra("price",productModels.getPrice());
            intent.putExtra("Id",productModels.getId());
            intent.putExtra("stock",productModels.getStock());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder{
        SampleProductBinding binding;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleProductBinding.bind(itemView);
        }
    }
}
