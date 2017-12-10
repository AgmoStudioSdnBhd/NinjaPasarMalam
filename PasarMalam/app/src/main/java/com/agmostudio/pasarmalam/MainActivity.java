package com.agmostudio.pasarmalam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

        // find all the Views that needed
        final View row = findViewById(R.id.row);
        final TextView nameTextView = findViewById(R.id.name);
        final TextView hoursTextView = findViewById(R.id.hours);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pasarmalam")
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final DocumentSnapshot document : task.getResult()) {
                                Log.d("doc", document.getId() + " => " + document.getData());

                                // convert document into Pasar Malam
                                PasarMalam item = document.toObject(PasarMalam.class);

                                // update the UI with our data
                                nameTextView.setText(item.getName());
                                hoursTextView.setText(item.getHours());

                                row.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // create an Intent to start MapActivity
                                        // pass in the document ID so we can find back the document
                                        Intent mapIntent = new Intent(v.getContext(), MapsActivity.class);
                                        mapIntent.putExtra("documentId", document.getId());
                                        startActivity(mapIntent);
                                    }
                                });
                            }
                        } else {
                            Log.w("doc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
