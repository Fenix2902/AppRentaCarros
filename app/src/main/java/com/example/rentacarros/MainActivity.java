package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etUsername, etPassword;
    Button btnIniciar, btnRegistrar,btnrecuperarcontraseña;
    Boolean Role;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnRegistrar = findViewById(R.id.btnRegistrarUser);
        btnrecuperarcontraseña = findViewById(R.id.btnRecuperaContr);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pasar a la actividad que muestra los usuarios
                startActivity(new Intent(getApplicationContext(), MainActivity_Registrar_Usuario.class));//permite ir a otra pagina
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    db.collection("users").whereEqualTo("username", etUsername.getText().toString())
                            .whereEqualTo("password", etPassword.getText().toString()).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().size() > 0) {
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            Role = document.getBoolean("Role");
                                            if (Role != null) {
                                                if (Role) {
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity_Opciones_de_Registro.class);
                                                    startActivity(intent);

                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity_Renta_Vehiculos.class);
                                                    startActivity(intent);
                                                }

                                            } else {
                                                Toast.makeText(MainActivity.this, "Acceso no autorizado", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(MainActivity.this, "Acceso de Sesión no exitoso", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Ingresa todos los campos para iniciar sesion!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnrecuperarcontraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity_RecuperarContrasena.class));
            }
        });

    }
}