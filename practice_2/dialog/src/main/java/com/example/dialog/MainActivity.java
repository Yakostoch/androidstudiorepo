package com.example.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickShowAlert(View view){
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }
    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onClickShowTime(View view)
    {
        Snackbar.make(view, "Привет, MIREA!", Snackbar.LENGTH_SHORT).show();
        TimeDialogFragment timeDialog = new TimeDialogFragment();
        timeDialog.show(getSupportFragmentManager(), "mirea");
    }

    public void onTimeOkClicked(int hour, int minute) {
        String time = String.format("%02d:%02d", hour, minute);
        Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
    }

    public void onClickShowDate(View view)
    {
        DateDialogFragment dateDialog = new DateDialogFragment();
        dateDialog.show(getSupportFragmentManager(), "mirea");
    }
    public void onDateOkClicked(int year, int month, int day) {
        String date = String.format("%02d.%02d.%d", day, month + 1, year);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }
    public void onClickShowProgress(View view)
    {
        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
        progressDialog.show(getSupportFragmentManager(), "mirea");
    }

}