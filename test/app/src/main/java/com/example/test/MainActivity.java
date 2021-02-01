package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int MILLIS_IN_FUTURE = 11 * 500;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private TextView textView;

    private int count = 5;
    private TextView countTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // permission チェック
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                // 拒否した場合
            } else {
                // 許可した場合
                int MY_PERMISSIONS_RECORD_AUDIO = 1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString());

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        InitMainActivityEvent();
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            final TextView text = findViewById(R.id.TestText);
            text.setText("準備できた");
        }

        @Override
        public void onBeginningOfSpeech() {
            final TextView text = findViewById(R.id.TestText);
            text.setText("喋って");
        }

        @Override
        public void onRmsChanged(float v) {
            final TextView text = findViewById(R.id.TestText);
            text.setText("音声変わった");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            final TextView text = findViewById(R.id.TestText);
            text.setText("新しい音声");
        }

        @Override
        public void onEndOfSpeech() {
            final TextView text = findViewById(R.id.TestText);
            text.setText("終わった");
        }

        @Override
        public void onError(int i) {
            final TextView text = findViewById(R.id.TestText);
            switch (i) {
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    text.setText("ネットワークエラー");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    text.setText("その他ネットワークエラー");
                    break;
                case SpeechRecognizer.ERROR_AUDIO:
                    text.setText("Audioエラー");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    text.setText("サーバーエラー");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    text.setText("クライアントエラー");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    text.setText("タイムアウト");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    text.setText("結果が見つからない");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    text.setText("今サービスが使えません");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    text.setText("パーミッションエラー");
                    break;
            }
        }

        @Override
        public void onResults(Bundle bundle) {
            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] result = new String[0];
            if (mResult != null) {
                result = new String[mResult.size()];
            }
            if (mResult != null) {
                mResult.toArray(result);
            }

            // テキスト比較
/*            if (TextUtils.equals(result[0], "メリークリスマス")) {
                Toast.makeText(MainActivity.this, "あなたもね！！", Toast.LENGTH_SHORT).show();
                countDownTimer.cancel();
                countTextView.setText("");
            }*/

            final TextView text = findViewById(R.id.TestText);
            text.setText(result[0]);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };


    protected void onDestroy() {
        super.onDestroy();
        mRecognizer.destroy();
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
                //displaySpeechRecognizer();

                // レコーディングスタート
                mRecognizer.startListening(intent);
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