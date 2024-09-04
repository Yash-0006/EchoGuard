package com.example.echoguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {
    private EditText codeWordEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        codeWordEditText = findViewById(R.id.code_word_edit_text);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeWord = codeWordEditText.getText().toString();
                // Save code word to database
                Database database = new Database(SettingsActivity.this);
                database.addCodeWord(codeWord);
            }
        });
    }
}