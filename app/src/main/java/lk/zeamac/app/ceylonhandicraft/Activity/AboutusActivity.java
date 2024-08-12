package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.zip.Inflater;

import lk.zeamac.app.ceylonhandicraft.R;

public class AboutusActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.googleMapId);
        mapFragment.getMapAsync(AboutusActivity.this);


        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent("android.intent.action.DIAL",
                        Uri.parse("0768622203"));
                startActivity(callIntent);

            }
        });

        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("induwarade005@gmail.com ") + "?subject=" +
                        Uri.encode("induwarade005@gmail.com ") + "&body=" + Uri.encode("Hello WOLD");

                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        findViewById(R.id.aboutUsFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com/induwara.deshan.714");


            }

            private void gotoUrl(String s) {
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        findViewById(R.id.youtube)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoUrl("https://www.youtube.com/");


                    }
                    private void gotoUrl(String s) {
                        Uri uri = Uri.parse(s);
                        startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    }
                });



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(7.431921399720525, 80.21482932703874);

        map.addMarker(new MarkerOptions().position(latLng).title("Handicraft Shop"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
    }
}