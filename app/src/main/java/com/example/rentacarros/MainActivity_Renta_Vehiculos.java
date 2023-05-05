package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class MainActivity_Renta_Vehiculos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etnumrenta, etusername,etnumplaca,etfecha;
   Button btnVolver, btnSaveRent, btnsearch;
    String vieja_renta, buscar_id_renta;

    Boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta_vehiculos);
        getSupportActionBar().hide();

        etnumrenta = findViewById(R.id.etNoRenta);
        etusername = findViewById(R.id.etUser);
        etnumplaca = findViewById(R.id.etnumplaca);
        etfecha = findViewById(R.id.etDate);

        btnVolver = findViewById(R.id.btnVolver);
        btnSaveRent = findViewById(R.id.btnSaveRenta);
        btnsearch = findViewById(R.id.btnsearchrenta);

        btnSaveRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etnumrenta.getText().toString().isEmpty() && !etusername.getText().toString().isEmpty() && !etnumplaca.getText().toString().isEmpty() && !etfecha.getText().toString().isEmpty()){
                    db.collection("users").whereEqualTo("username", etusername.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    db.collection("Vehiculos").whereEqualTo("placa", etnumplaca.getText().toString()).whereEqualTo("estado", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() > 0) {
                                                    db.collection("Rentas").whereEqualTo("renta", etnumrenta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                if(task.getResult().isEmpty()){
                                                                    Map<String, Object> renta = new HashMap<>();
                                                                    renta.put("renta", etnumrenta.getText().toString());
                                                                    renta.put("usuario", etusername.getText().toString());
                                                                    renta.put("placa", etnumplaca.getText().toString());
                                                                    renta.put("fecha", etfecha.getText().toString());
                                                                    limpiar();
                                                                    db.collection("Rentas").add(renta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            Toast.makeText(getApplicationContext(), "Renta ingresada correctamente con la identificacion: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "No se pudo realizar el registro: " + e, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Usuario existente, ingrese uno nuevo!!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Placa no disponible o el automovil no esta disponible", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Placa no disponible o el automovil no esta disponible", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Ingresa todos los campos para realizar una renta", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity_Opciones_de_Registro.class));
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if  (!etnumrenta.getText().toString().isEmpty()){
                    db.collection("Rentas").whereEqualTo("renta", etnumrenta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (!task.getResult().isEmpty()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        vieja_renta = document.getString("renta");
                                        buscar_id_renta = document.getId();
                                        etusername.setText(document.getString("usuario"));
                                        etnumplaca.setText(document.getString("placa"));
                                        etfecha.setText(document.getString("fecha"));
                                        Toast.makeText(getApplicationContext(), "Codigo de renta: " + buscar_id_renta, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Renta no existe, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar la identificacion de renta a buscar, intente de nuevo!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void limpiar() {
        etnumrenta.setText("");
        etusername.setText("");
        etnumplaca.setText("");
        etfecha.setText("");
    }

}

