package com.mirea.bkt.workmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.mirea.bkt.workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textViewStatus.setText(R.string.status_ready);

        binding.buttonStartWorker.setOnClickListener(v -> startWorker());
    }

    private void startWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build();

        OneTimeWorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
        binding.textViewStatus.setText(R.string.status_enqueued);

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(this, workInfo -> updateStatus(workInfo));
    }

    private void updateStatus(WorkInfo workInfo) {
        if (workInfo == null) {
            return;
        }

        switch (workInfo.getState()) {
            case ENQUEUED:
                binding.textViewStatus.setText(R.string.status_enqueued);
                break;
            case RUNNING:
                binding.textViewStatus.setText(R.string.status_running);
                break;
            case SUCCEEDED:
                binding.textViewStatus.setText(R.string.status_success);
                break;
            case FAILED:
                binding.textViewStatus.setText(R.string.status_failed);
                break;
            case BLOCKED:
                binding.textViewStatus.setText(R.string.status_blocked);
                break;
            case CANCELLED:
                binding.textViewStatus.setText(R.string.status_cancelled);
                break;
        }
    }
}
