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

import lk.zeamac.app.ceylonhandicraft.Entity.MyOrderEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class MyOrderViewAdapter extends RecyclerView.Adapter<MyOrderViewAdapter.Viewholder> {
    ArrayList<MyOrderEntity> items;
    Context context;

    public MyOrderViewAdapter(ArrayList<MyOrderEntity> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public MyOrderViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_order, parent, false);


        return new MyOrderViewAdapter.Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewAdapter.Viewholder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        int drawableResourcesId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourcesId)
                .into(holder.img);
        holder.price.setText(items.get(position).getPrice());
        holder.status.setText(items.get(position).getStatus());
        holder.id.setText(items.get(position).getId());
        holder.date.setText(items.get(position).getDate());


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, price, status,id,date;
        ImageView img;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.order_list_img);
            title = itemView.findViewById(R.id.my_order_list_title);
            price= itemView.findViewById(R.id.order_list_price);
            status = itemView.findViewById(R.id.order_list_status);
            id = itemView.findViewById(R.id.order_list_id);
            date = itemView.findViewById(R.id.order_list_date);
        }
    }
}
