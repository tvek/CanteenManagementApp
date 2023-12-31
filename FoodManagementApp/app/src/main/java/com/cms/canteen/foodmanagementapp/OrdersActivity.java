package com.cms.canteen.foodmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cms.canteen.foodmanagementapp.Common.Common;
import com.cms.canteen.foodmanagementapp.Database.Database;
import com.cms.canteen.foodmanagementapp.Interface.ItemClickListener;
import com.cms.canteen.foodmanagementapp.Model.Food;
import com.cms.canteen.foodmanagementapp.Model.Order;
import com.cms.canteen.foodmanagementapp.Model.Request;
import com.cms.canteen.foodmanagementapp.ViewHolder.CartAdapter;
import com.cms.canteen.foodmanagementapp.ViewHolder.FoodViewHolder;
import com.cms.canteen.foodmanagementapp.ViewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdersActivity extends AppCompatActivity {

    private static final String TAG = "OrdersActivity";
    FirebaseDatabase database;
    DatabaseReference requests;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrdersViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrdersViewHolder>(Request.class,R.layout.orders_list_item,
                OrdersViewHolder.class,requests.orderByChild("phone").equalTo(Common.currentUser.getPhone()))
                //is like select query
        {
            @Override
            protected void populateViewHolder(OrdersViewHolder viewHolder, Request model, int position) {
                viewHolder.totalPrice.setText(model.getTotal());
                String ordersData = "";
                if(null != model.getFoods()) {
                    for(Order order : model.getFoods()) {
                        ordersData += order.getProductName() + " - " + order.getQuantity() + "\n";
                    }
                } else {
                    ordersData = "No Items were present";
                }
                viewHolder.items.setText(ordersData);

            }
        };
        Log.d("TAG",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }

}