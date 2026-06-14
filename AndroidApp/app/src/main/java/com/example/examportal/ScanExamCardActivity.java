package com.example.examportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.activity.result.ActivityResultLauncher;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanExamCardActivity extends AppCompatActivity {

    TextView tvBack, tvVenueHeader;
    TextView tvStudentName, tvStudentId, tvStudentProgram;
    TextView tvCourseName, tvSeatNumber, tvVenue, tvExamDate,tvExamTime;
    TextView tvVenueStatus;
    Button btnScan, btnMarkWritten, btnScanAgain;
    CardView cardStudentInfo;
    ImageView imgStudent;

    OkHttpClient client = new OkHttpClient();
    String BASE_URL      = "http://10.114.190.95:5000/api";
    String selectedVenue;
    String selectedDate;
    String scannedStudentId;

    // ZXing scanner launcher
    private final ActivityResultLauncher<ScanOptions> scanLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedValue = result.getContents();
                    Toast.makeText(this,
                            "QR Value: " + scannedValue,
                            Toast.LENGTH_LONG).show();
                    fetchStudentData(scannedValue);
                } else {
                    Toast.makeText(this,
                            "Scan returned null",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_exam_card);

        selectedVenue   = getIntent().getStringExtra("venue");
        selectedDate    = getIntent().getStringExtra("date");

        tvBack          = findViewById(R.id.tvBack);
        tvVenueHeader   = findViewById(R.id.tvVenueHeader);
        tvStudentName   = findViewById(R.id.tvStudentName);
        tvStudentId     = findViewById(R.id.tvStudentId);
        tvStudentProgram= findViewById(R.id.tvStudentProgram);
        tvCourseName    = findViewById(R.id.tvCourseName);
        tvSeatNumber    = findViewById(R.id.tvSeatNumber);
        tvVenue         = findViewById(R.id.tvVenue);
        tvExamDate      = findViewById(R.id.tvExamDate);
        tvExamTime      = findViewById(R.id.tvExamTime);
        tvVenueStatus   = findViewById(R.id.tvVenueStatus);
        btnScan         = findViewById(R.id.btnScan);
        btnMarkWritten  = findViewById(R.id.btnMarkWritten);
        btnScanAgain    = findViewById(R.id.btnScanAgain);
        cardStudentInfo = findViewById(R.id.cardStudentInfo);
        imgStudent      = findViewById(R.id.imgStudent);

        tvVenueHeader.setText(selectedVenue + "  ·  " + selectedDate);

        // Back button
        tvBack.setOnClickListener(v -> finish());

        // Open QR scanner
        btnScan.setOnClickListener(v -> openScanner());

        // Mark student as written
        btnMarkWritten.setOnClickListener(v -> markStudentWritten());

        // Scan next student
        btnScanAgain.setOnClickListener(v -> {
            cardStudentInfo.setVisibility(View.GONE);
            scannedStudentId = null;
            btnScan.setEnabled(true);
        });
    }

    private void openScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan student exam card QR code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(false);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scanLauncher.launch(options);
    }

    private void fetchStudentData(String studentId) {
        scannedStudentId = studentId;
        btnScan.setEnabled(false);

        String url = BASE_URL
                + "/invigilator/verify-venue/" + studentId
                + "?venue=" + selectedVenue.replace(" ", "%20")
                + "&date="  + selectedDate.replace(" ", "%20");


        Toast.makeText(this, "URL: " + url, Toast.LENGTH_LONG).show();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    btnScan.setEnabled(true);
                    Toast.makeText(ScanExamCardActivity.this,
                            "Connection failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject json    = new JSONObject(body);

                        if (!response.isSuccessful()) {
                            btnScan.setEnabled(true);
                            Toast.makeText(ScanExamCardActivity.this,
                                    json.optString("message",
                                            "Student not found"),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONObject student = json.getJSONObject("student");
                        JSONObject verify  = json.getJSONObject("venueVerification");
                        JSONObject exam    = json.optJSONObject("exam");

// Populate student details
                        tvStudentName.setText(
                                student.optString("first_name", "") + " " +
                                        student.optString("last_name", "")
                        );
                        tvStudentId.setText(
                                "ID: " + student.optString("student_id", "")
                        );
                        tvStudentProgram.setText(
                                student.optString("program", "")
                        );

                        // Load student image
                        String imageUrl = student.optString("image_url", "");
                        if (!imageUrl.isEmpty()) {
                            Glide.with(ScanExamCardActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .fitCenter()
                                    .into(imgStudent);
                        } else {
                            imgStudent.setImageResource(
                                    R.drawable.ic_launcher_foreground
                            );
                        }

                        // Populate exam details if exam found
                        if (exam != null) {
                            tvCourseName.setText(
                                    exam.optString("course_code", "") +
                                            " — " +
                                            exam.optString("course_name", "")
                            );
                            tvSeatNumber.setText(
                                    String.valueOf(exam.optInt("seat_number", 0))
                            );
                            tvVenue.setText(
                                    exam.optString("venue", "")
                            );

                            // Clean exam date
                            String rawDate = exam.optString("exam_date", "");
                            if (rawDate.contains("T")) {
                                rawDate = rawDate.substring(0, 10);
                            }
                            tvExamDate.setText(rawDate);

                            // Display exam time
                            String start = exam.optString("start_time", "");
                            String end   = exam.optString("end_time", "");
                            if (!start.isEmpty() && !end.isEmpty()) {
                                tvExamTime.setText(start + " — " + end);
                            } else {
                                tvExamTime.setText("Not set");
                            }

                        } else {
                            tvCourseName.setText("No exam found");
                            tvSeatNumber.setText("-");
                            tvVenue.setText("-");
                            tvExamDate.setText("-");
                            tvExamTime.setText("-");
                        }
                        // Venue verification
                        boolean match = verify.optBoolean("match", false);
                        tvVenueStatus.setText(verify.optString("message", ""));

                        if (match) {
                            tvVenueStatus.setBackgroundColor(0xFFE8F5E9);
                            tvVenueStatus.setTextColor(0xFF2E7D32);
                            btnMarkWritten.setEnabled(true);
                        } else {
                            tvVenueStatus.setBackgroundColor(0xFFFCE4EC);
                            tvVenueStatus.setTextColor(0xFFC62828);
                            btnMarkWritten.setEnabled(false);
                        }

                        cardStudentInfo.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        btnScan.setEnabled(true);
                        Toast.makeText(ScanExamCardActivity.this,
                                "Error reading data: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void markStudentWritten() {
        SharedPreferences prefs = getSharedPreferences(
                "ExamPortal", MODE_PRIVATE
        );
        String invigilatorId = prefs.getString("invigilatorId", "");

        try {
            JSONObject body = new JSONObject();
            body.put("studentId",     scannedStudentId);
            body.put("invigilatorId", invigilatorId);
            body.put("venue",         selectedVenue);

            RequestBody requestBody = RequestBody.create(
                    body.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/invigilator/mark-written")
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(ScanExamCardActivity.this,
                                    "Failed to update: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            if (response.isSuccessful()) {
                                Toast.makeText(ScanExamCardActivity.this,
                                        "✓ Student marked as Written",
                                        Toast.LENGTH_SHORT).show();
                                btnMarkWritten.setEnabled(false);
                            } else {
                                Toast.makeText(ScanExamCardActivity.this,
                                        json.optString("message",
                                                "Could not update"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ScanExamCardActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
