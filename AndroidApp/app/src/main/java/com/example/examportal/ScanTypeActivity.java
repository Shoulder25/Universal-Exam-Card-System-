package com.example.examportal;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ScanTypeActivity extends AppCompatActivity {

    private TextView tvBack, tvVenueHeader, tvVenueName, tvVenueDate;
    private Button btnLogout;

    private String selectedVenue;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_type);

        // Initialize views
        tvBack = findViewById(R.id.tvBack);
        tvVenueHeader = findViewById(R.id.tvVenueHeader);
        tvVenueName = findViewById(R.id.tvVenueName);
        tvVenueDate = findViewById(R.id.tvVenueDate);
        btnLogout = findViewById(R.id.btnLogout);

        // Get data from previous activity
        selectedVenue = getIntent().getStringExtra("venue");
        selectedDate = getIntent().getStringExtra("date");

        // Set text using proper formatting (fixed warnings)
        tvVenueHeader.setText(getString(R.string.venue_header, selectedVenue, selectedDate));
        tvVenueName.setText(getString(R.string.venue_name, selectedVenue));
        tvVenueDate.setText(getString(R.string.venue_date, selectedDate));

        setupButtons();

        // Modern Back Press Handling (Fixes the main error)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();   // Same behavior as before
            }
        });
    }

    private void setupButtons() {

        // Back arrow
        tvBack.setOnClickListener(v -> finish());

        // Scan Exam Card
        findViewById(R.id.cardScanExamCard).setOnClickListener(v -> {
            Intent intent = new Intent(
                    ScanTypeActivity.this,
                    ScanExamCardActivity.class
            );
            intent.putExtra("venue", selectedVenue);
            intent.putExtra("date",  selectedDate);
            startActivity(intent);
        });

        // No Exam Card
        findViewById(R.id.cardNoExamCard).setOnClickListener(v -> {
            Intent intent = new Intent(
                    ScanTypeActivity.this,
                    NoExamCardActivity.class
            );
            intent.putExtra("venue", selectedVenue);
            intent.putExtra("date",  selectedDate);
            startActivity(intent);
        });
        // Collect Scripts button
        findViewById(R.id.cardCollectScripts).setOnClickListener(v -> {
            Intent intent = new Intent(
                    ScanTypeActivity.this,
                    ScriptCourseActivity.class
            );
            intent.putExtra("venue", selectedVenue);
            intent.putExtra("date",  selectedDate);
            startActivity(intent);
        });

        // Logout
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

        Intent intent = new Intent(ScanTypeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}