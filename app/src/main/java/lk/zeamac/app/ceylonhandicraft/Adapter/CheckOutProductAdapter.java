package lk.zeamac.app.ceylonhandicraft.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.CheckOutProductEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class CheckOutProductAdapter extends RecyclerView.Adapter<CheckOutProductAdapter.ViewHolder> {
    private static ArrayList<CartEntity> items;
    private FirebaseStorage storage;
    private static Context context;
    private static FirebaseFirestore fireStore;


    public CheckOutProductAdapter(ArrayList<CartEntity> items, Context context) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CheckOutProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_checkout_item, parent, false);


        return new CheckOutProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutProductAdapter.ViewHolder holder, int position) {

        fireStore = FirebaseFirestore.getInstance();

        CartEntity item = items.get(position);


        CollectionReference cartRef = fireStore.collection("Products");
        Query query = cartRef.whereEqualTo("id", item.getId());
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Map<String, Object> data = documentSnapshot.getData();

                            holder.name.setText(data.get("name").toString());


                        } else {
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        holder.price.setText(item.getCartProductFixedPrice().toString());

        holder.qty.setText(item.getQty().toString());


        storage.getReference("product-images/" + item.getId())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into(holder.image);
                    }
                });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, qty;
        ImageView image;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkout_title);
            price = itemView.findViewById(R.id.checkout_price);
            image = itemView.findViewById(R.id.checkout_imageView);
            qty = itemView.findViewById(R.id.checkout_qty);


        }

    }

}