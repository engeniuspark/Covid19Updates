package com.example.covid19updates.datamodels;

import java.io.Serializable;
import java.util.Comparator;

public class Countries implements Serializable {
    private int id;
    String Country;
    int TotalConfirmed;
    int TotalDeaths;
    int TotalRecovered;

    public Countries(String country, int totalConfirmed, int totalDeaths, int totalRecovered) {
        Country = country;
        TotalConfirmed = totalConfirmed;
        TotalDeaths = totalDeaths;
        TotalRecovered = totalRecovered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return Country;
    }

    public int getTotalConfirmed() {
        return TotalConfirmed;
    }

    public int getTotalDeaths() {
        return TotalDeaths;
    }

    public int getTotalRecovered() {
        return TotalRecovered;
    }

    /*Comparator for sorting the list by totalCases*/
    public static Comparator<Countries> totalCases = new Comparator<Countries>() {

        public int compare(Countries c1, Countries c2) {

            int totalCase1 = c1.getTotalConfirmed();
            int totalCases2 = c2.getTotalConfirmed();

            /*For ascending order*/
//            return totalCase1-totalCases2;

            /*For descending order*/
            return totalCases2-totalCase1;
        }};

    /*Comparator for sorting the list by Total Deaths*/
    public static Comparator<Countries> deaths = new Comparator<Countries>() {

        public int compare(Countries c1, Countries c2) {

            int totalDeaths1 = c1.getTotalDeaths();
            int totalDeaths2 = c2.getTotalDeaths();

            /*For ascending order*/
//            return totalDeaths1-totalDeaths2;

            /*For descending order*/
            return totalDeaths2-totalDeaths1;
        }};

    /*Comparator for sorting the list by Total Recovred*/
    public static Comparator<Countries> recovred = new Comparator<Countries>() {

        public int compare(Countries c1, Countries c2) {

            int totalRecovred1 = c1.getTotalRecovered();
            int totalRecovred2 = c2.getTotalRecovered();

            /*For ascending order*/
//            return totalRecovred1-totalRecovred2;

            /*For descending order*/
            return totalRecovred2-totalRecovred1;
        }};

}
