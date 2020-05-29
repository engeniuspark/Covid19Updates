package com.example.covid19updates.retrofit;

import com.example.covid19updates.datamodels.SummaryModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {

    @GET("summary")
    Call<SummaryModel> getAllCountryNames();

}
