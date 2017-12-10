package com.agmostudio.pasarmalam;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        Query query = FirebaseFirestore.getInstance()
                .collection("pasarmalam");

        // Configure recycler adapter options:
        // query is the Query object defined above.
        // Chat.class instructs the adapter to convert each DocumentSnapshot to a Chat object
        FirestoreRecyclerOptions<PasarMalam> options = new FirestoreRecyclerOptions.Builder<PasarMalam>()
                .setQuery(query, PasarMalam.class)
                .setLifecycleOwner(this)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<PasarMalam, PasarMalamHolder>(options) {
            @Override
            public PasarMalamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a
                // layout called R.layout.list_item for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new PasarMalamHolder(view);
            }

            @Override
            protected void onBindViewHolder(final PasarMalamHolder holder, int position, PasarMalam model) {
                holder.nameTextView.setText(model.getName());
                holder.hoursTextView.setText(model.getHours());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // our PasarMalam doesnt have ID, so we get from Firestore Document
                        DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());

                        // create an Intent to start MapActivity
                        // pass in the document ID so we can find back the document
                        Intent mapIntent = new Intent(v.getContext(), MapsActivity.class);
                        mapIntent.putExtra("documentId", doc.getId());
                        startActivity(mapIntent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add our Suggesstion to the given menu
        getMenuInflater().inflate(R.menu.menu_suggest, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_suggest) {
            showSuggestionDialog();
            return true; // tell others we have handled it
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuggestionDialog() {
        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_suggest, null);
        final EditText suggestText = dialogLayout.findViewById(R.id.suggestion);

        new AlertDialog.Builder(this)
                .setTitle("Suggest a Location")
                .setView(dialogLayout)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Suggest", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = suggestText.getText().toString().trim();

                        if (TextUtils.isEmpty(text)) {
                            // user didnt type anything, just exit
                            return;
                        }

                        Map<String, Object> data = new HashMap<>();
                        data.put("name", text);

                        FirebaseFirestore.getInstance()
                                .collection("suggestions")
                                .add(data)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        String msg;
                                        if (task.isSuccessful()) {
                                            msg = "Thank you for your suggestion";
                                        } else {
                                            msg = task.getException().getMessage();
                                        }

                                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                })
                .show();

    }
}
