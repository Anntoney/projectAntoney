package com.inducesmile.androidecommerceshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.inducesmile.notification.NotifyObject;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

    private Context context;
    private List<NotifyObject> notificationList;

    public NotificationAdapter(Context context, List<NotifyObject> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, final int position) {
        NotifyObject nObject = notificationList.get(position);
        holder.notificationName.setText(nObject.getTitle());
        holder.notificationDescription.setText(nObject.getContent());

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notificationList.remove(position);
                Gson gson = ((CustomApplication)context.getApplicationContext()).getGsonObject();
                String listNotification = gson.toJson(notificationList);
                ((CustomApplication)context.getApplicationContext()).getShared().storeNotification(listNotification);
                notifyDataSetChanged();
                return false;
            }
        });

        Date notificationDate = Helper.convertStringToDate(nObject.getDate());
        long diffDate = Helper.getDifferenceDays(notificationDate, new Date());
        if(diffDate <= 1){
            holder.notificationDuration.setText("Today");
        }else{
            holder.notificationDuration.setText("Since " + diffDate + " days ago");
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
