package com.cms.canteen.foodmanagementapp;

import static com.cms.canteen.foodmanagementapp.Common.Common.ADMIN_ORDER_INTENT_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cms.canteen.foodmanagementapp.Common.Common;
import com.cms.canteen.foodmanagementapp.Database.Database;
import com.cms.canteen.foodmanagementapp.Interface.ItemClickListener;
import com.cms.canteen.foodmanagementapp.Model.Food;
import com.cms.canteen.foodmanagementapp.Model.Order;
import com.cms.canteen.foodmanagementapp.Model.Request;
import com.cms.canteen.foodmanagementapp.Model.User;
import com.cms.canteen.foodmanagementapp.ViewHolder.CartAdapter;
import com.cms.canteen.foodmanagementapp.ViewHolder.FoodViewHolder;
import com.cms.canteen.foodmanagementapp.ViewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrdersActivity extends AppCompatActivity {

    private static final String TAG = "OrdersActivity";
    FirebaseDatabase database;
    DatabaseReference requests;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrdersViewHolder> adapter;

    Boolean isAdminPageView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        isAdminPageView = getIntent().getBooleanExtra(ADMIN_ORDER_INTENT_KEY, false);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
        addSwipeToDeleteOrders();
    }

    void addSwipeToDeleteOrders(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                    final int fromPos = viewHolder.getAdapterPosition();
//                    final int toPos = viewHolder.getAdapterPosition();
//                    // move item in `fromPos` to `toPos` in adapter.
                return true;// true if moved, false otherwise
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                adapter.getRef(viewHolder.getLayoutPosition()).removeValue();
                //adapter.notifyItemRemoved(viewHolder.getLayoutPosition());

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadOrders() {
        Query query;
        if ((User.USER_TYPE_ADMIN.equals(Common.currentUser.getUsertype()))
                && (isAdminPageView)) {
            query = requests;
        } else {
            query = requests.orderByChild("phone").equalTo(Common.currentUser.getPhone());
        }
        adapter = new FirebaseRecyclerAdapter<Request, OrdersViewHolder>(Request.class,R.layout.orders_list_item,
                OrdersViewHolder.class,query)
                //is like select query
        {
            @Override
            protected void populateViewHolder(OrdersViewHolder viewHolder, Request model, int position) {
                viewHolder.totalPrice.setText(model.getTotal());
                viewHolder.deliveryDate.setText(model.getAddress());

                // Configure Status
                viewHolder.status.setEnabled((User.USER_TYPE_ADMIN.equals(Common.currentUser.getUsertype()))
                        && (isAdminPageView));
                viewHolder.status.setSelection(Request.STATUS_LIST.indexOf(model.getStatus()));
                viewHolder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                        model.setStatus(Request.STATUS_LIST.get(pos));
                        adapter.getRef(position).setValue(model);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                // Update Product and Quantity
                String ordersData = "";
                if(null != model.getFoods()) {
                    for(Order order : model.getFoods()) {
                        ordersData += order.getProductName() + " - " + order.getQuantity() + "\n";
                    }
                } else {
                    ordersData = "No Items were present \n";
                }
                ordersData = ordersData.substring(0, ordersData.length() - 1);
                viewHolder.items.setText(ordersData);

            }
        };
        Log.d("TAG",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }

}