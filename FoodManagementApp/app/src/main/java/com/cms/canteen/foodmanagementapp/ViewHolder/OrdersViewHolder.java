package com.cms.canteen.foodmanagementapp.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cms.canteen.foodmanagementapp.Model.Request;
import com.cms.canteen.foodmanagementapp.OrdersActivity;
import com.cms.canteen.foodmanagementapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView items;
    public TextView totalPrice;

    public TextView deliveryDate;
    public Spinner status;

    private final List<View.OnClickListener> mListener = new ArrayList<View.OnClickListener>();

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        items = itemView.findViewById(R.id.order_items);
        totalPrice = itemView.findViewById(R.id.order_price);
        status = itemView.findViewById(R.id.order_status);
        deliveryDate = itemView.findViewById(R.id.delivery_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, Request.STATUS_LIST);
        status.setAdapter(adapter);

        // Currently no action on tap of element
        // itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

}
