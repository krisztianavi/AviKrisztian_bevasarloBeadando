package com.example.bevasarlobeadando_ak;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);

        loadTermekek();

        Button visszaButton = findViewById(R.id.visszaButton);
        visszaButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Termekek selectedTermek = (Termekek) parent.getItemAtPosition(position);
            Intent intent = new Intent(ListActivity.this, TermekActivity.class);
            intent.putExtra("TERMÉK_ID", selectedTermek.getId());
            startActivity(intent);
        });
    }

    private void loadTermekek() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Termekek>> call = apiService.getTermekek();

        call.enqueue(new Callback<List<Termekek>>() {
            @Override
            public void onResponse(Call<List<Termekek>> call, Response<List<Termekek>> response) {
                if (response.isSuccessful()) {
                    List<Termekek> termekekList = response.body();
                    ArrayAdapter<Termekek> adapter = new ArrayAdapter<Termekek>(ListActivity.this, android.R.layout.simple_list_item_2, termekekList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = convertView;

                            if (view == null) {
                                view = LayoutInflater.from(ListActivity.this).inflate(android.R.layout.simple_list_item_2, parent, false);
                            }

                            TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                            TextView textView2 = (TextView) view.findViewById(android.R.id.text2);

                            Termekek termek = getItem(position);
                            String termekNev = termek.getNev();
                            int egysegar = termek.getEgysegar();
                            double mennyiseg = termek.getMennyiseg();
                            String mertekegyseg = termek.getMertekegyseg();

                            String termekAdatok = "Egységár: " + egysegar + " Ft\n" + "Mennyiség: " + mennyiseg + " " + mertekegyseg;

                            if (textView1 != null) {
                                textView1.setText(termekNev);
                                textView1.setTypeface(null, android.graphics.Typeface.BOLD);
                                textView1.setTextSize(30);
                                textView1.setGravity(Gravity.CENTER_VERTICAL);
                            }

                            if (textView2 != null) {
                                textView2.setText(termekAdatok);
                                textView2.setTextSize(14);
                                textView2.setGravity(Gravity.CENTER_VERTICAL);
                            }
                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(ListActivity.this, "Hiba az adatok betöltése közben!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Termekek>> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }}