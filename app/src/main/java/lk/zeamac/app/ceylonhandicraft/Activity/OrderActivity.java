package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Adapter.CartViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.MyOrderViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.MyOrderEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView.Adapter orderAdapter;
    private RecyclerView recyclerViewMyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, OrderInfoActivity.class);
                startActivity(intent);
            }
        });

        //     LoadMyOrder
        ArrayList<MyOrderEntity> items = new ArrayList<>();
        items.add(new MyOrderEntity("#18765589","wooden","Wooden","2000","Pending","2023.11.01"));
        items.add(new MyOrderEntity("#18765456","pottery","Pottery","5000","Delivered","2023.11.01"));
        items.add(new MyOrderEntity("#18765752","elephant","Elephant","1000","Pending","2023.10.8"));
        items.add(new MyOrderEntity("#18765589","wooden","Wooden","2000","Pending","2023.11.01"));
        items.add(new MyOrderEntity("#18765456","pottery","Pottery","5000","Delivered","2023.11.01"));
        items.add(new MyOrderEntity("#18765752","elephant","Elephant","1000","Pending","2023.10.8"));
        items.add(new MyOrderEntity("#18765589","wooden","Wooden","2000","Pending","2023.11.01"));
        items.add(new MyOrderEntity("#18765456","pottery","Pottery","5000","Delivered","2023.11.01"));
        items.add(new MyOrderEntity("#18765752","elephant","Elephant","1000","Pending","2023.10.8"));
        recyclerViewMyOrder = findViewById(R.id.myOrderListView);
        recyclerViewMyOrder.setLayoutManager(new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL,false));
        orderAdapter = new MyOrderViewAdapter(items);
        recyclerViewMyOrder.setAdapter(orderAdapter);
        //     LoadMyOrder



    }
}