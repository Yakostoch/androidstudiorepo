package com.example.activitylifecycle;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String TAG = "LIFECYCLE";;
    private EditText editText;
    private void showState(String message) {
        Log.d(TAG, message);      // вывод в Logcat
        editText.setText(message); // вывод в EditText
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        showState("onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        showState("onStart");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        showState("onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showState("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showState("onResume");
    }
}