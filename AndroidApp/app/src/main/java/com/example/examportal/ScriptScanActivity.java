package com.example.examportal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.activity.result.ActivityResultLauncher;
import com.bumptech.glide.Glide;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScriptScanActivity extends AppCompatActivity {

    TextView tvBack, tvCourseHeader;
    TextView tvStudentName, tvStudentId, tvStudentProgram;
    TextView tvCourseName, tvSeatNumber;
    TextView tvScriptStatus, tvScriptNumber;
    Button btnScanScript;
    CardView cardScriptResult;
    ImageView imgStudent;

    OkHttpClient client = new OkHttpClient();
    String BASE_URL     = "http://10.114.190.95:5000/api";

    String studentId;
    String courseCode;
    String courseName;
    String selectedVenue;
    boolean hasScript;

    // ZXing scanner
    private final ActivityResultLauncher<ScanOptions> scanLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedValue = result.getContents().trim();
                    saveScriptNumber(scannedValue);
                } else {
                    Toast.makeText(this,
                            "Scan cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_scan);

        // Get data from intent
        studentId     = getIntent().getStringExtra("studentId");
        courseCode    = getIntent().getStringExtra("courseCode");
        courseName    = getIntent().getStringExtra("courseName");
        selectedVenue = getIntent().getStringExtra("venue");
        hasScript     = getIntent().getBooleanExtra("hasScript", false);

        String firstName    = getIntent().getStringExtra("firstName");
        String lastName     = getIntent().getStringExtra("lastName");
        String program      = getIntent().getStringExtra("program");
        String imageUrl     = getIntent().getStringExtra("imageUrl");
        int    seatNumber   = getIntent().getIntExtra("seatNumber", 0);
        String scriptNumber = getIntent().getStringExtra("scriptNumber");

        tvBack          = findViewById(R.id.tvBack);
        tvCourseHeader  = findViewById(R.id.tvCourseHeader);
        tvStudentName   = findViewById(R.id.tvStudentName);
        tvStudentId     = findViewById(R.id.tvStudentId);
        tvStudentProgram= findViewById(R.id.tvStudentProgram);
        tvCourseName    = findViewById(R.id.tvCourseName);
        tvSeatNumber    = findViewById(R.id.tvSeatNumber);
        tvScriptStatus  = findViewById(R.id.tvScriptStatus);
        tvScriptNumber  = findViewById(R.id.tvScriptNumber);
        btnScanScript   = findViewById(R.id.btnScanScript);
        cardScriptResult= findViewById(R.id.cardScriptResult);
        imgStudent      = findViewById(R.id.imgStudent);

        // Populate header
        tvCourseHeader.setText(courseCode + " — " + courseName);

        // Populate student profile
        tvStudentName.setText(firstName + " " + lastName);
        tvStudentId.setText("ID: " + studentId);
        tvStudentProgram.setText(program);
        tvCourseName.setText(courseCode + " — " + courseName);
        tvSeatNumber.setText(String.valueOf(seatNumber));

        // Load student image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .fitCenter()
                    .into(imgStudent);
        }

        // Check if script already collected
        if (hasScript && scriptNumber != null &&
                !scriptNumber.equals("null") && !scriptNumber.isEmpty()) {
            tvScriptStatus.setText("✓ Script already collected");
            tvScriptStatus.setBackgroundColor(0xFFE8F5E9);
            tvScriptStatus.setTextColor(0xFF2E7D32);
            tvScriptNumber.setText(scriptNumber);
            cardScriptResult.setVisibility(View.VISIBLE);
            btnScanScript.setEnabled(false);
            btnScanScript.setText("Script Already Collected");
        } else {
            tvScriptStatus.setText("Script not yet collected");
            tvScriptStatus.setBackgroundColor(0xFFFFF3E0);
            tvScriptStatus.setTextColor(0xFFE65100);
        }

        tvBack.setOnClickListener(v -> finish());
        btnScanScript.setOnClickListener(v -> openScanner());
    }

    private void openScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan script QR code number");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(false);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scanLauncher.launch(options);
    }

    private void saveScriptNumber(String scriptNumber) {
        btnScanScript.setEnabled(false);

        try {
            JSONObject body = new JSONObject();
            body.put("studentId",    studentId);
            body.put("courseCode",   courseCode);
            body.put("scriptNumber", scriptNumber);

            RequestBody requestBody = RequestBody.create(
                    body.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/invigilator/save-script")
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        btnScanScript.setEnabled(true);
                        Toast.makeText(ScriptScanActivity.this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    String respBody = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(respBody);
                            if (response.isSuccessful()) {
                                // Show success
                                tvScriptNumber.setText(scriptNumber);
                                cardScriptResult.setVisibility(View.VISIBLE);
                                tvScriptStatus.setText(
                                        "✓ Script collected"
                                );
                                tvScriptStatus.setBackgroundColor(0xFFE8F5E9);
                                tvScriptStatus.setTextColor(0xFF2E7D32);
                                btnScanScript.setText("Script Collected ✓");

                                Toast.makeText(ScriptScanActivity.this,
                                        "Script #" + scriptNumber +
                                                " saved successfully",
                                        Toast.LENGTH_SHORT).show();

                                // Return to student list after 2 seconds
                                tvBack.postDelayed(() -> finish(), 2000);

                            } else {
                                btnScanScript.setEnabled(true);
                                Toast.makeText(ScriptScanActivity.this,
                                        json.optString("message",
                                                "Could not save script"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            btnScanScript.setEnabled(true);
                            Toast.makeText(ScriptScanActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            btnScanScript.setEnabled(true);
            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}