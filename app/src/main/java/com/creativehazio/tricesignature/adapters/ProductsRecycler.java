package com.creativehazio.tricesignature.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.model.Product;

import java.util.ArrayList;

public class ProductsRecycler extends RecyclerView.Adapter<ProductsRecycler.ViewHolder> {

    private Context context;
    private ArrayList<Product> allProducts;
    private Listener listener;
//    private View emptyView;

    public interface Listener {
        void onClick(int position);
    }

    public ProductsRecycler(Context context, ArrayList<Product> allProducts) {
        this.context = context;
        this.allProducts = allProducts;
    }

    public void filterList(ArrayList<Product> filteredAllProductsArraylist){
        this.allProducts = filteredAllProductsArraylist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(context).inflate(R.layout.product_cardview,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
//        cardView.setBackgroundResource(R.drawable.product_border);
        ImageView productImage = cardView.findViewById(R.id.product_image);
        TextView productName = cardView.findViewById(R.id.product_name);
        TextView productDescription = cardView.findViewById(R.id.product_description);
        TextView productPrice = cardView.findViewById(R.id.product_price);

        Glide.with(context).load(allProducts.get(position).getImage()).into(productImage);
        productName.setText(allProducts.get(position).getName());
        productDescription.setText(allProducts.get(position).getDescription());
        productPrice.setText('\u20A6'+String.valueOf(allProducts.get(position).getPrice()));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(holder.getAdapterPosition());
                }
            }
        });

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return allProducts == null ? 0 : allProducts.size();
    }

//    public void displayEmptyStock(){
//        if (getItemCount() == 0){
//            emptyView.setVisibility(View.VISIBLE);
//        }else{
//            emptyView.setVisibility(View.GONE);
//        }
//    }
//    public void setEmptyView(View view){
//        this.emptyView = view;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
