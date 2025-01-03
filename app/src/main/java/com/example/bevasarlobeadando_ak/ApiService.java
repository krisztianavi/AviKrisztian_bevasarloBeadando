package com.example.bevasarlobeadando_ak;

import java.util.List;

import retrofit2.*;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("hoxkBo/data")
    Call<List<Termekek>> getTermekek();

    @GET("hoxkBo/data/{id}")
    Call<Termekek> getTermek(@Path("id") int id);

    @POST("hoxkBo/data")
    Call<Void> addTermek(@Body Termekek termek);

    @PUT("hoxkBo/data/{id}")
    Call<Void> updateTermek(@Path("id") int id, @Body Termekek termek);

    @DELETE("hoxkBo/data/{id}")
    Call<Void> deleteTermek(@Path("id") int id);
}
