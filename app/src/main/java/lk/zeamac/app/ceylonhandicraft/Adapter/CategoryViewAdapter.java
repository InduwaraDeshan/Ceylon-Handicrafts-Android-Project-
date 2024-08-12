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

import lk.zeamac.app.ceylonhandicraft.Entity.CategoryEntity;
import lk.zeamac.app.ceylonhandicraft.R;

import java.util.ArrayList;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.Viewholder> {
    ArrayList<CategoryEntity> categoryItems;
    Context context;
    private FirebaseStorage storage;

    OnCategoryClickListener onCategoryClickListener;


    public CategoryViewAdapter(ArrayList<CategoryEntity> item,Context context,OnCategoryClickListener categoryClickListener) {
        this.categoryItems = item;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.onCategoryClickListener = categoryClickListener;
    }



    public interface OnCategoryClickListener{
        void OnCategoryClick(String categoryType);
    }




    @NonNull
    @Override
    public CategoryViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_category, parent, false);


        return new Viewholder(inflate,onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewAdapter.Viewholder holder, int position) {



        CategoryEntity category = categoryItems.get(position);
        holder.titleText.setText(category.getName());

        storage.getReference("category-images/"+category.getImagePath())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into(holder.img);
                    }
                });


    }






    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView img;

        public Viewholder(@NonNull View itemView,final OnCategoryClickListener clickListener) {
            super(itemView);
            titleText = itemView.findViewById(R.id.categoryTitle);
            img = itemView.findViewById(R.id.categoryImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){

                        clickListener.OnCategoryClick(categoryItems.get(position).getName());

                    }
                }
            });


        }
    }
}
