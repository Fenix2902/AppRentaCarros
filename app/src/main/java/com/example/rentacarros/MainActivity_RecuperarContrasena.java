package com.example.rentacarros;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity_RecuperarContrasena extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etPalabraClave, etnewpassword, etconfirmarpassword;
    Button btnupdatepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recuperar_contrasena);
        getSupportActionBar().hide();

        etPalabraClave = findViewById(R.id.etpalabraclave);
        etnewpassword = findViewById(R.id.etNewPassword);
        etconfirmarpassword = findViewById(R.id.etConfirmarPassword);

        btnupdatepassword = findViewById(R.id.btnUpdatePassword);

        btnupdatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}