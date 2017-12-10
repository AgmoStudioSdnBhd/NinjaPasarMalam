package com.agmostudio.pasarmalam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView nameTextView = findViewById(R.id.name);
        final TextView hoursTextView = findViewById(R.id.hours);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pasarmalam")
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("doc", document.getId() + " => " + document.getData());

                                PasarMalam item = document.toObject(PasarMalam.class);
                                nameTextView.setText(item.getName());
                                hoursTextView.setText(item.getHours());
                            }
                        } else {
                            Log.w("doc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
