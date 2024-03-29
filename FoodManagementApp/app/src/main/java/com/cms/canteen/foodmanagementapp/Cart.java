package com.cms.canteen.foodmanagementapp;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.canteen.foodmanagementapp.Common.Common;
import com.cms.canteen.foodmanagementapp.Database.Database;
import com.cms.canteen.foodmanagementapp.Model.Order;
import com.cms.canteen.foodmanagementapp.Model.Request;
import com.cms.canteen.foodmanagementapp.ViewHolder.CartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;


    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    EditText edtAddress;

    void addSwipeToDelete(){
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
                Order currentElement = cart.get(viewHolder.getLayoutPosition());
                new Database(Cart.this).removeFromCart(currentElement);
                cart.remove(viewHolder.getLayoutPosition());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                updateTotalPrice();
                
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        loadListFood();
        updateTotalPrice();
        addSwipeToDelete();
    }

    private void showAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
            builder.setTitle("One More Step!");
            builder.setMessage("Enter Date of Collection: (DD/MM/YYYY)");

            edtAddress = new EditText(Cart.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            edtAddress.setLayoutParams(lp);
            builder.setView(edtAddress);
            builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //create new request

                    String orderDate = getCurrentDate();
                    Query orderDateQuery = requests.orderByChild("orderDate").equalTo(orderDate.toString());
                    orderDateQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            orderDateQuery.removeEventListener(this);
                            if (50 <= dataSnapshot.getChildrenCount()){
                                String message = "Current " + String.valueOf(dataSnapshot.getChildrenCount()) + "/50. 50 orders are only allowed per day. Please book on the next day.";
                                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                builder.setTitle("Today's Booking is FULL!");
                                builder.setMessage(message);
                                builder.show();
                            } else {
                                //requestPayment();
                                finishOrderPlacement();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
            builder.show();
        }

    private void requestPayment() {
        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "thomasvml@okhdfcbank")
                        .appendQueryParameter("pn", "Thomas Easo")
                        .appendQueryParameter("mc", "your-merchant-code")
                        .appendQueryParameter("tr", "your-transaction-ref-id")
                        .appendQueryParameter("tn", "App Testing")
                        .appendQueryParameter("am", txtTotalPrice.getText().toString())
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    private String getCurrentDate() {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return df.format(c);
        }
    private void finishOrderPlacement() {

        String orderDate = getCurrentDate();
        Request request = new Request(
                Common.currentUser.getPhone(),
                Common.currentUser.getName(),
                edtAddress.getText().toString(),
                txtTotalPrice.getText().toString(),
                cart,
                orderDate.toString()
        );
        //submit to firebase
        //currentMilli to key
        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
        //delete cart
        new Database(getBaseContext()).cleanCart();
        Toast.makeText(Cart.this, "Your  Order has been Confirmed!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

    }

    private void updateTotalPrice() {
        //total price
        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }
}
