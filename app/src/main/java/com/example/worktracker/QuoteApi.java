package com.example.worktracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuoteApi {
    @GET("random")
    Call<List<QuoteResponse>> getRandomQuote();
}