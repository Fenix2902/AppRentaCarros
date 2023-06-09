package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.assist.AssistStructure;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_Registrar_Vehiculos extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnRegresar, btnSave,btneditar,btnSearch, btndelete;
    EditText etplaca, etmarca, etvalorDia;
    String old_placa_auto, id_placa_find;

    Switch swdisponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registrar_vehiculos);
        getSupportActionBar().hide();

        btnRegresar = findViewById(R.id.btnVolverOpc);
        btnSave = findViewById(R.id.btnSaveVehic);
        btneditar = findViewById(R.id.btnUpdateV);
        btnSearch = findViewById(R.id.btnSearchV);
        btndelete = findViewById(R.id.btnDeleteV);

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

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_placa_find != null) {
                    if (!etplaca.getText().toString().isEmpty() && !etmarca.getText().toString().isEmpty() && !etvalorDia.getText().toString().isEmpty()){
                        if (!old_placa_auto.equals(etplaca.getText().toString())){
                            db.collection("Vehiculos").whereEqualTo("placa", etplaca.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().isEmpty()){
                                            Map<String, Object> auto_placa = new HashMap<>();
                                            auto_placa.put("marca", etmarca.getText().toString());
                                            auto_placa.put("placa", etplaca.getText().toString());
                                            auto_placa.put("Valor Día", etvalorDia.getText().toString());
                                            boolean estado = swdisponible.isChecked();
                                            auto_placa.put("estado", estado);
                                            db.collection("Vehiculos").document(id_placa_find).set(auto_placa).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Auto editado correctamente!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Error interno!", Toast.LENGTH_SHORT).show();
                                                    limpiar();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Auto existe, intente de nuevo!", Toast.LENGTH_SHORT).show();
                                            limpiar();
                                        }
                                    }
                                }
                            });
                        }else{
                            Map<String, Object> auto_placa = new HashMap<>();
                            auto_placa.put("marca", etmarca.getText().toString());
                            auto_placa.put("placa", etplaca.getText().toString());
                            auto_placa.put("Valor Día", etvalorDia.getText().toString());
                            boolean estado = swdisponible.isChecked();
                            auto_placa.put("estado", estado);
                            db.collection("Vehiculos").document(id_placa_find).set(auto_placa).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Auto actualizado correctamente!", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Error interno!", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            });
                        }
                    }else{
                        Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Debe ingresar todos los datos para editar, intente de nuevo!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Debe realizar una búsqueda antes de editar el auto!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_placa_find != null) {
                    if (!etplaca.getText().toString().isEmpty()) {
                        db.collection("Vehiculos").document(id_placa_find).delete(); {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity_Registrar_Vehiculos.this);
                            alertDialogBuilder.setMessage("Quieres borrar el auto?");
                            alertDialogBuilder.setPositiveButton("Eliminar!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(getApplicationContext(),"Auto borrado con exito!",Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            });
                            alertDialogBuilder.setNegativeButton("Cancelar!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(getApplicationContext(),"Auto no borrado con exito!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    } else {
                        Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Ingrese la placa del auto!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Debe realizar una búsqueda antes de editar el auto!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if  (!etplaca.getText().toString().isEmpty()){
                    db.collection("Vehiculos").whereEqualTo("placa", etplaca.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (!task.getResult().isEmpty()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        old_placa_auto = document.getString("placa");
                                        id_placa_find = document.getId();
                                        etmarca.setText(document.getString("marca"));
                                        etplaca.setText(document.getString("placa"));
                                        etvalorDia.setText(document.getString("Valor Día"));
                                        boolean auto_estado = document.getBoolean("estado");
                                        swdisponible.setChecked(auto_estado);
                                    }
                                } else {
                                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Auto no existe, intente de nuevo!", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity_Registrar_Vehiculos.this, "Debe ingresar la placa del auto para buscar, intente de nuevo!", Toast.LENGTH_SHORT).show();
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