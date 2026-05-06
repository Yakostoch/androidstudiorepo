package com.mirea.bkt.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.bkt.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + result);
                binding.textViewResult.setText(result);
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextAge.setText("");
        binding.editTextProfession.setText("");

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToLooper();
            }
        });
    }

    private void sendMessageToLooper() {
        String ageText = binding.editTextAge.getText().toString().trim();
        String profession = binding.editTextProfession.getText().toString().trim();

        if (ageText.isEmpty() || profession.isEmpty()) {
            binding.textViewResult.setText(R.string.fill_all_fields);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException exception) {
            binding.textViewResult.setText(R.string.invalid_age);
            return;
        }

        if (age <= 0) {
            binding.textViewResult.setText(R.string.invalid_age);
            return;
        }

        if (myLooper.mHandler == null) {
            binding.textViewResult.setText(R.string.looper_not_ready);
            return;
        }

        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("age", age);
        bundle.putString("profession", profession);
        msg.setData(bundle);

        binding.textViewResult.setText(getString(R.string.task_sent, age));
        myLooper.mHandler.sendMessage(msg);
    }
}
