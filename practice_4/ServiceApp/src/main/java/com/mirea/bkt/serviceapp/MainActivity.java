package com.mirea.bkt.serviceapp;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mirea.bkt.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 200;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        checkNotificationPermission();

        binding.buttonStart.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
            ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
            binding.textViewStatus.setText(R.string.status_started);
        });

        binding.buttonStop.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, PlayerService.class));
            binding.textViewStatus.setText(R.string.status_stopped);
        });
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Log.d(MainActivity.class.getSimpleName(), "Runtime notification permission is not required");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.class.getSimpleName(), "Разрешения получены");
        } else {
            Log.d(MainActivity.class.getSimpleName(), "Нет разрешений!");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{POST_NOTIFICATIONS},
                    PERMISSION_CODE
            );
        }
    }
}
