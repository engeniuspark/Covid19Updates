package com.example.covid19updates.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19updates.R;
import com.example.covid19updates.datamodels.Countries;

import java.util.List;

public class CountryRVAdapter extends RecyclerView.Adapter<CountryRVAdapter.ViewHolder> {

    private final int FIRST_ROW_COLOR = 0xFFd45155;
    private final int ALL_OTHER_ROWS_COLOR = 0xFFFFFFFF;
    private List<Countries> countryNamesList;
    private View view;

    public CountryRVAdapter(List<Countries> countryNamesList) {
        this.countryNamesList = countryNamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_country_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Countries countryModel = countryNamesList.get(position);

        if(position == 0){
            changeStyle(holder, Typeface.BOLD,FIRST_ROW_COLOR);
        }else{
            changeStyle(holder, Typeface.NORMAL,ALL_OTHER_ROWS_COLOR);
        }

        holder.tvCountryName.setText(countryModel.getCountryName());
        holder.tvTotalCases.setText(String.valueOf(countryModel.getTotalConfirmed()));
        holder.tvDeaths.setText(String.valueOf(countryModel.getTotalDeaths()));
        holder.tvRecovered.setText(String.valueOf(countryModel.getTotalRecovered()));
    }

    private void changeStyle(@NonNull ViewHolder holder, int typeface,int color) {
        holder.tvCountryName.setTypeface(Typeface.defaultFromStyle(typeface));
        holder.tvTotalCases.setTypeface(Typeface.defaultFromStyle(typeface));
        holder.tvDeaths.setTypeface(Typeface.defaultFromStyle(typeface));
        holder.tvRecovered.setTypeface(Typeface.defaultFromStyle(typeface));
        holder.tvCountryName.setTextColor(color);
        holder.tvTotalCases.setTextColor(color);;
        holder.tvDeaths.setTextColor(color);;
        holder.tvRecovered.setTextColor(color);;
    }

    @Override
    public int getItemCount() {
        return countryNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCountryName;
        TextView tvTotalCases;
        TextView tvDeaths;
        TextView tvRecovered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            tvTotalCases = itemView.findViewById(R.id.tvTotalCases);
            tvDeaths = itemView.findViewById(R.id.tvDeaths);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);

        }
    }

}