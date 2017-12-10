package com.agmostudio.pasarmalam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // get the document ID that was pass over when startActivity
        String docId = getIntent().getStringExtra("documentId");

        // ask Firestore to fetch the document with the specified Document ID
        FirebaseFirestore.getInstance()
                .collection("pasarmalam")
                .document(docId)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        PasarMalam pasarMalam = task.getResult().toObject(PasarMalam.class);
                        GeoPoint coordinate = pasarMalam.getCoordinate();
                        Log.d("doc", task.getResult().getId() + " => " + task.getResult().getData());

                        // Google Map uses LatLng, convert Firestore GeoPoint to Latlng
                        LatLng position = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());

                        // set the Marker/pin attributes and add it to GMap
                        mMap.addMarker(new MarkerOptions().position(position).title(pasarMalam.getName()));

                        // move the camera to the position of the pin
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    }
                });
    }
}
