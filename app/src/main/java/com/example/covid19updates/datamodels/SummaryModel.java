package com.example.covid19updates.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SummaryModel {

    @SerializedName("Countries")
    @Expose
    private ArrayList<Countries> contries;

    @SerializedName("Date")
    private String date;

    @SerializedName("Global")
    Global global;

    public SummaryModel(ArrayList<Countries> contries) {
        this.contries = contries;
    }

    public ArrayList<Countries> getContries() {
        return contries;
    }
}
