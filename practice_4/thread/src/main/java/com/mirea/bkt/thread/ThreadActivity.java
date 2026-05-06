package com.mirea.bkt.thread;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.bkt.thread.databinding.ActivityThreadBinding;

import java.util.Arrays;
import java.util.Locale;

public class ThreadActivity extends AppCompatActivity {
    private static final String TAG = ThreadActivity.class.getSimpleName();
    private ActivityThreadBinding binding;
    private int backgroundThreadCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThreadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonShowMainThread.setOnClickListener(v -> showMainThreadInfo());
        binding.buttonRunOnMain.setOnClickListener(v -> runLongTaskOnMainThread());
        binding.buttonRunInBackground.setOnClickListener(v -> runLongTaskInBackground());
        binding.buttonCalculate.setOnClickListener(v -> calculateAveragePairs());
    }

    private void showMainThreadInfo() {
        Thread mainThread = Thread.currentThread();
        String oldName = mainThread.getName();
        mainThread.setName("MireaMainThread");

        String info = getString(
                R.string.thread_info_template,
                oldName,
                mainThread.getName(),
                String.valueOf(mainThread.getPriority()),
                String.valueOf(mainThread.isAlive()),
                String.valueOf(mainThread.getThreadGroup())
        );

        binding.textViewThreadInfo.setText(info);
        Log.d(TAG, "Main thread stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.d(TAG, "Main thread group: " + mainThread.getThreadGroup());
    }

    private void runLongTaskOnMainThread() {
        binding.textViewThreadInfo.setText(R.string.main_thread_started);

        long endTime = System.currentTimeMillis() + 4000;
        while (System.currentTimeMillis() < endTime) {
            SystemClock.sleep(200);
        }

        binding.textViewThreadInfo.setText(R.string.main_thread_finished);
    }

    private void runLongTaskInBackground() {
        int threadNumber = ++backgroundThreadCounter;
        binding.textViewThreadInfo.setText(getString(R.string.background_started_ui, threadNumber));

        Runnable runnable = () -> {
            Log.d(TAG, getString(R.string.background_log_started, threadNumber));

            long endTime = System.currentTimeMillis() + 4000;
            while (System.currentTimeMillis() < endTime) {
                SystemClock.sleep(200);
            }

            Log.d(TAG, getString(R.string.background_log_finished, threadNumber));
            runOnUiThread(() -> binding.textViewThreadInfo.setText(
                    getString(R.string.background_finished_ui, threadNumber)
            ));
        };

        new Thread(runnable, "Worker-" + threadNumber).start();
    }

    private void calculateAveragePairs() {
        String totalPairsText = binding.editTextTotalPairs.getText().toString().trim();
        String studyDaysText = binding.editTextStudyDays.getText().toString().trim();

        if (totalPairsText.isEmpty() || studyDaysText.isEmpty()) {
            binding.textViewResult.setText(getString(R.string.result_fill_fields));
            return;
        }

        int totalPairs;
        int studyDays;

        try {
            totalPairs = Integer.parseInt(totalPairsText);
            studyDays = Integer.parseInt(studyDaysText);
        } catch (NumberFormatException exception) {
            binding.textViewResult.setText(getString(R.string.result_invalid_number));
            return;
        }

        if (studyDays <= 0) {
            binding.textViewResult.setText(getString(R.string.result_days_error));
            return;
        }

        binding.textViewResult.setText(getString(R.string.result_calculating));

        final int pairs = totalPairs;
        final int days = studyDays;

        Runnable calculationRunnable = () -> {
            double average = (double) pairs / days;
            String result = getString(
                    R.string.result_value,
                    String.format(Locale.US, "%.2f", average)
            );
            Log.d(TAG, "Average pairs per day = " + average);

            runOnUiThread(() -> binding.textViewResult.setText(result));
        };

        new Thread(calculationRunnable, "AverageCalculator").start();
    }
}
