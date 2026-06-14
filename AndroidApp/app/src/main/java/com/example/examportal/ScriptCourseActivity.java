package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScriptCourseActivity extends AppCompatActivity {

    TextView tvBack, tvVenueHeader;
    LinearLayout layoutCourses;
    ProgressBar progressBar;

    OkHttpClient client = new OkHttpClient();
    String BASE_URL     = "http://10.114.190.95:5000/api";
    String selectedVenue;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_course);

        selectedVenue  = getIntent().getStringExtra("venue");
        selectedDate   = getIntent().getStringExtra("date");

        tvBack         = findViewById(R.id.tvBack);
        tvVenueHeader  = findViewById(R.id.tvVenueHeader);
        layoutCourses  = findViewById(R.id.layoutCourses);
        progressBar    = findViewById(R.id.progressBar);

        tvVenueHeader.setText(selectedVenue + "  ·  " + selectedDate);
        tvBack.setOnClickListener(v -> finish());

        fetchCourses();
    }

    private void fetchCourses() {
        progressBar.setVisibility(View.VISIBLE);

        String url = BASE_URL
                + "/invigilator/script-courses"
                + "?venue=" + selectedVenue.replace(" ", "%20")
                + "&date="  + selectedDate.replace(" ", "%20");

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ScriptCourseActivity.this,
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
                        JSONObject json    = new JSONObject(body);
                        JSONArray  courses = json.getJSONArray("courses");

                        if (courses.length() == 0) {
                            Toast.makeText(ScriptCourseActivity.this,
                                    "No courses found for this venue and date",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        layoutCourses.removeAllViews();

                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject course = courses.getJSONObject(i);
                            String courseCode = course.optString("course_code");
                            String courseName = course.optString("course_name");
                            addCourseCard(courseCode, courseName);
                        }

                    } catch (Exception e) {
                        Toast.makeText(ScriptCourseActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addCourseCard(String courseCode, String courseName) {
        // Create card programmatically
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setRadius(24f);
        card.setCardElevation(6f);
        card.setCardBackgroundColor(0xFFFFFFFF);

        // Inner layout
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setGravity(android.view.Gravity.CENTER_VERTICAL);
        inner.setPadding(40, 32, 40, 32);

        // Course text layout
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        textLayout.setLayoutParams(textParams);

        // Course code
        TextView tvCode = new TextView(this);
        tvCode.setText(courseCode);
        tvCode.setTextSize(16f);
        tvCode.setTypeface(null, android.graphics.Typeface.BOLD);
        tvCode.setTextColor(0xFF1A1A2E);

        // Course name
        TextView tvName = new TextView(this);
        tvName.setText(courseName);
        tvName.setTextSize(13f);
        tvName.setTextColor(0xFF888888);
        tvName.setPadding(0, 4, 0, 0);

        // Arrow
        TextView tvArrow = new TextView(this);
        tvArrow.setText("›");
        tvArrow.setTextSize(24f);
        tvArrow.setTextColor(0xFF1A1A2E);

        textLayout.addView(tvCode);
        textLayout.addView(tvName);
        inner.addView(textLayout);
        inner.addView(tvArrow);
        card.addView(inner);

        // Click listener
        card.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ScriptCourseActivity.this,
                    ScriptStudentActivity.class
            );
            intent.putExtra("courseCode", courseCode);
            intent.putExtra("courseName", courseName);
            intent.putExtra("venue",      selectedVenue);
            intent.putExtra("date",       selectedDate);
            startActivity(intent);
        });

        layoutCourses.addView(card);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}