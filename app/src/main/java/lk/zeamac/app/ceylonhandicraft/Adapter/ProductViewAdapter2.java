
package lk.zeamac.app.ceylonhandicraft.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lk.zeamac.app.ceylonhandicraft.Entity.AllCategoryEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.ProductEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class ProductViewAdapter2 extends RecyclerView.Adapter<ProductViewAdapter2.ViewHolder> {
    private static ArrayList<ProductEntity> items;
    private FirebaseStorage storage;
    private Context context;
   OnProductClickListener onProductClickListener;

    public ProductViewAdapter2(ArrayList<ProductEntity> items, Context context,OnProductClickListener productClickListener) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.onProductClickListener = productClickListener;
    }
    public interface OnProductClickListener{
        void OnProductClick(String product);
    }


    public void setFilterList(ArrayList<ProductEntity> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_product_list2,parent,false);
        return new ProductViewAdapter2.ViewHolder(view,onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter2.ViewHolder holder, int position) {
        ProductEntity item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());

        storage.getReference("product-images/"+item.getImage())
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,shopNow;
        ImageView image;




        public ViewHolder(@NonNull View itemView,final OnProductClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.titleTextProductTitle2);
            price = itemView.findViewById(R.id.productPrice2);
            image = itemView.findViewById(R.id.productImage2);

            shopNow= itemView.findViewById(R.id.plusSingleProductView);
            shopNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        clickListener.OnProductClick(items.get(position).getId());

                    }
                }
            });
        }
    }
}