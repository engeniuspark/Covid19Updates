package com.example.covid19updates.ui.homescreen;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.covid19updates.datamodels.SummaryModel;
import com.example.covid19updates.retrofit.RetrofitAPI;
import com.example.covid19updates.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainRepository {
    private static MainRepository mainRepository;

    public static MainRepository getInstance() {
        if (mainRepository == null) {
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    private RetrofitAPI retrofitAPI;

    public MainRepository() {
        retrofitAPI = RetrofitClient.getInstance().getRetrofitApi();
    }

    public MutableLiveData<SummaryModel> getAllCountriesUpdates() {
        MutableLiveData<SummaryModel> countryLiveData = new MutableLiveData<>();

        retrofitAPI.getAllCountryNames().enqueue(new Callback<SummaryModel>() {

            @Override
            public void onResponse(Call<SummaryModel> call, Response<SummaryModel> response) {
                if (response.isSuccessful()) {
                    countryLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SummaryModel> call, Throwable t) {
                Log.d("MainRepo: ", "Error: " + t.getMessage());
            }
        });
        return countryLiveData;
    }
}
