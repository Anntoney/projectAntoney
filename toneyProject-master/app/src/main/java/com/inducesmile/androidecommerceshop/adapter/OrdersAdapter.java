package com.inducesmile.androidecommerceshop.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.OrdersObject;
import com.inducesmile.androidecommerceshop.utils.Helper;

import java.util.Date;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersViewHolder>{

    private Context context;
    private List<OrdersObject> ordersList;

    public OrdersAdapter(Context context, List<OrdersObject> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        OrdersObject ordersObject = ordersList.get(position);

        holder.productName.setText("ORDER NUMBER " + ordersObject.getId());
        holder.productStatus.setText(ordersObject.getStatus());
        Date formatDate = Helper.convertStringToDate(ordersObject.getCreated_at());
        String date = Helper.convertDateToString(formatDate);
        holder.productOrderDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

}
