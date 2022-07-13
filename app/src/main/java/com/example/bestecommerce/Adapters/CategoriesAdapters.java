package com.example.bestecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestecommerce.CategorySelectActivity;
import com.example.bestecommerce.R;
import com.example.bestecommerce.databinding.SampleCategoriesBinding;
import com.example.bestecommerce.models.CategoriesModels;

import java.util.ArrayList;

public class CategoriesAdapters extends RecyclerView.Adapter<CategoriesAdapters.categoriesViewholder>{
    ArrayList<CategoriesModels>categoriesList;
    Context context;

    public CategoriesAdapters(ArrayList<CategoriesModels> categoriesList, Context context) {
        this.categoriesList = categoriesList;
        this.context = context;
    }

    @NonNull
    @Override
    public categoriesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_categories,parent,false);
        return new categoriesViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoriesViewholder holder, int position) {
       final CategoriesModels cmodels = categoriesList.get(position);
       holder.binding.categoriesName.setText(Html.fromHtml(cmodels.getName()));
       Glide.with(context).load(cmodels.getImage()).into(holder.binding.categoriesImage);
       holder.itemView.setOnClickListener(v->{
           Intent intent = new Intent(context.getApplicationContext(), CategorySelectActivity.class);
           intent.putExtra("catname",cmodels.getName());
           intent.putExtra("cid",cmodels.getId());
           context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class categoriesViewholder extends RecyclerView.ViewHolder{
       SampleCategoriesBinding binding;
        public categoriesViewholder(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoriesBinding.bind(itemView);
             }
    }
}
