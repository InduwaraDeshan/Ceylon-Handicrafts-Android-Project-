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

import lk.zeamac.app.ceylonhandicraft.Entity.WishListEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class WishListViewAdapter extends RecyclerView.Adapter<WishListViewAdapter.Viewholder> {
    ArrayList<WishListEntity> items;
    Context context;

    public WishListViewAdapter(ArrayList<WishListEntity> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public WishListViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_favorite, parent, false);


        return new WishListViewAdapter.Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewAdapter.Viewholder holder, int position) {
        holder.titleText.setText(items.get(position).getTitle());
        int drawableResourcesId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getImagePath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourcesId)
                .into(holder.img);
        holder.price.setText(items.get(position).getPrice());


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleText, price;
        ImageView img;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewWishListProductTitle);
            img = itemView.findViewById(R.id.imageViewWishListImage);
            price = itemView.findViewById(R.id.textViewWishListPrice);

        }
    }
}
