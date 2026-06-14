package com.example.examportal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView tvError;
    OkHttpClient client = new OkHttpClient();

    // For emulator testing
    String BASE_URL = "http://10.114.190.95:5000/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvError     = findViewById(R.id.tvError);

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvError.setText("Please fill in all fields");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        tvError.setText("");

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/invigilator/login")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        tvError.setText("Connection failed: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            if (response.isSuccessful() && json.has("invigilator")) {
                                JSONObject inv = json.getJSONObject("invigilator");

                                // Save invigilator details
                                SharedPreferences prefs = getSharedPreferences("ExamPortal", MODE_PRIVATE);
                                prefs.edit()
                                        .putString("invigilatorId", inv.getString("invigilator_id"))
                                        .putString("firstName", inv.getString("first_name"))
                                        .putString("lastName", inv.getString("last_name"))
                                        .apply();

                                // Show success for now
                                tvError.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                tvError.setText("Welcome " + inv.getString("first_name") + "!");

                                // Navigate to VenueSelectActivity
                                Intent intent = new Intent(MainActivity.this, VenueSelectActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                tvError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                tvError.setText(json.optString("message", "Invalid credentials"));
                            }
                        } catch (Exception e) {
                            tvError.setText("Something went wrong: " + e.getMessage());
                        }
                    });
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            tvError.setText("Error: " + e.getMessage());
        }
    }
}
