package com.example.dompetajaib.RestApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/v1/products")
    Call<List<Product>> getProducts();
}
