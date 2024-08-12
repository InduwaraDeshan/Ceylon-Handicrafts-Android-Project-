package lk.zeamac.app.ceylonhandicraft.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.zeamac.app.ceylonhandicraft.Activity.OrderInfoActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.ProductCheckOutActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.SingleProductViewActivity;
import lk.zeamac.app.ceylonhandicraft.Adapter.CartViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.DeliveryInfoEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class CartFragment extends Fragment {

    private ArrayList<CartEntity> productItems;
    private RecyclerView recyclerViewProductView;
    private CartViewAdapter cartViewAdapter;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(getContext());

        cartViewAdapter = new CartViewAdapter(productItems, getActivity());
        cartViewAdapter.setOnProductClickRemoveListener(new CartViewAdapter.OnProductClickListener() {
            @Override
            public void OnProductClick(int position) {
                builder.setTitle("Alert ❗")
                        .setMessage("Do you want to remove Product")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeProduct(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });


        //     View Product

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            CollectionReference cartRef = fireStore.collection("Cart");
            Query query = cartRef.whereEqualTo("userId", currentUser.getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        productItems = new ArrayList<>();
                        recyclerViewProductView = fragment.findViewById(R.id.cartProductView);
                        recyclerViewProductView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
                        cartViewAdapter = new CartViewAdapter(productItems, getActivity());
                        recyclerViewProductView.setAdapter(cartViewAdapter);
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            CartEntity cart = change.getDocument().toObject(CartEntity.class);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(cart);
                                case MODIFIED:
                                    CartEntity old = productItems.stream().filter(i -> i.getId().equals(cart.getId())).findFirst().orElse(null);


                                    ProgressDialog dialog = new ProgressDialog(getActivity());
                                    dialog.setMessage("Adding new Price");
                                    dialog.setCancelable(true);


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


        //     View Product
        fragment.findViewById(R.id.buttonCheckoutProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user1 = firebaseAuth.getCurrentUser();
                String Id = user1.getUid();
                fireStore.collection("UsersAddress").document(Id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();

                        if (snapshot.exists()) {
                            DeliveryInfoEntity address = snapshot.toObject(DeliveryInfoEntity.class);
                            if (address != null) {
                                startActivity(new Intent(getActivity(), ProductCheckOutActivity.class));
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Please Add To your Delivery Information", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), OrderInfoActivity.class));
                        }
                    }
                });


            }
        });


    }


    // Remove Product
    private void removeProduct(int position) {
        CartEntity productToDelete = productItems.get(position);

        fireStore.collection("Cart").whereEqualTo("id", productToDelete.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                WriteBatch writeBatch = fireStore.batch();
                List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc : snapshots) {
                    writeBatch.delete(doc.getReference());
                }
                writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        productItems.remove(position);
                        cartViewAdapter.notifyItemRemoved(position);
                        Toast.makeText(getActivity().getApplicationContext(), "Product Removed", Toast.LENGTH_LONG).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to Removed Product Images: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Failed to Removed Product: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

// Remove Product


}