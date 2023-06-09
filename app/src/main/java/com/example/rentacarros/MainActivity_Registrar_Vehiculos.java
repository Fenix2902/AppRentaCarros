package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.assist.AssistStructure;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_Registrar_Vehiculos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnRegresar, btnSave;
    EditText etplaca, etmarca, etvalorDia;

    Switch swdisponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registrar_vehiculos);
        getSupportActionBar().hide();

        btnRegresar = findViewById(R.id.btnVolverOpc);
        btnSave = findViewById(R.id.btnSaveVehic);

        etmarca = findViewById(R.id.etMarcaVeh);
        etplaca = findViewById(R.id.etPlaca);
        etvalorDia = findViewById(R.id.etvalordia);

        swdisponible = findViewById(R.id.swDispo);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar que el nombre del usuario se haya digitado
                if (!etplaca.getText().toString().isEmpty() && !etmarca.getText().toString().isEmpty() && !etvalorDia.getText().toString().isEmpty()){
                    //Busqueda de usuario en la coleccion users
                    db.collection("Vehiculos")
                            .whereEqualTo("placa",etplaca.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task ) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().isEmpty()){
                                            // No encontrar el documento con el username especifico
                                            // Crear un objeto Map con los datos que se van a guardar en Firestore
                                            Map<String, Object> vehiculo = new HashMap<>();
                                            vehiculo.put("placa",etplaca.getText().toString());
                                            vehiculo.put("marca",etmarca.getText().toString());
                                            vehiculo.put("Valor Día",etvalorDia.getText().toString());
                                            limpiar();

                                            // Obtener el estado del Switch y agregarlo al objeto Map
                                            boolean isChecked = swdisponible.isChecked();
                                            vehiculo.put("estado", isChecked);

                                            // Agregar un nuevo documento con un ID generado automáticamente
                                            db.collection("Vehiculos")
                                                    .add(vehiculo)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Vehiculo creado correctamente ", Toast.LENGTH_SHORT).show();
                                                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Error al crear el Vehiculo: " + e, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Vehiculo existe, Intentelo con otro...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos... ", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity_Opciones_de_Registro.class));
            }
        });

    }
    private void limpiar() {
        etplaca.setText("");
        etmarca.setText("");
        etvalorDia.setText("");
    }
}