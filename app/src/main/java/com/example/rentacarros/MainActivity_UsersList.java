package com.example.rentacarros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity_UsersList extends AppCompatActivity {
    RecyclerView userRecycler;
    ArrayList<Users> userlits;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_users_list);
        getSupportActionBar().hide();
        userRecycler = findViewById(R.id.rvUserList);
        userlits = new ArrayList<>();
        userRecycler.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        userRecycler.setHasFixedSize(true);
        // Cargar datos de Firebase - Firestore
        loadUsers();
    }
    private void loadUsers() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.w("Datos", document.getId() + " => " + document.getData());
                                //
                                Users objuser = new Users();
                                objuser.setName(document.getString("name"));
                                objuser.setUsername(document.getString("username"));
                                userlits.add(objuser);
                                //
                            }
                            UserAdapter auser=new UserAdapter(userlits);
                            userRecycler.setAdapter(auser);
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}