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
import com.creativehazio.tricesignature.model.ProductCategory;

import java.util.ArrayList;

public class ProductsCategoryRecycler extends RecyclerView.Adapter<ProductsCategoryRecycler.ViewHolder> {

    private Context context;
    private ArrayList<ProductCategory> productCategoryList;
    private Listener listener;

    public interface Listener{
        void onClick(int position);
    }

    public ProductsCategoryRecycler(Context context, ArrayList<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.product_category_cardview,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
//        cardView.setBackgroundResource(R.drawable.product_border);
        ImageView productCategoryImage = cardView.findViewById(R.id.product_category_image);
        TextView productName = cardView.findViewById(R.id.product_category);

        Glide.with(context).load(productCategoryList.get(position).getImageResourceId()).into(productCategoryImage);
        productCategoryImage.setContentDescription(productCategoryList.get(position).getCategory());
        productName.setText(productCategoryList.get(position).getCategory());

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
        return productCategoryList == null ? 0 : productCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }
}
