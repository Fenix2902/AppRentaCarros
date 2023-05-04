package com.example.rentacarros;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity_Renta_Vehiculos extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta_vehiculos);
        getSupportActionBar().hide();
    }
}