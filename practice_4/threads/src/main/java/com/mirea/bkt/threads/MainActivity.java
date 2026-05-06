package com.mirea.bkt.threads;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.bkt.threads.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Thread mainThread;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainThread = Thread.currentThread();
        binding.textView.setText(getString(R.string.current_thread_name, mainThread.getName()));

        mainThread.setName("ГРУППА: БСБО-XX-XX, НОМЕР: XX, ФИЛЬМ: XX");
        binding.textView.append("\n" + getString(R.string.new_thread_name, mainThread.getName()));
        binding.textView.append("\n" + getString(R.string.thread_priority, mainThread.getPriority()));
        binding.textView.append("\n" + getString(R.string.thread_alive, mainThread.isAlive()));

        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d(
                                "ThreadProject",
                                String.format(
                                        "Запущен поток № %d студентом группы № %s номер по списку № %d",
                                        numberThread,
                                        "БСБО-XX-XX",
                                        -1
                                )
                        );

                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());
                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);

                        runOnUiThread(() -> binding.textView.append(
                                "\n" + getString(R.string.thread_finished, numberThread)
                        ));
                    }
                }).start();
            }
        });
    }
}
