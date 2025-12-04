package com.example.COSC341Task3.task4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.COSC341Task3.Homepage;
import com.example.COSC341Task3.R;

public class ReportUserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_user_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvUser = findViewById(R.id.textView6);
        TextView tvPost = findViewById(R.id.textView5);

        String username = getIntent().getStringExtra("username");
        String postText = getIntent().getStringExtra("postText");

        if (username != null) {
            tvUser.setText("Username: " + username);
        }

        if (postText != null) {
            tvPost.setText("Post:\n" + postText);
        }

        Spinner spinner = findViewById(R.id.spinner2);
        Button doneButton = findViewById(R.id.button);

        doneButton.setOnClickListener(v -> {
            int pos = spinner.getSelectedItemPosition();

            if (pos == 0) {
                Toast.makeText(
                        ReportUserPage.this,
                        "Please select a reason before submitting.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            //why was the report made
            String reason = spinner.getSelectedItem().toString();

            Toast.makeText(
                    ReportUserPage.this,
                    "Your report has been submitted.",
                    Toast.LENGTH_SHORT
            ).show();

            // Return to homepage
            Intent intent = new Intent(ReportUserPage.this, Homepage.class);
            intent.putExtra("username", username);
            intent.putExtra("postText", postText);
            intent.putExtra("reason", reason);


            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.imageView).setOnClickListener(v -> {
            finish();
        });
    }
}
