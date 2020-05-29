package com.example.covid19updates.ui.homescreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covid19updates.datamodels.Countries;
import com.example.covid19updates.datamodels.SummaryModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private MutableLiveData<SummaryModel> mutableLiveData;
    private MainRepository mainRepository;
    ArrayList<Countries> countryModelList = new ArrayList<>();

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        mainRepository = mainRepository.getInstance();
        mutableLiveData = mainRepository.getAllCountriesUpdates();

    }

    public LiveData<SummaryModel> getMainRepository() {
        return mutableLiveData;
    }

//    public MainViewModel() {
//        mutableLiveData = new mutableLiveData<>();
//
//        // call your Rest API in init method
//        init();
//    }
//
//    public mutableLiveData<ArrayList<Countries>> getUserMutableLiveData() {
//
//        return mutableLiveData;
//    }
//
//    public void init(){
//        populateList();
//        mutableLiveData.setValue(countryModelList);
//    }
//
//    public void populateList(){
//
//        Call<SummaryModel> call = RetrofitClient.getInstance().getRetrofitApi().getAllCountryNames();
//        call.enqueue(new Callback<SummaryModel>() {
//            private Countries countryModel;
//
//            @Override
//            public void onResponse(Call<SummaryModel> call, Response<SummaryModel> response) {
//                if (response.code() == 200) {
//                    countryModelList = response.body().getContries();
//                } else {
////                    Toast.makeText(this, "You have reached maximum request limit.", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SummaryModel> call, Throwable t) {
////                llProgressbarContainer.setVisibility(View.GONE);
////                Log.d(TAG, "Countrylist: " + t.getMessage());
//            }
//        });
//    }
}