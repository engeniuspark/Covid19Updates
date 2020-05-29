package com.example.covid19updates.ui.homescreen;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19updates.R;
import com.example.covid19updates.adapters.CountryRVAdapter;
import com.example.covid19updates.datamodels.Countries;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedpreferences;
    public static final String PREFERENCENAME = "preference_name";
    public static final String ITEMPOSITION = "item_position";

    private static final String TAG = "Test";
    private static final int REQUEST_CODE = 1000;
    TextView tvTotalCasesCount, tvTotalRecovered, tvTotalDeaths;
    private LinearLayout llProgressbarContainer;
    Button btnFilterResults;
    TextView tvCountryHeader, tvTotalCasesHeader, tvDeathsHeader, tvRecoveredHeader;
    RecyclerView recyclerView;
    CountryRVAdapter adapter;
    ArrayList<Countries> countryModelList = new ArrayList<>();
    ArrayList<Countries> tempArrayList = new ArrayList<>();

    private FusedLocationProviderClient mFusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private String countryName;
    final Handler handler = new Handler();
    private boolean totalCaseAscending = false;
    private boolean deathsAscending = false;
    private boolean recoverdDscending = false;
    private Dialog dialog;
    String[] patientTypes = {"Total Cases", "Deaths", "Recovered"};
    String[] conditions = {"<=", ">="};
    String strPatientType, conditionOperator;
    MainViewModel viewModel;
    private MainActivity context;
    private Countries countryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        getSupportActionBar().setTitle("Covid19 Counter");

        sharedpreferences = getSharedPreferences(PREFERENCENAME,
                Context.MODE_PRIVATE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

        tvTotalCasesCount = findViewById(R.id.tvTotalCasesCount);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        btnFilterResults = findViewById(R.id.btnFilterResults);
        btnFilterResults.setOnClickListener(this);

        tvCountryHeader = findViewById(R.id.tvCountryHeader);
        tvCountryHeader.setOnClickListener(this);
        tvTotalCasesHeader = findViewById(R.id.tvTotalCasesHeader);
        tvTotalCasesHeader.setOnClickListener(this);
        tvDeathsHeader = findViewById(R.id.tvDeathsHeader);
        tvDeathsHeader.setOnClickListener(this);
        tvRecoveredHeader = findViewById(R.id.tvRecoveredHeader);
        tvRecoveredHeader.setOnClickListener(this);

        recyclerView = findViewById(R.id.rv_countries);
        llProgressbarContainer = findViewById(R.id.llProgressbarContainer);

        viewModel = ViewModelProviders.of(context).get(MainViewModel.class);
        viewModel.init();

        getDataFromViewModel();

    }

    private void setupRecyclerView(ArrayList<Countries> countriesArrayList) {
        if (adapter == null) {
            adapter = new CountryRVAdapter(countriesArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void sortData(boolean ascending, Comparator<Countries> obj_comparator, TextView textView) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (ascending) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_descending, 0, 0, 0);
            Collections.sort(countryModelList.subList(1, countryModelList.size()), obj_comparator);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ascending, 0, 0, 0);
            Collections.sort(countryModelList.subList(1, countryModelList.size()), Collections.reverseOrder(obj_comparator));
        }
