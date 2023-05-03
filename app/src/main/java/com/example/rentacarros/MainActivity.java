package com.example.rentacarros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etName, etPassword;
    Button btnIniciar, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnRegistrar = findViewById(R.id.btnRegistrarUser);



        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pasar a la actividad que muestra los usuarios
                startActivity(new Intent(getApplicationContext(),MainActivity_Registrar_Usuario.class));//permite ir a otra pagina
            }
        });
    }
}