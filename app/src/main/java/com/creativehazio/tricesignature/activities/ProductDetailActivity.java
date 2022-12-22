package com.creativehazio.tricesignature.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativehazio.tricesignature.R;
import com.creativehazio.tricesignature.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "productId";

    private FirebaseFirestore firestore;

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private TextView itemStock;
    private TextView itemAdded;
    private Button addItemBtn, removeItemBtn;
    private Button addToCartButton;
    private Toolbar toolbar;
    private int itemCounter = 0;
    private int stockUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initViews();
        setSupportActionBar(toolbar);

        String productId = getIntent().getStringExtra(EXTRA_ID);

        firestore.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        System.out.println(documentSnapshot.getData());

                        Product product = documentSnapshot.toObject(Product.class);
                        Glide.with(ProductDetailActivity.this).load(product.getImage()).into(productImage);
                        productName.setText(product.getName());
                        char nairaSymbol = '\u20A6';
                        productDescription.setText(product.getDescription());
                        productPrice.setText(nairaSymbol+String.valueOf(product.getPrice()));
                        itemStock.setText(String.valueOf(product.getStockUnit()));
                        stockUnit = product.getStockUnit();
                    }
                });

        itemAdded.setText("0");
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemCounter > stockUnit){
                    Toast.makeText(ProductDetailActivity.this, "O items left in stock", Toast.LENGTH_SHORT).show();
                }else{
                    itemAdded.setText(String.valueOf(itemCounter++));
                }
            }
        });
        removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemCounter < 0){
                    Toast.makeText(ProductDetailActivity.this, "!!!!", Toast.LENGTH_SHORT).show();
                }else{
                    itemAdded.setText(String.valueOf(itemCounter--));
                }
            }
        });
    }

    private void initViews(){
        productImage = findViewById(R.id.product_detail_image);
        productName = findViewById(R.id.product_detail_name);
        productPrice = findViewById(R.id.product_detail_price);
        toolbar = findViewById(R.id.product_detail_toolbar);
        itemStock = findViewById(R.id.items_stock_textview);
        itemAdded = findViewById(R.id.items_added_textview);
        addItemBtn = findViewById(R.id.add_to_cart_button);
        removeItemBtn = findViewById(R.id.remove_from_cart_button);
        addToCartButton = findViewById(R.id.product_detail_cart_button);
        productDescription = findViewById(R.id.product_detail_description);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.category_fragment_cart:
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK));
                return true;
            case R.id.category_fragment_profile:
                startActivity(new Intent(ProductDetailActivity.this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}