//        ADAPTER
        adapter = new CountryRVAdapter(countryModelList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(new Runnable() {
            public void run() {
                getDataFromViewModel();
                handler.postDelayed(this, 120000); //now is every 2 minutes
            }
        }, 120000); //
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
            // already permission granted
            // get location here
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
//                    wayLatitude =  26.579746;
//                    wayLongitude = 30.137615;
                    System.out.println("CURRENT LOCATION" + String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                    countryName = getCountryName(MainActivity.this, wayLatitude, wayLongitude);
                    System.out.println("COUNTRY: " + countryName);
                }
            });
        }
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }

        } catch (IOException ignored) {
            //do something
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            System.out.println("CURRENT LOCATION: " + String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                            countryName = getCountryName(MainActivity.this, wayLatitude, wayLongitude);
                            System.out.println("COUNTRY: " + countryName);
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getDataFromViewModel() {
        viewModel.getMainRepository().observe(context, summaryModel -> {

            if (!summaryModel.getContries().isEmpty()){
               llProgressbarContainer.setVisibility(View.GONE);
            }
            countryModelList = summaryModel.getContries();

            Collections.sort(countryModelList, Countries.totalCases);
            int itemPos = 0;
            int totalCases = 0;
            int deaths = 0;
            int recovered = 0;
            for (Countries countryModel : countryModelList) {
                if (countryModel.getCountryName().equalsIgnoreCase(countryName)) {
                    itemPos = countryModelList.indexOf(countryModel);
                    saveCurrentLocationPosition(itemPos);
                    this.countryModel = countryModel;
                }
                totalCases += countryModel.getTotalConfirmed();
                deaths += countryModel.getTotalDeaths();
                recovered += countryModel.getTotalRecovered();
            }
            countryModelList.remove(itemPos);
            countryModelList.add(0, countryModel);

            tvTotalCasesCount.setText(String.valueOf(totalCases));
            tvTotalDeaths.setText(String.valueOf(deaths));
            tvTotalRecovered.setText(String.valueOf(recovered));

//            adapter = new CountryRVAdapter(summaryModel.getContries());
//            recyclerView.setAdapter(adapter);
            setupRecyclerView(countryModelList);
        });
    }

    private void saveCurrentLocationPosition(int itemPos) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(ITEMPOSITION, itemPos);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnFilterResults:
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_dialog);
                dialog.show();

                Spinner spinner1 = dialog.findViewById(R.id.patientTypeSpinner);
//                spinner1.setOnItemSelectedListener(this);

                Spinner spinner2 = dialog.findViewById(R.id.conditionSpinner);
//                spinner1.setOnItemSelectedListener(this);

                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, patientTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Setting the ArrayAdapter data on the Spinner
                spinner1.setAdapter(adapter);
                spinner2.setAdapter(adapter1);

                EditText editText = dialog.findViewById(R.id.etNumber);

                Button btnApply = (Button) dialog.findViewById(R.id.btnApply);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkCondition(spinner1.getSelectedItem().toString(),
                                spinner2.getSelectedItem().toString(),
                                editText.getText().toString());
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.tvCountryHeader:
                // do your code
                break;

            case R.id.tvTotalCasesHeader:

                sortData(totalCaseAscending, Countries.totalCases, tvTotalCasesHeader);
                totalCaseAscending = !totalCaseAscending;
                break;

            case R.id.tvDeathsHeader:
                sortData(deathsAscending, Countries.deaths, tvDeathsHeader);
                deathsAscending = !deathsAscending;
                break;

            case R.id.tvRecoveredHeader:
                sortData(recoverdDscending, Countries.recovred, tvRecoveredHeader);
                recoverdDscending = !recoverdDscending;
                break;

            default:
                break;
        }
    }

    private void checkCondition(String strPatientType, String conditionOperator, String value) {

        if (strPatientType.equalsIgnoreCase("Total Cases")) {

            tempArrayList.clear();
            for (Countries item : countryModelList) {
                if (conditionOperator.equalsIgnoreCase("<=")) {
                    if (item.getTotalConfirmed() <= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                } else {
                    if (item.getTotalConfirmed() >= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                }
            }
            adapter = new CountryRVAdapter(tempArrayList);
            recyclerView.setAdapter(adapter);
        }

        if (strPatientType.equalsIgnoreCase("Deaths")) {

            tempArrayList.clear();
            for (Countries item : countryModelList) {
                if (conditionOperator.equalsIgnoreCase("<=")) {
                    if (item.getTotalDeaths() <= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                } else {
                    if (item.getTotalDeaths() >= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                }
            }
            adapter = new CountryRVAdapter(tempArrayList);
            recyclerView.setAdapter(adapter);
        }

        if (strPatientType.equalsIgnoreCase("Recovered")) {

            tempArrayList.clear();
            for (Countries item : countryModelList) {
                if (conditionOperator.equalsIgnoreCase("<=")) {
                    if (item.getTotalRecovered() <= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                } else {
                    if (item.getTotalConfirmed() >= Integer.parseInt(value)) {
                        tempArrayList.add(item);
                    }
                }
            }
            adapter = new CountryRVAdapter(tempArrayList);
            recyclerView.setAdapter(adapter);
        }
    }

    Observer<ArrayList<Countries>> countryListUpdateObserver = new Observer<ArrayList<Countries>>() {
        private Countries countryModel;

        @Override
        public void onChanged(ArrayList<Countries> countriesArrayList) {

        }
    };
}