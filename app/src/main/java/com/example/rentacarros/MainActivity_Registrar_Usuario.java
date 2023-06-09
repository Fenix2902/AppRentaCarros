package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class MainActivity_Registrar_Usuario extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etUsername, etName, etPassword,etreservword;
    Button btnSave,btnListarUsers;
    Switch swAdmin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registrar_usuario);
        getSupportActionBar().hide();

        etUsername = findViewById(R.id.etRegUsername);
        etName = findViewById(R.id.etRegName);
        etPassword = findViewById(R.id.etpassword);
        btnSave = findViewById(R.id.btnSaveUser);
        btnListarUsers = findViewById(R.id.btnListUsers);
        etreservword = findViewById(R.id.etreservword);
        swAdmin = findViewById(R.id.switchRole);

        btnListarUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity_UsersList.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar que el nombre del usuario se haya digitado
                if (!etName.getText().toString().isEmpty() && !etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && !etreservword.getText().toString().isEmpty()){
                    //Busqueda de usuario en la coleccion users
                    db.collection("users")
                            .whereEqualTo("username",etUsername.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().isEmpty()){
                                            // No encontrar el documento con el username especifico
                                            // Create a new user with a first and last name
                                            Map<String, Object>user = new HashMap<>();
                                            user.put("name",etName.getText().toString());
                                            user.put("username",etUsername.getText().toString());
                                            user.put("password",etPassword.getText().toString());
                                            user.put("reservword",etreservword.getText().toString());
                                            limpiar();

                                            // Obtener el estado del Switch y agregarlo al objeto Map
                                            boolean isChecked = swAdmin.isChecked();
                                            user.put("Role", isChecked);
                                            limpiar();

                                            // Add a new document with a generated ID
                                            db.collection("users")
                                                    .add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                                                            if(isChecked){
                                                                startActivity(new Intent(getApplicationContext(),MainActivity_Registrar_Vehiculos.class));
                                                            }else{
                                                                startActivity(new Intent(getApplicationContext(),MainActivity_Renta_Vehiculos.class));
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Error al crear el usuario: " + e, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Usuario existe, Intentelo con otro...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos... ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void limpiar() {
        etUsername.setText("");
        etName.setText("");
        etPassword.setText("");
        etreservword.setText("");
    }
}