package lk.zeamac.app.handicraftadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.zeamac.app.handicraftadmin.Entity.ProductEntity;
import lk.zeamac.app.handicraftadmin.Fragment.ProductFragment;
import lk.zeamac.app.handicraftadmin.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    static ArrayList<ProductEntity> items;
    FirebaseStorage storage;
    Context context;
    private static OnProductClickListener onProductClickRemoveListener;



    public ProductAdapter(ArrayList<ProductEntity> items, Context context) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();

    }


    public void setFilterList(ArrayList<ProductEntity> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }


    public interface OnProductClickListener {
        void OnProductClick(int position);
    }

    public void setOnProductClickRemoveListener(OnProductClickListener clickListener){
        this.onProductClickRemoveListener = clickListener;
    }




    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_product_list, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductEntity item = items.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice());
        holder.qty.setText(item.getQty());


        storage.getReference("product-images/" + item.getImage())
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
        TextView name, description, price, qty;
        ImageView image, removeProductAdmin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productTitleAdmin);
            description = itemView.findViewById(R.id.descriptionProductAdmin);
            price = itemView.findViewById(R.id.productPriceAdmin);
            qty = itemView.findViewById(R.id.productQtyAdmin);
            image = itemView.findViewById(R.id.imgProductAll);


            removeProductAdmin =itemView.findViewById(R.id.removeProductAdmin);
            removeProductAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onProductClickRemoveListener !=null){
                        onProductClickRemoveListener.OnProductClick(getAdapterPosition());
                    }
                }
            });

        }
    }


}
