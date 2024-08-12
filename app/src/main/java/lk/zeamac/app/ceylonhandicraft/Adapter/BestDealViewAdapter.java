package lk.zeamac.app.ceylonhandicraft.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Activity.MainActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.SingleProductViewActivity;
import lk.zeamac.app.ceylonhandicraft.Entity.BestDealEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.ProductEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class BestDealViewAdapter extends RecyclerView.Adapter<BestDealViewAdapter.Viewholder> {
    ArrayList<BestDealEntity> items;
    Context context;

    public BestDealViewAdapter(ArrayList<BestDealEntity> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public BestDealViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_best_deal, parent, false);






        return new BestDealViewAdapter.Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestDealViewAdapter.Viewholder holder, int position) {
        holder.titleText.setText(items.get(position).getTitle());
        int drawableResourcesId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourcesId)
                .into(holder.img);
        holder.dealPrice.setText(items.get(position).getPrice());

        
        holder.singleProductOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductViewActivity.class);
                context.startActivity(intent);
            }


        });

    }


    
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilterList(ArrayList<BestDealEntity> itemsGrid) {
        this.items = itemsGrid;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleText,dealPrice, singleProductOpen;
        ImageView img ;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextDeal);
            img = itemView.findViewById(R.id.imgBestDeal);
            dealPrice = itemView.findViewById(R.id.dealPrice);
            singleProductOpen = itemView.findViewById(R.id.plusSingleProductView);
        }
    }
}
