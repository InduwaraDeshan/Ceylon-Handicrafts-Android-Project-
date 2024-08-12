package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Adapter.CartViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.CategoryViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.CheckOutProductAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.CategoryEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.CheckOutProductEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.DeliveryInfoEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class ProductCheckOutActivity extends AppCompatActivity {
    private ArrayList<CartEntity> productItems;
    private RecyclerView recyclerViewProductView;
    private CheckOutProductAdapter cartViewAdapter;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    TextView totalProductFee, itemTotal;
    public static String totalPrice;
    double totalAmount, changeAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_check_out);


        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        totalProductFee = findViewById(R.id.totalFee);
        itemTotal = findViewById(R.id.itemTotal);

        //     View Product

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            CollectionReference cartRef = fireStore.collection("Cart");
            Query query = cartRef.whereEqualTo("userId", currentUser.getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String cartSize = String.valueOf(queryDocumentSnapshots.size());
                    if (!queryDocumentSnapshots.isEmpty()) {
                        productItems = new ArrayList<>();
                        recyclerViewProductView = findViewById(R.id.checkOutView);
                        recyclerViewProductView.setLayoutManager(new GridLayoutManager(ProductCheckOutActivity.this, 1, GridLayoutManager.VERTICAL, false));
                        cartViewAdapter = new CheckOutProductAdapter(productItems, ProductCheckOutActivity.this);
                        recyclerViewProductView.setAdapter(cartViewAdapter);
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            CartEntity cart = change.getDocument().toObject(CartEntity.class);
                            itemTotal.setText(cartSize);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(cart);
                                case MODIFIED:
                                    CartEntity old = productItems.stream().filter(i -> i.getId().equals(cart.getId())).findFirst().orElse(null);


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference cartRef = db.collection("Cart");
                                    cartRef.whereEqualTo("id", cart.getId()).whereEqualTo("selected", false).get()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    for (DocumentSnapshot document : task1.getResult()) {
                                                        CartEntity item = document.toObject(CartEntity.class);
                                                        totalAmount = 0;
                                                        totalAmount += item.getCartProductFixedPrice() * old.getQty();

                                                    }
                                                    changeAmount += totalAmount;

                                                    //        Calculate Delivery Price
                                                    TextView deliveryFee = findViewById(R.id.deliveryFee);
                                                    FirebaseUser user1 = firebaseAuth.getCurrentUser();
                                                    String Id = user1.getUid();
                                                    fireStore.collection("UsersAddress").document(Id).get().addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot snapshot = task.getResult();

                                                            if (snapshot.exists()) {
                                                                DeliveryInfoEntity address = snapshot.toObject(DeliveryInfoEntity.class);
                                                                if (address != null) {
                                                                    String district = address.getCity();
                                                                    if (district.equals("Kurunegala")) {
                                                                        deliveryFee.setText("500");
                                                                        totalPrice = String.valueOf(changeAmount + 500);
                                                                        totalProductFee.setText(totalPrice);

                                                                    } else {
                                                                        deliveryFee.setText("1500");
                                                                        totalPrice = String.valueOf(changeAmount + 1500);
                                                                        totalProductFee.setText(totalPrice);
                                                                    }
                                                                } else {
                                                                    deliveryFee.setText("0");
                                                                }
                                                            }
                                                        }
                                                    });
//        Calculate Delivery Price
                                                    cartViewAdapter.notifyDataSetChanged();
                                                } else {
                                                }
                                            });

                                    if (old != null) {
                                        old.setQty(cart.getQty());
                                        old.setCartProductFixedPrice(cart.getCartProductFixedPrice());

                                    }
                                    break;
                                case REMOVED:
                                    productItems.remove(cart);
                            }
                        }
                        cartViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }


        findViewById(R.id.buttonPayNowProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductCheckOutActivity.this, PaymentActivity.class));
            }
        });

        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductCheckOutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}