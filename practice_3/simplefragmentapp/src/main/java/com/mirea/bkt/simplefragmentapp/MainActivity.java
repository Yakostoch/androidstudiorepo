package com.mirea.bkt.simplefragmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment1, fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Настройка отступов (важно иметь android:id="@+id/main" в обоих XML)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        // 1. Пытаемся найти кнопки (они есть только в портретном режиме)
        Button btnFirstFragment = findViewById(R.id.btnFirstFragment);
        Button btnSecondFragment = findViewById(R.id.btnSecondFragment);

        // 2. Если кнопки найдены — значит мы в ПОРТРЕТНОМ режиме
        if (btnFirstFragment != null && btnSecondFragment != null) {

            fragment1 = new FirstFragment();
            fragment2 = new SecondFragment();

            // Показываем первый фрагмент при старте, если это не поворот
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment1)
                        .commit();
            }

            btnFirstFragment.setOnClickListener(v -> {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment1)
                        .commit();
            });

            btnSecondFragment.setOnClickListener(v -> {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment2)
                        .commit();
            });
        }
        // Если кнопки НЕ найдены, значит загрузился layout-land.
        // Там фрагменты прописаны через тег <fragment>, поэтому Android
        // выведет их сам автоматически. Дополнительный код не нужен.
    }
}