package com.mirea.bkt.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.mirea.bkt.cryptoloader.databinding.ActivityMainBinding;

import java.security.InvalidParameterException;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 1234;
    private final String tag = this.getClass().getSimpleName();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEncrypt.setOnClickListener(this::onClickButton);
    }

    public void onClickButton(View view) {
        String sourceText = binding.editTextPhrase.getText().toString().trim();
        if (sourceText.isEmpty()) {
            binding.textViewStatus.setText(R.string.enter_phrase);
            return;
        }

        SecretKey key = CryptoUtils.generateKey();
        byte[] cipher = CryptoUtils.encryptMsg(sourceText, key);

        Bundle bundle = new Bundle();
        bundle.putByteArray(MyLoader.ARG_WORD, cipher);
        bundle.putByteArray(MyLoader.ARG_KEY, key.getEncoded());

        binding.textViewStatus.setText(R.string.loader_started);
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, bundle, this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(tag, "onLoaderReset");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == LOADER_ID) {
            Toast.makeText(this, "onCreateLoader: " + id, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        throw new InvalidParameterException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (loader.getId() == LOADER_ID) {
            Log.d(tag, "onLoadFinished: " + data);
            binding.textViewStatus.setText(getString(R.string.loader_finished, data));
            Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        }
    }
}
