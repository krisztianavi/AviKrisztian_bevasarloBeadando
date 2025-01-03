package com.example.bevasarlobeadando_ak;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermekActivity extends AppCompatActivity {

    private EditText nevInput, egysegarInput, mennyisegInput, mertekegysegInput;
    private Button modositasButton, torlesButton, visszaButton;
    private int termekId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termek);

        nevInput = findViewById(R.id.nevInput);
        egysegarInput = findViewById(R.id.egysegarInput);
        mennyisegInput = findViewById(R.id.mennyisegInput);
        mertekegysegInput = findViewById(R.id.mertekegysegInput);
        modositasButton = findViewById(R.id.modositasButton);
        torlesButton = findViewById(R.id.torlesButton);
        visszaButton = findViewById(R.id.visszaButton);

        termekId = getIntent().getIntExtra("TERMÉK_ID", -1);

        if (termekId != -1) {
            loadTermek(termekId);
        } else {
            Toast.makeText(this, "Nem található termék ilyen ID-val!", Toast.LENGTH_SHORT).show();
        }

        modositasButton.setOnClickListener(v -> updateTermek());
        torlesButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        visszaButton.setOnClickListener(v -> finish());
    }

    private void loadTermek(int id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Termekek> call = apiService.getTermek(id);

        call.enqueue(new Callback<Termekek>() {
            @Override
            public void onResponse(Call<Termekek> call, Response<Termekek> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Termekek termek = response.body();
                    nevInput.setText(termek.getNev());
                    egysegarInput.setText(String.valueOf(termek.getEgysegar()));
                    mennyisegInput.setText(String.valueOf(termek.getMennyiseg()));
                    mertekegysegInput.setText(termek.getMertekegyseg());
                } else {
                    Toast.makeText(TermekActivity.this, "Hiba az adatok betöltése közben!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Termekek> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTermek() {
        String nev = nevInput.getText().toString();
        String egysegarStr = egysegarInput.getText().toString();
        String mennyisegStr = mennyisegInput.getText().toString();
        String mertekegyseg = mertekegysegInput.getText().toString();

        if (nev.isEmpty() || egysegarStr.isEmpty() || mennyisegStr.isEmpty() || mertekegyseg.isEmpty()) {
            Toast.makeText(this, "Egyik mező sem maradhat üresen!", Toast.LENGTH_SHORT).show();
            return;
        }

        int egysegar = Integer.parseInt(egysegarStr);
        double mennyiseg = Double.parseDouble(mennyisegStr);

        Termekek updatedTermek = new Termekek(nev, egysegar, mennyiseg, mertekegyseg);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.updateTermek(termekId, updatedTermek);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TermekActivity.this, "A módosítás sikeres!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TermekActivity.this, "Hiba a módosítás során!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Megerősítés")
                .setMessage("Biztosan törölni szeretnéd ezt a terméket?")
                .setPositiveButton("Igen", (dialog, which) -> deleteTermek())
                .setNegativeButton("Nem", null)
                .show();
    }

    private void deleteTermek() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.deleteTermek(termekId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TermekActivity.this, "A termék sikeresen törölve!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TermekActivity.this, "Hiba történt a törlés során!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TermekActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}