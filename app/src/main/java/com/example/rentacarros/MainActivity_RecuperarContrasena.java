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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_RecuperarContrasena extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etusuario, etPalabraClave, etnewpassword, etconfirmarpassword;
    Button btnupdatepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recuperar_contrasena);
        getSupportActionBar().hide();

        etusuario = findViewById(R.id.etusuarioR);
        etPalabraClave = findViewById(R.id.etpalabraclave);
        etnewpassword = findViewById(R.id.etNewPassword);
        etconfirmarpassword = findViewById(R.id.etConfirmarPassword);

        btnupdatepassword = findViewById(R.id.btnUpdatePassword);

        btnupdatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etusuario.getText().toString().isEmpty() && !etPalabraClave.getText().toString().isEmpty() && !etnewpassword.getText().toString().isEmpty() && !etconfirmarpassword.getText().toString().isEmpty()) {
                    // Verificar que la nueva contraseña coincida con la confirmación de contraseña
                    if (etnewpassword.getText().toString().equals(etconfirmarpassword.getText().toString())) {
                        // Búsqueda de usuario en la colección "users"
                        db.collection("users")
                                .whereEqualTo("username", etusuario.getText().toString())
                                .whereEqualTo("reservword", etPalabraClave.getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (!task.getResult().isEmpty()) {
                                                // La palabra clave y el usuario son correctos, se encontró el usuario
                                                // Actualizar la contraseña
                                                DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                                String userId = userDocument.getId();

                                                // Crear un mapa con los datos actualizados
                                                Map<String, Object> updatedUserData = new HashMap<>();
                                                updatedUserData.put("password", etnewpassword.getText().toString());

                                                // Actualizar el documento del usuario con los datos actualizados
                                                db.collection("users")
                                                        .document(userId)
                                                        .update(updatedUserData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getApplicationContext(), "Se actualizó correctamente la contraseña", Toast.LENGTH_SHORT).show();
                                                                // Aquí puedes realizar alguna acción adicional o redirigir al usuario a otra actividad si lo deseas
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Error al actualizar la contraseña: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "La palabra clave o el usuario son incorrectos, intenta de nuevo", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "La nueva contraseña y la confirmación de contraseña no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limpiar() {
        etPalabraClave.setText("");
        etnewpassword.setText("");
        etconfirmarpassword.setText("");
    }
}