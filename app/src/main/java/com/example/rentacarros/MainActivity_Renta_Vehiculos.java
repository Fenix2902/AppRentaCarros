package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity_Renta_Vehiculos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etnumrenta, etusername,etnumplacas,etfecha;
   Button  btnSaveRent, btnListarVehic,btnCerrarsesion,btnsearch;
    String vieja_renta, buscar_id_renta;
    List<String> aPlaca = new ArrayList<String>();
    Spinner spplaca;

    Boolean isChecked = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta_vehiculos);
        getSupportActionBar().hide();

        etnumrenta = findViewById(R.id.etNoRenta);
        etusername = findViewById(R.id.etUser);
        //etnumplaca = findViewById(R.id.etnumplaca);
        spplaca = findViewById(R.id.spPlaca);
        etfecha = findViewById(R.id.etDate);

        btnSaveRent = findViewById(R.id.btnSaveRenta);
        btnListarVehic = findViewById(R.id.btnListarDisponibles);
        btnCerrarsesion = findViewById(R.id.btnCerrarSesion);

        btnListarVehic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity_VehiculosList.class));
            }
        });
        btnSaveRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etnumrenta.getText().toString().isEmpty() && !etusername.getText().toString().isEmpty() && !spplaca.getSelectedItem().toString().isEmpty() && !etfecha.getText().toString().isEmpty()){
                    db.collection("users").whereEqualTo("username", etusername.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    db.collection("Vehiculos").whereEqualTo("placa", spplaca.getSelectedItem().toString()).whereEqualTo("estado", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() > 0) {

                                                    task.getResult().getDocuments().get(0).getReference().update("estado", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(MainActivity_Renta_Vehiculos.this, "Se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainActivity_Renta_Vehiculos.this, "No se actualizo el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                    db.collection("Rentas").whereEqualTo("renta", etnumrenta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                if(task.getResult().isEmpty()){
                                                                    Map<String, Object> renta = new HashMap<>();
                                                                    renta.put("renta", etnumrenta.getText().toString());
                                                                    renta.put("usuario", etusername.getText().toString());
                                                                    renta.put("placa", spplaca.getSelectedItem().toString());
                                                                    renta.put("fecha", etfecha.getText().toString());
                                                                    limpiar();

                                                                    db.collection("Rentas").add(renta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            Toast.makeText(getApplicationContext(), "Renta ingresada correctamente ", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(getApplicationContext(), "Placa no disponible", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Placa no disponible", Toast.LENGTH_SHORT).show();
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

        btnCerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        /*btnsearch.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }

    private void loadRefs() {
        db.collection("Vehiculos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                aPlaca.add(document.getString("placa"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_Renta_Vehiculos.this, android.R.layout.simple_list_item_checked, aPlaca);
                            spplaca.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al cargar las referencias de vehículos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void limpiar() {
        etnumrenta.setText("");
        etusername.setText("");
        spplaca.setSelection(0);
        etfecha.setText("");
    }

}

