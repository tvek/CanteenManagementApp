package com.cms.canteen.foodmanagementapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cms.canteen.foodmanagementapp.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView items;
    public TextView totalPrice;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        items = (TextView)itemView.findViewById(R.id.order_items);
        totalPrice = (TextView) itemView.findViewById(R.id.order_price);

        // Currently no action on tap of element
        // itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
