package lk.zeamac.app.handicraftadmin.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lk.zeamac.app.handicraftadmin.Activity.MainActivity;
import lk.zeamac.app.handicraftadmin.Adapter.CategoryAdapter;
import lk.zeamac.app.handicraftadmin.Adapter.ProductAdapter;
import lk.zeamac.app.handicraftadmin.Entity.CategoryEntity;
import lk.zeamac.app.handicraftadmin.Entity.ProductEntity;
import lk.zeamac.app.handicraftadmin.R;


public class CategoryFragment extends Fragment {

    private ImageView imageButton;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private Uri imagePath;
    private ArrayList<CategoryEntity> categoryItems;
    EditText editTextName;
    private CategoryAdapter categoryAdapter;
    private boolean isImageSelected = false;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        builder = new AlertDialog.Builder(getContext());

        // Remove Product

        categoryAdapter = new CategoryAdapter(categoryItems, getActivity());
        categoryAdapter.setOnProductClickRemoveListener(new ProductAdapter.OnProductClickListener() {
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

        // Remove Product















// BackButton

        fragment.findViewById(R.id.textViewBackDashBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

// BackButton

//     Add Category
        TextView addImageBtn = fragment.findViewById(R.id.addImageBtn);

        imageButton = fragment.findViewById(R.id.imageButton);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });

        //Add New Category

        fragment.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName = fragment.findViewById(R.id.categoryName);

                String name = editTextName.getText().toString();


                if(!isImageSelected){
                    Toast.makeText(getActivity().getApplicationContext(), "Please Select Category Image", Toast.LENGTH_LONG).show();

                }else if(name.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Category Name", Toast.LENGTH_LONG).show();
                }else {

                String imageId = UUID.randomUUID().toString();

                CategoryEntity category = new CategoryEntity(imageId, name, imageId);


                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Adding new Category");
                dialog.setCancelable(false);
                dialog.show();

                fireStore.collection("Categories").add(category)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if (imagePath != null) {
                                    dialog.setMessage("Uploading image...");
                                    StorageReference reference = storage.getReference("category-images")
                                            .child(imageId);
                                    reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            dialog.dismiss();

                                            editTextName.setText("");
                                            int drawableResourceId = R.drawable.baseline_category_24;
                                            imageButton.setImageResource(drawableResourceId);
                                            categoryAdapter.notifyDataSetChanged();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                            dialog.setMessage("Uploading " + (int) progress + "%");


                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });


                }
            }
        });


//     Add Category

        //     View Category
        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.allCategoryViewAdmin);
        categoryAdapter = new CategoryAdapter(categoryItems, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(categoryAdapter);

        fireStore.collection("Categories")
                //                .limit(5)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange change : value.getDocumentChanges()) {
                    CategoryEntity category = change.getDocument().toObject(CategoryEntity.class);
                    switch (change.getType()) {
                        case ADDED:
                            categoryItems.add(category);
                        case MODIFIED:
                            CategoryEntity old = categoryItems.stream().filter(i -> i.getId()
                                            .equals(category.getId()))
                                    .findFirst()
                                    .orElse(null);


                            if (old != null) {
                                old.setName(category.getName());
                                old.setImagePath(category.getImagePath());
                            }
                            break;
                        case REMOVED:
                            categoryItems.remove(category);
                    }
                }

                categoryAdapter.notifyDataSetChanged();
            }
        });


        //     View Category


    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imagePath = result.getData().getData();
                        isImageSelected = true;
                        Picasso.get()
                                .load(imagePath)
                                .resize(200, 200)
                                .centerCrop()
                                .into(imageButton);


                    }
                }
            }
    );



    // Remove Product
    private void removeProduct(int position) {
        CategoryEntity productToDelete = categoryItems.get(position);

        fireStore.collection("Categories").whereEqualTo("id", productToDelete.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        StorageReference storageReference = storage.getReference("category-images").child(productToDelete.getId());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                categoryItems.remove(position);
                                categoryAdapter.notifyItemRemoved(position);
                                Toast.makeText(getActivity().getApplicationContext(), "Category Removed", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to Removed Category Images: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed to Removed Category: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
// Remove Product


}