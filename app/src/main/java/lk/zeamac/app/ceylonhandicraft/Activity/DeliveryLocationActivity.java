package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lk.zeamac.app.ceylonhandicraft.R;

public class DeliveryLocationActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private FirebaseFirestore fireStore;

    private FirebaseAuth firebaseAuth;
    GoogleMap gMap;
    FrameLayout map;
    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);



        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        map = findViewById(R.id.secondMapId);


        fusedClient = LocationServices.getFusedLocationProviderClient(DeliveryLocationActivity.this);
        getLocation();

        findViewById(R.id.currentLocation).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                fusedClient = LocationServices.getFusedLocationProviderClient(DeliveryLocationActivity.this);
                getLocation();
            }
        });

        findViewById(R.id.imageViewBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryLocationActivity.this, OrderInfoActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.shopNowBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryLocationActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });

        findViewById(R.id.imageViewBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryLocationActivity.this, OrderInfoActivity.class );
                startActivity(intent);
            }
        });


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    double latitude = currentLocation.getLatitude();
                    double Longitude = currentLocation.getLongitude();

                    String lati = String.valueOf(latitude);
                    String longi = String.valueOf(Longitude);

                    System.out.println(lati);
                    System.out.println(longi);

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        DocumentReference reference = fireStore.collection("UsersAddress").document(currentUser.getUid());
                        reference.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot document = task1.getResult();
                                if (document.exists()) {

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("latitude", lati);
                                    updates.put("longitude", longi);

                                    reference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(DeliveryLocationActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DeliveryLocationActivity.this, "Failed to update details", Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }
                            }
                        });
                    }

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.secondMapId);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(DeliveryLocationActivity.this);
                }
            }
        });


    }

}