package com.example.COSC341Task3.Task2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.COSC341Task3.R;

public class CreatePostActivity extends AppCompatActivity {

    private EditText inputUserName;
    private EditText inputText;
    private Button btnPost;
    private boolean isBellOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);  // make sure this XML exists
        ImageView iconMore = findViewById(R.id.iconMore);
        LinearLayout moreMenu = findViewById(R.id.moreMenu);
        ImageView iconClose = findViewById(R.id.iconClose);
        ImageView iconAttach = findViewById(R.id.iconAttach);
        ImageView attachMenu = findViewById(R.id.attachMenu);
        ImageView iconTag = findViewById(R.id.iconTag);
        ImageView tagMenu = findViewById(R.id.tagMenu);
        LinearLayout cardContent = findViewById(R.id.createPostCardContent);
        ImageView iconBell = findViewById(R.id.iconBell);

        View.OnClickListener hideAllMenus = v -> {
            attachMenu.setVisibility(View.GONE);
            tagMenu.setVisibility(View.GONE);
            moreMenu.setVisibility(View.GONE);
        };

        iconAttach.setOnClickListener(v -> {
            // toggle attach
            if (attachMenu.getVisibility() == View.VISIBLE) {
                attachMenu.setVisibility(View.GONE);
            } else {
                attachMenu.setVisibility(View.VISIBLE);
                tagMenu.setVisibility(View.GONE);
                moreMenu.setVisibility(View.GONE);
            }
        });

        iconTag.setOnClickListener(v -> {
            if (tagMenu.getVisibility() == View.VISIBLE) {
                tagMenu.setVisibility(View.GONE);
            } else {
                tagMenu.setVisibility(View.VISIBLE);
                attachMenu.setVisibility(View.GONE);
                moreMenu.setVisibility(View.GONE);
            }
        });
        iconBell.setOnClickListener(v -> {
            isBellOn = !isBellOn;

            if (isBellOn) {
                iconBell.setImageResource(R.drawable.bell_icon);      // normal bell
                iconBell.setAlpha(1.0f);                              // fully visible
            } else {
                iconBell.setImageResource(R.drawable.bell_off_icon);  // muted bell (add this drawable)
                iconBell.setAlpha(0.5f);                              // slightly faded (optional)
            }
        });
        iconMore.setOnClickListener(v -> {
            if (moreMenu.getVisibility() == View.VISIBLE) {
                moreMenu.setVisibility(View.GONE);
            } else {
                moreMenu.setVisibility(View.VISIBLE);
                attachMenu.setVisibility(View.GONE);
                tagMenu.setVisibility(View.GONE);
            }
        });

        cardContent.setOnClickListener(hideAllMenus);
        iconClose.setOnClickListener(v -> {
            hideAllMenus.onClick(v);
            setResult(RESULT_CANCELED);
            finish();
        });

        inputUserName = findViewById(R.id.inputUserName);
        inputText = findViewById(R.id.inputText);
        btnPost = findViewById(R.id.btnPost);
        btnPost.setEnabled(false);  // start disabled

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePostButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };


        inputUserName.addTextChangedListener(watcher);
        inputText.addTextChangedListener(watcher);


        btnPost.setOnClickListener(v -> {
            String userName = inputUserName.getText().toString().trim();
            String text = inputText.getText().toString().trim();

            if (userName.isEmpty() || text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            hideAllMenus.onClick(v);   // close any open dropdowns

            Intent data = new Intent();
            data.putExtra("userName", userName);
            data.putExtra("text", text);
            data.putExtra("notificationsOn", isBellOn);
            setResult(RESULT_OK, data);
            finish();
        });
    }
    private void updatePostButtonState() {
        String userName = inputUserName.getText().toString().trim();
        String text = inputText.getText().toString().trim();

        boolean canPost = !userName.isEmpty() && !text.isEmpty();
        btnPost.setEnabled(canPost);
    }
}
