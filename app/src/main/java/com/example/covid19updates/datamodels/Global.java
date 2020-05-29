package com.example.covid19updates.datamodels;

import com.google.gson.annotations.SerializedName;

public class Global {

    @SerializedName("NewConfirmed")
     int newConfirmed;
    @SerializedName("NewDeaths")
    int newDeaths;
    @SerializedName("NewRecovered")
    int newRecovered;
    @SerializedName("TotalConfirmed")
    int totalConfirmed;
    @SerializedName("TotalDeaths")
    int totalDeaths;
    @SerializedName("TotalRecovered")
    int totalRecovered;

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public Global(int totalConfirmed, int totalDeaths, int totalRecovered) {
        this.totalConfirmed = totalConfirmed;
        this.totalDeaths = totalDeaths;
        this.totalRecovered = totalRecovered;
    }
}
