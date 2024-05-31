package com.example.dompetajaib.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dompetajaib.R;
import com.example.dompetajaib.RestApi.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameProduct.setText(" Name: " + product.getName());
        holder.descriptionProduct.setText(" Description: " + product.getDescription());
        holder.priceProduct.setText(" Price: " + product.getPrice());
        holder.ratingProduct.setText(" Rating: " + product.getRating());
        holder.brandProduct.setText(" Brand: " + product.getBrand());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView  nameProduct, descriptionProduct, priceProduct, ratingProduct, brandProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.name_product);
            descriptionProduct = itemView.findViewById(R.id.description_product);
            priceProduct = itemView.findViewById(R.id.price_product);
            ratingProduct = itemView.findViewById(R.id.rating_product);
            brandProduct = itemView.findViewById(R.id.brand_product);
        }
    }
}
