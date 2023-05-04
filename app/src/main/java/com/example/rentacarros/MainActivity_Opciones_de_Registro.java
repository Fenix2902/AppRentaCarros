package com.example.rentacarros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.checkerframework.common.subtyping.qual.Bottom;

public class MainActivity_Opciones_de_Registro extends AppCompatActivity {

    Button btnRegistrar_Vehiculo, btnRentar,btnVolverInic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_opciones_de_registro);
        getSupportActionBar().hide();

        btnRentar = findViewById(R.id.btnRentarVehiculo);
        btnRegistrar_Vehiculo = findViewById(R.id.btnRegVehiculo);

        btnRegistrar_Vehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity_Registrar_Vehiculos.class));
            }
        });

        btnRentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_Renta_Vehiculos.class));
            }
        });

        btnVolverInic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }
}