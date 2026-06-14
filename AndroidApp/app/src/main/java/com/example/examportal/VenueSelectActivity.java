package com.example.examportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VenueSelectActivity extends AppCompatActivity {

    TextView tvInvigilatorName, tvInvigilatorRole;
    Spinner spinnerVenue, spinnerDate;
    Button btnProceed, btnLogout;
    ProgressBar progressBar;

    OkHttpClient client = new OkHttpClient();

    // Change to your IP when testing on physical phone
    String BASE_URL = "http://10.114.190.95:5000/api";

    List<String> venueList = new ArrayList<>();
    List<String> dateList  = new ArrayList<>();

    ArrayAdapter<String> venueAdapter;
    ArrayAdapter<String> dateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_select);

        tvInvigilatorName = findViewById(R.id.tvInvigilatorName);
        tvInvigilatorRole = findViewById(R.id.tvInvigilatorRole);
        spinnerVenue      = findViewById(R.id.spinnerVenue);
        spinnerDate       = findViewById(R.id.spinnerDate);
        btnProceed        = findViewById(R.id.btnProceed);
        btnLogout         = findViewById(R.id.btnLogout);

        loadInvigilatorData();
        setupAdapters();
        fetchVenues();
        setupButtons();
    }

    private void loadInvigilatorData() {
        SharedPreferences prefs = getSharedPreferences("ExamPortal", MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "");
        String lastName  = prefs.getString("lastName", "");
        String role      = prefs.getString("role", "Invigilator");
        String dept      = prefs.getString("department", "");

        tvInvigilatorName.setText(firstName + " " + lastName);
        if (!dept.isEmpty()) {
            tvInvigilatorRole.setText(capitalize(role) + "  ·  " + dept);
        } else {
            tvInvigilatorRole.setText(capitalize(role));
        }
    }

    private void setupAdapters() {
        // Venue adapter
        venueAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                venueList
        );
        venueAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerVenue.setAdapter(venueAdapter);

        // Date adapter
        dateAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dateList
        );
        dateAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerDate.setAdapter(dateAdapter);

        // When venue changes fetch dates for that venue
        spinnerVenue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVenue = venueList.get(position);
                fetchDates(selectedVenue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchVenues() {
        btnProceed.setEnabled(false);

        Request request = new Request.Builder()
                .url(BASE_URL + "/invigilator/venues")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    btnProceed.setEnabled(true);
                    Toast.makeText(VenueSelectActivity.this,
                            "Failed to load venues: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject json    = new JSONObject(body);
                        JSONArray  venues  = json.getJSONArray("venues");

                        venueList.clear();
                        for (int i = 0; i < venues.length(); i++) {
                            venueList.add(venues.getString(i));
                        }
                        venueAdapter.notifyDataSetChanged();
                        btnProceed.setEnabled(true);

                        // Fetch dates for first venue automatically
                        if (!venueList.isEmpty()) {
                            fetchDates(venueList.get(0));
                        }

                    } catch (Exception e) {
                        Toast.makeText(VenueSelectActivity.this,
                                "Error loading venues",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void fetchDates(String venue) {
        dateList.clear();
        dateAdapter.notifyDataSetChanged();

        String url = BASE_URL + "/invigilator/dates?venue=" +
                venue.replace(" ", "%20");

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(VenueSelectActivity.this,
                                "Failed to load dates",
                                Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject json  = new JSONObject(body);
                        JSONArray  dates = json.getJSONArray("dates");

                        dateList.clear();
                        for (int i = 0; i < dates.length(); i++) {
                            dateList.add(dates.getString(i));
                        }
                        dateAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(VenueSelectActivity.this,
                                "Error loading dates",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setupButtons() {
        btnProceed.setOnClickListener(v -> {
            if (venueList.isEmpty() || dateList.isEmpty()) {
                Toast.makeText(this,
                        "Please wait for venues and dates to load",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedVenue = spinnerVenue.getSelectedItem().toString();
            String selectedDate  = spinnerDate.getSelectedItem().toString();

            Intent intent = new Intent(
                    VenueSelectActivity.this,
                    ScanTypeActivity.class
            );
            intent.putExtra("venue", selectedVenue);
            intent.putExtra("date", selectedDate);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) -> logout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logout() {
        getSharedPreferences("ExamPortal", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(
                VenueSelectActivity.this,
                MainActivity.class
        );
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() +
                text.substring(1).toLowerCase();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showLogoutDialog();
    }
}