package com.mirea.bkt.favouritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {

    private EditText editTextBook;
    private EditText editTextQuote;

    private TextView textViewDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        editTextBook = findViewById(R.id.editTextFavBook);
        editTextQuote = findViewById(R.id.editTextQuote);

        TextView textViewDev = findViewById(R.id.textViewDevInfo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String book_name = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quotes_name = extras.getString(MainActivity.QUOTES_KEY);
            textViewDev.setText(String.format("Моя любимая книга: %s и цитата %s",
                    book_name, quotes_name));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void sendData(View view) {
        String book = editTextBook.getText().toString();
        String quote = editTextQuote.getText().toString();
        String text = "Название Вашей любимой книги: " + book + ". Цитата: " + quote;

        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, text);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}