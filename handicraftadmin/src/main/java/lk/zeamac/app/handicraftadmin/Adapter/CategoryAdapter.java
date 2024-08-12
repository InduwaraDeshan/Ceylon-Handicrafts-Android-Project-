package lk.zeamac.app.handicraftadmin.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.zeamac.app.handicraftadmin.Entity.CategoryEntity;
import lk.zeamac.app.handicraftadmin.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryEntity> items;
    private FirebaseStorage storage;
    private Context context;
    private static ProductAdapter.OnProductClickListener onProductClickRemoveListener;
    public CategoryAdapter(ArrayList<CategoryEntity> items, Context context) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    public interface OnProductClickListener {
        void OnProductClick(int position);
    }

    public void setOnProductClickRemoveListener(ProductAdapter.OnProductClickListener clickListener){
        this.onProductClickRemoveListener = clickListener;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryEntity item = items.get(position);
        holder.textName.setText(item.getName());

        storage.getReference("category-images/"+item.getImagePath())
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

        TextView textName ;
        ImageView image,removeProductAdmin;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.categoryTitle);
            image = itemView.findViewById(R.id.categoryImage);


            removeProductAdmin =itemView.findViewById(R.id.textViewCategoryRemoveAdmin);
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
