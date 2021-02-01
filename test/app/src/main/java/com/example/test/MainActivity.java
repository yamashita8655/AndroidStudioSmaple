package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitMainActivityEvent();
    }

    private void InitMainActivityEvent() {
        final Button testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Log.d("debug", "button1, Perform action on click");
                final TextView text = findViewById(R.id.TestText);
                text.setText("OnClick!!");
            }
        });

        final Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final EditText edit = findViewById(R.id.SaveEdit);

                // 値の設定
                SharedPreferences sharedPref = getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("SAVE_KEY", edit.getText().toString());
                editor.commit();
            }
        });

        final Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*// 設定ファイルを開きます。
                SharedPreferences sharedPref = getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);

                // 値の取得
                String strVal = sharedPref.getString("SAVE_KEY", ""); // 既定値 123 を設定

                final TextView text = findViewById(R.id.TestText);
                text.setText(strVal);*/
                displaySpeechRecognizer();
            }
        });

        final Button goToCurrentProgressButton = findViewById(R.id.GoToCurrentProgressButton);
        goToCurrentProgressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setContentView(R.layout.current_progress_view);
                InitCurrentProgressActivityEvent();
            }
        });
    }

    private void InitCurrentProgressActivityEvent() {
        final Button currentProgressGoToTitleButton = findViewById(R.id.CurrentProgressGoToTitleButton);
        currentProgressGoToTitleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                InitMainActivityEvent();
            }
        });
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            final TextView text = findViewById(R.id.TestText);
            text.setText(spokenText);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}