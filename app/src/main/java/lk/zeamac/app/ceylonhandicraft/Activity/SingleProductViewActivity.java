package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lk.zeamac.app.ceylonhandicraft.Adapter.ProductViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.ProductViewAdapter2;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.ProductEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class SingleProductViewActivity extends AppCompatActivity implements ProductViewAdapter.OnProductClickListener {

    TextView title, description, price, qty, totalPrice, increase, decrease, updateQty;
    ImageView image;
    String categoryName, productId, queryId;
    private FirebaseStorage storage;
    int count = 1;
    int updatedValue;
    int productPrice;
    private ArrayList<ProductEntity> productItems;
    private ProductViewAdapter2 productAdapter;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerViewProductView;
    Long fixedPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_view);

        storage = FirebaseStorage.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
            OnProductClick(productId);

        }


        findViewById(R.id.buttonAddToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long totalQty = Long.parseLong(String.valueOf(count));


                DocumentReference reference = fireStore.collection("Cart").document(queryId);
                reference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot productDoc = task.getResult();
                        if (productDoc.exists()) {
                            Toast.makeText(SingleProductViewActivity.this, "Already added! Quantity Updated", Toast.LENGTH_SHORT).show();
                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("qty", totalQty);
                            reference.update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();

                            DocumentReference reference1 = fireStore.collection("Cart")
                                    .document(queryId);

                            CartEntity cart = new CartEntity(queryId, queryId, userId, totalQty, fixedPrice, false);

                            reference1.set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SingleProductViewActivity.this, "Add To Cart Successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SingleProductViewActivity.this, "Add To Cart Failed", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }


                });

            }
        });


//        similar Product View


        productItems = new ArrayList<>();
        recyclerViewProductView = findViewById(R.id.similarView);
        recyclerViewProductView.setLayoutManager(new GridLayoutManager(SingleProductViewActivity.this, 1, GridLayoutManager.HORIZONTAL, false));
        productAdapter = new ProductViewAdapter2(productItems, SingleProductViewActivity.this, this::OnProductClick);
        recyclerViewProductView.setAdapter(productAdapter);


        fireStore.collection("Products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange change : value.getDocumentChanges()) {
                            ProductEntity product = change.getDocument().toObject(ProductEntity.class);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(product);
                                case MODIFIED:
                                    ProductEntity old = productItems.stream().filter(i -> i.getId()
                                                    .equals(product.getId()))
                                            .findFirst()
                                            .orElse(null);


                                    if (old != null) {
                                        old.setName(product.getName());
                                        old.setDescription(product.getDescription());
                                        old.setCategory(product.getCategory());
                                        old.setQty(product.getQty());
                                        old.setPrice(product.getPrice());
                                        old.setImage(product.getImage());
                                    }
                                    break;
                                case REMOVED:
                                    productItems.remove(product);
                            }
                        }

                        productAdapter.notifyDataSetChanged();

                    }
                });


//        similar Product View


    }

    private void OnCategorySearch(String categoryType) {

        ArrayList<ProductEntity> filteredList = new ArrayList<>();
        for (ProductEntity product : productItems) {

            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
            }
            productAdapter.setFilterList(filteredList);
        }

    }

    @Override
    public void OnProductClick(String product) {
        queryId = product;
        loadProduct(product);
        count = 1;
    }


    public void loadProduct(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Products").whereEqualTo("id", productId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override


            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ProductEntity product = document.toObject(ProductEntity.class);

                        title = findViewById(R.id.textViewProductTitle);
                        description = findViewById(R.id.textViewDescription);
                        price = findViewById(R.id.textViewProductPrice);
                        qty = findViewById(R.id.availableQty);
                        image = findViewById(R.id.imageViewProductImage);
                        totalPrice = findViewById(R.id.textViewTotalPrice);
                        increase = findViewById(R.id.textViewIncrease);
                        decrease = findViewById(R.id.textViewDecrease);
                        updateQty = findViewById(R.id.textViewUpdateQty);


                        updatedValue = Integer.parseInt(product.getQty());
                        productPrice = Integer.parseInt(product.getPrice());
                        fixedPrice = Long.valueOf(product.getPrice());

                        totalPrice.setText("" + productPrice);
                        updateQty.setText("1");

                        increase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (updatedValue > count) {
                                    count++;
                                    totalPrice.setText("" + count * productPrice);
                                    updateQty.setText("" + count);


                                } else {
                                    count = updatedValue;
                                }
                            }
                        });
                        decrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (count <= 1) {
                                    count = 1;
                                } else {
                                    count--;
                                    updateQty.setText("" + count);
                                    totalPrice.setText("" + count * productPrice);


                                }

                            }
                        });
                        storage.getReference("product-images/" + product.getImage())
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .resize(200, 200)
                                                .centerCrop()
                                                .into(image);
                                    }
                                });

                        title.setText(product.getName());
                        description.setText(product.getDescription());
                        price.setText(product.getPrice());
                        qty.setText(product.getQty());
                        categoryName = product.getCategory();

                        OnCategorySearch(categoryName);

                    }
                } else {
                    Log.w("FireStore", "Error getting documents", task.getException());
                }
            }
        });

    }
}


