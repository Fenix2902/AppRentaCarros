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

public class MainActivity_VehiculosList extends AppCompatActivity {

    RecyclerView VehiRecycler;
    ArrayList<Vehiculos> vehiList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vehiculos_list);


        VehiRecycler = findViewById(R.id.rvVehiculos);
        vehiList = new ArrayList<>();
        VehiRecycler.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        VehiRecycler.setHasFixedSize(true);
        //Cargar datos de Firebase - Firestore
        loadVehiculos();
    }

    private void loadVehiculos() {
        db.collection("Vehiculos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                //Log.w("Datos", document.getId()+"=>"+document.getData());
                                Vehiculos objvehic = new Vehiculos();
                               boolean estado  = document.getBoolean("estado");
                                if (estado) {
                                    objvehic.setEstado("Disponible");
                                } else {
                                    objvehic.setEstado("No disponible");
                                }
                                objvehic.setMarca(document.getString("marca"));
                                objvehic.setPlaca(document.getString("placa"));
                                vehiList.add(objvehic);

                            }
                            VehiculosAdapter aVehic = new VehiculosAdapter(vehiList);
                            VehiRecycler.setAdapter(aVehic);
                        }else {
                           // Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}