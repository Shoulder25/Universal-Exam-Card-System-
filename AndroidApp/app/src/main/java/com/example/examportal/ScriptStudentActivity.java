package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

public class ScriptStudentActivity extends AppCompatActivity {

    TextView tvBack, tvCourseHeader, tvVenueHeader, tvStudentCount;
    EditText etSearch;
    LinearLayout layoutStudents;
    ProgressBar progressBar;

    OkHttpClient client  = new OkHttpClient();
    String BASE_URL      = "http://10.114.190.95:5000/api";
    String selectedVenue;
    String selectedDate;
    String courseCode;
    String courseName;

    // Store all students for filtering
    List<JSONObject> allStudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_student);

        selectedVenue  = getIntent().getStringExtra("venue");
        selectedDate   = getIntent().getStringExtra("date");
        courseCode     = getIntent().getStringExtra("courseCode");
        courseName     = getIntent().getStringExtra("courseName");

        tvBack         = findViewById(R.id.tvBack);
        tvCourseHeader = findViewById(R.id.tvCourseHeader);
        tvVenueHeader  = findViewById(R.id.tvVenueHeader);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        etSearch       = findViewById(R.id.etSearch);
        layoutStudents = findViewById(R.id.layoutStudents);
        progressBar    = findViewById(R.id.progressBar);

        tvCourseHeader.setText(courseCode + " — " + courseName);
        tvVenueHeader.setText(selectedVenue + "  ·  " + selectedDate);
        tvBack.setOnClickListener(v -> finish());

        // Real time search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fetchStudents();
    }

    private void fetchStudents() {
        progressBar.setVisibility(View.VISIBLE);

        String url = BASE_URL
                + "/invigilator/script-students"
                + "?courseCode=" + courseCode
                + "&venue="      + selectedVenue.replace(" ", "%20");

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ScriptStudentActivity.this,
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
                        progressBar.setVisibility(View.GONE);
                        JSONObject json     = new JSONObject(body);
                        JSONArray  students = json.getJSONArray("students");

                        allStudents.clear();
                        for (int i = 0; i < students.length(); i++) {
                            allStudents.add(students.getJSONObject(i));
                        }

                        tvStudentCount.setText(
                                allStudents.size() + " student(s) found"
                        );
                        renderStudents(allStudents);

                    } catch (Exception e) {
                        Toast.makeText(ScriptStudentActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void filterStudents(String query) {
        if (query.isEmpty()) {
            renderStudents(allStudents);
            return;
        }

        List<JSONObject> filtered = new ArrayList<>();
        for (JSONObject student : allStudents) {
            try {
                String fullName  = student.optString("first_name", "") +
                        " " + student.optString("last_name", "");
                String studentId = student.optString("student_id", "");

                if (fullName.toLowerCase().contains(query.toLowerCase()) ||
                        studentId.contains(query)) {
                    filtered.add(student);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tvStudentCount.setText(filtered.size() + " student(s) found");
        renderStudents(filtered);
    }

    private void renderStudents(List<JSONObject> students) {
        layoutStudents.removeAllViews();

        for (JSONObject student : students) {
            try {
                String studentId    = student.optString("student_id", "");
                String firstName    = student.optString("first_name", "");
                String lastName     = student.optString("last_name", "");
                String scriptNumber = student.optString("script_qr_code", "");
                boolean hasScript   = !scriptNumber.equals("null") &&
                        !scriptNumber.isEmpty();

                // Card
                CardView card = new CardView(this);
                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                cardParams.setMargins(0, 0, 0, 20);
                card.setLayoutParams(cardParams);
                card.setRadius(20f);
                card.setCardElevation(4f);
                card.setCardBackgroundColor(
                        hasScript ? 0xFFE8F5E9 : 0xFFFFFFFF
                );

                // Inner layout
                LinearLayout inner = new LinearLayout(this);
                inner.setOrientation(LinearLayout.HORIZONTAL);
                inner.setGravity(android.view.Gravity.CENTER_VERTICAL);
                inner.setPadding(36, 28, 36, 28);

                // Text layout
                LinearLayout textLayout = new LinearLayout(this);
                textLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams textParams =
                        new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                        );
                textLayout.setLayoutParams(textParams);

                // Full name
                TextView tvName = new TextView(this);
                tvName.setText(firstName + " " + lastName);
                tvName.setTextSize(15f);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                tvName.setTextColor(0xFF1A1A2E);

                // Student ID
                TextView tvId = new TextView(this);
                tvId.setText(studentId);
                tvId.setTextSize(12f);
                tvId.setTextColor(0xFF888888);
                tvId.setPadding(0, 4, 0, 0);

                // Script status
                TextView tvScript = new TextView(this);
                if (hasScript) {
                    tvScript.setText("✓ Script #" + scriptNumber + " collected");
                    tvScript.setTextColor(0xFF2E7D32);
                } else {
                    tvScript.setText("Script not yet collected");
                    tvScript.setTextColor(0xFFE65100);
                }
                tvScript.setTextSize(11f);
                tvScript.setPadding(0, 4, 0, 0);

                // Arrow
                TextView tvArrow = new TextView(this);
                tvArrow.setText("›");
                tvArrow.setTextSize(24f);
                tvArrow.setTextColor(hasScript ? 0xFF2E7D32 : 0xFF1A1A2E);

                textLayout.addView(tvName);
                textLayout.addView(tvId);
                textLayout.addView(tvScript);
                inner.addView(textLayout);
                inner.addView(tvArrow);
                card.addView(inner);

                // Click — go to script scan screen
                final JSONObject finalStudent = student;
                card.setOnClickListener(v -> {
                    Intent intent = new Intent(
                            ScriptStudentActivity.this,
                            ScriptScanActivity.class
                    );
                    intent.putExtra("studentId",   studentId);
                    intent.putExtra("firstName",   firstName);
                    intent.putExtra("lastName",    lastName);
                    intent.putExtra("courseCode",  courseCode);
                    intent.putExtra("courseName",  courseName);
                    intent.putExtra("venue",       selectedVenue);
                    intent.putExtra("date",        selectedDate);
                    intent.putExtra("hasScript",   hasScript);
                    intent.putExtra("scriptNumber", scriptNumber);
                    try {
                        intent.putExtra("program",
                                finalStudent.optString("program", ""));
                        intent.putExtra("imageUrl",
                                finalStudent.optString("image_url", ""));
                        intent.putExtra("seatNumber",
                                finalStudent.optInt("seat_number", 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                });

                layoutStudents.addView(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Refresh list when returning from ScriptScanActivity
    @Override
    protected void onResume() {
        super.onResume();
        fetchStudents();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}