package com.example.dompetajaib.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dompetajaib.Adapter.ProductAdapter;
import com.example.dompetajaib.R;
import com.example.dompetajaib.RestApi.ApiService;
import com.example.dompetajaib.RestApi.Product;
import com.example.dompetajaib.RestApi.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private TextView errorTextView;
    private ProgressBar loadingProduct;
    private SearchView searchProduct;
    private Button loadMoreButton;
    private ProgressBar loadingMoreProgress;
    private List<Product> productList = new ArrayList<>();
    private List<Product> displayedList = new ArrayList<>();
    private TextView noProductTextView;
    private static final int PAGE_SIZE = 10;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        errorTextView = view.findViewById(R.id.errorTextView);
        loadingProduct = view.findViewById(R.id.loading_product);
        searchProduct = view.findViewById(R.id.search_product);
        noProductTextView = view.findViewById(R.id.noProductTextView);
        loadMoreButton = view.findViewById(R.id.loadmore);
        loadingMoreProgress = view.findViewById(R.id.loading_more_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreButton.setVisibility(View.GONE);
                loadingMoreProgress.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreProducts();
                        loadingMoreProgress.setVisibility(View.GONE);
                    }
                }, 1000); // Delay of 1 second
            }
        });

        // Fetch products from API
        if (isNetworkAvailable()) {
            fetchProducts();
        } else {
            showError("Upsss, Coba periksa jaringan anda");
        }

        setupSearchView();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchProducts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    currentPage = 0;
                    loadMoreProducts();
                    loadingProduct.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.GONE);
                    noProductTextView.setVisibility(View.GONE);
                } else {
                    showError("Failed to retrieve products");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        recyclerView.setVisibility(View.GONE);
        loadingProduct.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }

    private void setupSearchView() {
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProductsByName(newText);
                return true;
            }
        });
    }

    private void filterProductsByName(String name) {
        loadingProduct.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        noProductTextView.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            if (filteredList.isEmpty()) {
                noProductTextView.setVisibility(View.VISIBLE);
            } else {
                noProductTextView.setVisibility(View.GONE);
            }
            productAdapter = new ProductAdapter(filteredList);
            recyclerView.setAdapter(productAdapter);
            loadingProduct.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }, 2000); // Show loading for 2 seconds
    }

    private void loadMoreProducts() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, productList.size());
        if (start < end) {
            displayedList.addAll(productList.subList(start, end));
            currentPage++;
            if (productAdapter == null) {
                productAdapter = new ProductAdapter(displayedList);
                recyclerView.setAdapter(productAdapter);
            } else {
                productAdapter.notifyDataSetChanged();
            }
            if (end == productList.size()) {
                loadMoreButton.setVisibility(View.GONE);
            } else {
                loadMoreButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
