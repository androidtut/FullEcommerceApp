package com.example.bestecommerce.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestecommerce.Fragments.BottomSheetFragment;
import com.example.bestecommerce.R;
import com.example.bestecommerce.databinding.ItemCartBinding;
import com.example.bestecommerce.databinding.QuantityDialogBinding;
import com.example.bestecommerce.models.ProductModels;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;

public class CartAdapters extends RecyclerView.Adapter<CartAdapters.CartViewHolder>{
    ArrayList<ProductModels>cartlist;
    Context context;
    CartListener cartListener;
    Cart cart;

    public interface CartListener{
        public void onQuantityChangeListener();
    }

    public CartAdapters(ArrayList<ProductModels> cartlist, Context context,CartListener cartListener) {
        this.cartlist = cartlist;
        this.context = context;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        final ProductModels products = cartlist.get(position);
        Glide.with(context).load(products.getImage()).into(holder.binding.cartimage);
        holder.binding.cartname.setText(products.getName());
        holder.binding.cartprice.setText("PKR "+products.getPrice());
        holder.binding.cartitems.setText(""+products.getStock());

        holder.itemView.setOnClickListener(v->{
//            alert dialog

            Toast.makeText(context, "Edit item is "+position, Toast.LENGTH_SHORT).show();
            DialogPlus dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.quantity_dialog))
                    .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
            dialog.show();

            View view = dialog.getHolderView();
            Button save,incrementquantity,dquantity;
            TextView showquantity,pname,pstock;

            save = view.findViewById(R.id.save);
            incrementquantity = view.findViewById(R.id.quantity_increment);
            dquantity = view.findViewById(R.id.quantity_decrement);
            showquantity = view.findViewById(R.id.quantitynumber);
            pname = view.findViewById(R.id.pname);
            pstock = view.findViewById(R.id.pstock);

            save.setOnClickListener(v1->{
                Toast.makeText(context, "Hello world", Toast.LENGTH_SHORT).show();
            });

            incrementquantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = products.getQuantity();
                    quantity++;
                    if(quantity > products.getStock()){
                        Toast.makeText(context, "max stock is less then "+products.getStock(), Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        products.setQuantity(quantity);
                        showquantity.setText(String.valueOf(quantity));
                    }
                }
            });

            dquantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = products.getQuantity();
                    if (quantity > 1)
                        quantity--;
                    products.setQuantity(quantity);
                    showquantity.setText(String.valueOf(quantity));
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    cart.updateItem(products,products.getQuantity());
                    cartListener.onQuantityChangeListener();
                    notifyDataSetChanged();
                    holder.binding.cartprice.setTextColor(Color.GREEN);
                }
            });

            pstock.setText(""+products.getStock());
            pname.setText(products.getName());
        });

    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }

}
