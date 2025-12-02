package com.example.COSC341Task3.task4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class adminReportPage extends AppCompatActivity {

    private Spinner spinner;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_report_page1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvUser = findViewById(R.id.textView7);
        TextView tvPost = findViewById(R.id.textView8);

        spinner = findViewById(R.id.spinner);
        doneButton = findViewById(R.id.button2);

        ImageView backArrow = findViewById(R.id.imageView3);
        ImageView closeIcon = findViewById(R.id.imageView2);

        Intent fromUser = getIntent();
        String username = fromUser.getStringExtra("username");
        String postText = fromUser.getStringExtra("postText");
        String reason   = fromUser.getStringExtra("reason"); // opcional

        if (username != null && !username.isEmpty()) {
            tvUser.setText("Username: " + username);
        } else {
            tvUser.setText("Username: -");
        }

        StringBuilder postBuilder = new StringBuilder();
        postBuilder.append("Post:\n");

        if (postText != null && !postText.isEmpty()) {
            postBuilder.append(postText);
        } else {
            postBuilder.append("-");
        }

        if (reason != null && !reason.isEmpty()) {
            postBuilder.append("\n\nReason: ").append(reason);
        }

        tvPost.setText(postBuilder.toString());

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = spinner.getSelectedItemPosition();

                if (pos == 0) {
                    Toast.makeText(
                            adminReportPage.this,
                            "Please select an action first.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                String selectedOption = spinner.getSelectedItem().toString();

                Toast.makeText(
                        adminReportPage.this,
                        "Action applied: " + selectedOption,
                        Toast.LENGTH_SHORT
                ).show();

                Intent homeIntent = new Intent(adminReportPage.this, Homepage.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(adminReportPage.this, Homepage.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
