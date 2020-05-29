package com.example.covid19updates.ui.homescreen;

import androidx.lifecycle.MutableLiveData;

import com.example.covid19updates.datamodels.SummaryModel;
import com.example.covid19updates.retrofit.RetrofitAPI;
import com.example.covid19updates.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//                    countryModelList = response.body().getContries();
                } else {
                    countryLiveData.setValue(null);
//                    Toast.makeText(this, "You have reached maximum request limit.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SummaryModel> call, Throwable t) {
//                llProgressbarContainer.setVisibility(View.GONE);
//                Log.d(TAG, "Countrylist: " + t.getMessage());
                countryLiveData.setValue(null);
            }
        });
        return countryLiveData;
    }
}
