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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class MainActivity_Renta_Vehiculos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etnumrenta, etusername,etnumplacas,etfecha,etfechaDev;
   Button  btnSaveRent, btnListarVehic,btnCerrarsesion,btnsearch,btndevolucionrenta;
    //String vieja_renta, buscar_id_renta;

    Spinner spplaca;

    Boolean isChecked = true;
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta_vehiculos);
        getSupportActionBar().hide();

        etnumrenta = findViewById(R.id.etNoRenta);
        etusername = findViewById(R.id.etUser);
        spplaca = findViewById(R.id.spPlaca);
        etfecha = findViewById(R.id.etDate);
        etfechaDev = findViewById(R.id.etDate2);

        btnSaveRent = findViewById(R.id.btnSaveRenta);
        btnListarVehic = findViewById(R.id.btnListarDisponibles);
        btnCerrarsesion = findViewById(R.id.btnCerrarSesion);
        btndevolucionrenta = findViewById(R.id.btndevolucionRenta);


        btnListarVehic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity_VehiculosList.class));
            }
        });

        btndevolucionrenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity_devolucion.class));
            }
        });

        btnSaveRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etnumrenta.getText().toString().isEmpty() && !etusername.getText().toString().isEmpty() && !spplaca.getSelectedItem().toString().isEmpty() && !etfecha.getText().toString().isEmpty() && !etfechaDev.getText().toString().isEmpty()){
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
                                                    final DocumentSnapshot autoSnapshot = task.getResult().getDocuments().get(0);
                                                    db.collection("Rentas").whereEqualTo("renta", etnumrenta.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().isEmpty()) {
                                                                    Map<String, Object> renta = new HashMap<>();
                                                                    renta.put("renta", etnumrenta.getText().toString());
                                                                    renta.put("usuario", etusername.getText().toString());
                                                                    renta.put("placa", spplaca.getSelectedItem().toString());
                                                                    renta.put("fecha", etfecha.getText().toString());
                                                                    renta.put("fecha de Devolución", etfechaDev.getText().toString());
                                                                    String fechaInicialStr = etfecha.getText().toString();
                                                                    String fechaFinalStr = etfechaDev.getText().toString();
                                                                    Date fechaInicial = parseDate(fechaInicialStr);
                                                                    Date fechaFinal = parseDate(fechaFinalStr);
                                                                    Date fechaActual = new Date();
                                                                    if (fechaInicial != null && fechaFinal != null) {
                                                                        if (fechaInicial.before(fechaActual)) {
                                                                            Toast.makeText(MainActivity_Renta_Vehiculos.this, "La fecha inicial no puede ser menor que la fecha actual", Toast.LENGTH_LONG).show();
                                                                            return;
                                                                        } else if (fechaFinal.before(fechaInicial)) {
                                                                            Toast.makeText(MainActivity_Renta_Vehiculos.this, "La fecha final no puede ser anterior a la fecha inicial", Toast.LENGTH_LONG).show();
                                                                            return;
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(MainActivity_Renta_Vehiculos.this, "Error interno", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                    db.collection("Rentas").add(renta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Toast.makeText(getApplicationContext(), "Renta ingresada correctamente!", Toast.LENGTH_LONG).show();
                                                                                    autoSnapshot.getReference().update("estado", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    Toast.makeText(MainActivity_Renta_Vehiculos.this, "Se actualizó el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(MainActivity_Renta_Vehiculos.this, "No se actualizó el estado del vehículo!!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                    limpiar();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getApplicationContext(), "No se pudo realizar el registro", Toast.LENGTH_SHORT).show();
                                                                                    limpiar();
                                                                                }
                                                                            });
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Usuario existente, ingrese uno nuevo!!", Toast.LENGTH_SHORT).show();
                                                                    limpiar();
                                                                }
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Debes ingresar los autos disponibles para rentar!", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Placa no disponible 1", Toast.LENGTH_SHORT).show();
                                                limpiar();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario no disponible para realizar renta", Toast.LENGTH_SHORT).show();
                                limpiar();
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


loadRefs();
    }

    private void loadRefs() {
        db.collection("Vehiculos")
                .whereEqualTo("estado", isChecked)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> placas = new ArrayList<>();
                            placas.add("Selecciona Placa");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String placa = document.getString("placa");
                                placas.add(placa);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_Renta_Vehiculos.this, android.R.layout.simple_spinner_item, placas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        etfechaDev.setText("");
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}

