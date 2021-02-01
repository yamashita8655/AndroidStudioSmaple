package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                // 設定ファイルを開きます。
                SharedPreferences sharedPref = getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);

                // 値の取得
                String strVal = sharedPref.getString("SAVE_KEY", ""); // 既定値 123 を設定

                final TextView text = findViewById(R.id.TestText);
                text.setText(strVal);
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
}