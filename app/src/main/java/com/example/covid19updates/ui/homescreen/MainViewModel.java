package com.example.covid19updates.ui.homescreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.covid19updates.datamodels.Countries;
import com.example.covid19updates.datamodels.SummaryModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private MutableLiveData<SummaryModel> mutableLiveData;
    private MainRepository mainRepository;

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
}