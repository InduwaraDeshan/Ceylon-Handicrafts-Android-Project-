package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Adapter.CheckOutProductAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.DeliveryInfoEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.MyOrderEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    TextView textView12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        textView12 = findViewById(R.id.textView12);
        textView12.setText("Rs :" + ProductCheckOutActivity.totalPrice);

        findViewById(R.id.payment_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, ProductCheckOutActivity.class);
                startActivity(intent);
            }
        });
    }
}