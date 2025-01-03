package com.example.bevasarlobeadando_ak;

import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText nevInput, egysegarInput, mennyisegInput, mertekegysegInput;
    private Button hozzaadasButton, listazasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nevInput = findViewById(R.id.nevInput);
        egysegarInput = findViewById(R.id.egysegarInput);
        mennyisegInput = findViewById(R.id.mennyisegInput);
        mertekegysegInput = findViewById(R.id.mertekegysegInput);
        hozzaadasButton = findViewById(R.id.hozzaadasButton);
        listazasButton = findViewById(R.id.listazasButton);

        hozzaadasButton.setOnClickListener(v -> addTermek());
        listazasButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        });
    }

    private void addTermek() {
        String nev = nevInput.getText().toString();
        String egysegarStr = egysegarInput.getText().toString();
        String mennyisegStr = mennyisegInput.getText().toString();
        String mertekegyseg = mertekegysegInput.getText().toString();

        if (nev.isEmpty() || egysegarStr.isEmpty() || mennyisegStr.isEmpty() || mertekegyseg.isEmpty()) {
            Toast.makeText(this, "Egyik mezőt sem lehet üresen hagyni!", Toast.LENGTH_SHORT).show();
            return;
        }

        int egysegar = Integer.parseInt(egysegarStr);
        double mennyiseg = Double.parseDouble(mennyisegStr);

        Termekek ujTermek = new Termekek(nev, egysegar, mennyiseg, mertekegyseg);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.addTermek(ujTermek);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Termék hozzáadva!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                } else {
                    Toast.makeText(MainActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputs() {
        nevInput.setText("");
        egysegarInput.setText("");
        mennyisegInput.setText("");
        mertekegysegInput.setText("");
    }
}