package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.zeamac.app.ceylonhandicraft.R;

public class OrderInfoActivity extends AppCompatActivity {



    Button buttonDelivery,buttonConfirm;
    LinearLayout linearLayout;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;

    TextView address,addNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        buttonDelivery= findViewById(R.id.buttonDelivery);
        linearLayout = findViewById(R.id.deliveryAddress);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        address = findViewById(R.id.deliveryText);
        addNew = findViewById(R.id.addNewAddress);

        buttonDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDelivery.setBackgroundColor(Color.rgb(102,0,102));
                linearLayout.setVisibility(View.VISIBLE);
                buttonConfirm.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                addNew.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.addNewAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoActivity.this, AddDeliveryAddressActivity.class);
                startActivity(intent);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null){
                    fireStore.collection("UsersAddress").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            DocumentSnapshot snapshot = task.getResult();

                            if(snapshot.exists()){
                                Intent intent = new Intent(OrderInfoActivity.this, DeliveryLocationActivity.class );
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(OrderInfoActivity.this, "firstly Please Add Your Delivery Address", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }
        });


        findViewById(R.id.imageViewBackPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });



    }


}