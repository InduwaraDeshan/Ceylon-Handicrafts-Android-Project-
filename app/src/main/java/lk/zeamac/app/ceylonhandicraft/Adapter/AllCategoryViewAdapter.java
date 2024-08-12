package lk.zeamac.app.ceylonhandicraft.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Entity.AllCategoryEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class AllCategoryViewAdapter extends RecyclerView.Adapter<AllCategoryViewAdapter.Viewholder> {
        ArrayList<AllCategoryEntity> items;
        Context context;

public AllCategoryViewAdapter(ArrayList<AllCategoryEntity> item) {
        this.items = item;
        }

@NonNull
@Override
public AllCategoryViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_category_with_product, parent, false);


        return new AllCategoryViewAdapter.Viewholder(inflate);
        }

@Override
public void onBindViewHolder(@NonNull AllCategoryViewAdapter.Viewholder holder, int position) {
        holder.categoryTitleText.setText(items.get(position).getTitle());
        int drawableResourcesId = holder.itemView.getResources()
        .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
        .load(drawableResourcesId)
        .into(holder.img);
        holder.categoryPrice.setText(items.get(position).getPrice());
        }

@Override
public int getItemCount() {
        return items.size();
        }

public class Viewholder extends RecyclerView.ViewHolder {
    TextView categoryTitleText,categoryPrice;
    ImageView img;



    public Viewholder(@NonNull View itemView) {
        super(itemView);
        categoryTitleText = itemView.findViewById(R.id.categoryViewTitle);
        img = itemView.findViewById(R.id.imgCategoryAll);
        categoryPrice = itemView.findViewById(R.id.categoryViewPrice);
    }
}
}
