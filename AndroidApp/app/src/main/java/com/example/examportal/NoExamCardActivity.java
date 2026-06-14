package com.example.examportal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoExamCardActivity extends AppCompatActivity {

    TextView tvBack, tvVenueHeader;
    EditText etStudentId;
    Button btnFindStudent, btnMarkWritten, btnFindNext;
    CardView cardStudentInfo;
    TextView tvStudentName, tvStudentId, tvStudentProgram;
    TextView tvCourseName, tvSeatNumber, tvVenue;
    TextView tvExamDate, tvExamTime, tvVenueStatus;
    ImageView imgStudent;

    OkHttpClient client = new OkHttpClient();
    String BASE_URL     = "http://10.114.190.95:5000/api";
    String selectedVenue;
    String selectedDate;
    String foundStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_exam_card);

        selectedVenue   = getIntent().getStringExtra("venue");
        selectedDate    = getIntent().getStringExtra("date");

        tvBack          = findViewById(R.id.tvBack);
        tvVenueHeader   = findViewById(R.id.tvVenueHeader);
        etStudentId     = findViewById(R.id.etStudentId);
        btnFindStudent  = findViewById(R.id.btnFindStudent);
        btnMarkWritten  = findViewById(R.id.btnMarkWritten);
        btnFindNext     = findViewById(R.id.btnFindNext);
        cardStudentInfo = findViewById(R.id.cardStudentInfo);
        tvStudentName   = findViewById(R.id.tvStudentName);
        tvStudentId     = findViewById(R.id.tvStudentId);
        tvStudentProgram= findViewById(R.id.tvStudentProgram);
        tvCourseName    = findViewById(R.id.tvCourseName);
        tvSeatNumber    = findViewById(R.id.tvSeatNumber);
        tvVenue         = findViewById(R.id.tvVenue);
        tvExamDate      = findViewById(R.id.tvExamDate);
        tvExamTime      = findViewById(R.id.tvExamTime);
        tvVenueStatus   = findViewById(R.id.tvVenueStatus);
        imgStudent      = findViewById(R.id.imgStudent);

        tvVenueHeader.setText(selectedVenue + "  ·  " + selectedDate);

        tvBack.setOnClickListener(v -> finish());

        btnFindStudent.setOnClickListener(v -> {
            String id = etStudentId.getText().toString().trim();
            if (id.isEmpty()) {
                Toast.makeText(this,
                        "Please enter a student ID",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            findStudent(id);
        });

        btnMarkWritten.setOnClickListener(v -> markStudentWritten());

        btnFindNext.setOnClickListener(v -> {
            etStudentId.setText("");
            cardStudentInfo.setVisibility(View.GONE);
            foundStudentId = null;
            btnFindStudent.setEnabled(true);
        });
    }

    private void findStudent(String studentId) {
        foundStudentId = studentId;
        btnFindStudent.setEnabled(false);
        cardStudentInfo.setVisibility(View.GONE);

        String url = BASE_URL
                + "/invigilator/verify-venue/" + studentId
                + "?venue=" + selectedVenue.replace(" ", "%20")
                + "&date="  + selectedDate.replace(" ", "%20");

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    btnFindStudent.setEnabled(true);
                    Toast.makeText(NoExamCardActivity.this,
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
                        JSONObject json = new JSONObject(body);

                        if (!response.isSuccessful()) {
                            btnFindStudent.setEnabled(true);
                            Toast.makeText(NoExamCardActivity.this,
                                    json.optString("message",
                                            "Student not found"),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONObject student = json.getJSONObject("student");
                        JSONObject verify  = json.getJSONObject(
                                "venueVerification"
                        );
                        JSONObject exam    = json.optJSONObject("exam");

                        // Student details
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

                        // Load student image with Glide
                        String imageUrl = student.optString("image_url", "");
                        if (!imageUrl.isEmpty()) {
                            Glide.with(NoExamCardActivity.this)
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

                        // Exam details
                        if (exam != null) {
                            tvCourseName.setText(
                                    exam.optString("course_code", "") +
                                            " — " +
                                            exam.optString("course_name", "")
                            );
                            tvSeatNumber.setText(
                                    String.valueOf(
                                            exam.optInt("seat_number", 0)
                                    )
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

                            // Exam time
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
                        tvVenueStatus.setText(
                                verify.optString("message", "")
                        );
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
                        btnFindStudent.setEnabled(true);

                    } catch (Exception e) {
                        btnFindStudent.setEnabled(true);
                        Toast.makeText(NoExamCardActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
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
            body.put("studentId",     foundStudentId);
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
                            Toast.makeText(NoExamCardActivity.this,
                                    "Failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    String respBody = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(respBody);
                            if (response.isSuccessful()) {
                                Toast.makeText(NoExamCardActivity.this,
                                        "✓ Student marked as Written",
                                        Toast.LENGTH_SHORT).show();
                                btnMarkWritten.setEnabled(false);
                            } else {
                                Toast.makeText(NoExamCardActivity.this,
                                        json.optString("message",
                                                "Could not update"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(NoExamCardActivity.this,
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}