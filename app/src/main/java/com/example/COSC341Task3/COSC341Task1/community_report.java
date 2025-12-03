package com.example.COSC341Task3.COSC341Task1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.COSC341Task3.R;

public class community_report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_report);

        CheckBox cbOther = findViewById(R.id.cb_other);
        EditText otherReasonText = findViewById(R.id.other_reason_text);
        Button submitReportButton = findViewById(R.id.submitReportButton);
        ImageButton backBtn = findViewById(R.id.btnCloseReport);

        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                otherReasonText.setVisibility(View.VISIBLE);
            } else {
                otherReasonText.setVisibility(View.GONE);
            }
        });

        submitReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(community_report.this, community_report_finish.class);
            startActivity(intent);
            finish();
        });

        backBtn.setOnClickListener(v ->{
            finish();
        });

    }
}
