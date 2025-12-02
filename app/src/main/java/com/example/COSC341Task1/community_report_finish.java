package com.example.COSC341Task1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.COSC341Task3.R;

public class community_report_finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_report_finish);

        Button finishButton = findViewById(R.id.finishReportButton);

        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(community_report_finish.this, community_detail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
