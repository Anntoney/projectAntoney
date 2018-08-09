package com.inducesmile.androidecommerceshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.adapter.NotificationAdapter;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;
import com.inducesmile.androidecommerceshop.utils.Helper;
import com.inducesmile.notification.NotifyObject;

import java.util.List;

public class NotificationFragment extends Fragment {

    private static final String TAG = NotificationFragment.class.getSimpleName();

    private RecyclerView notificationView;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationView = (RecyclerView)view.findViewById(R.id.notification_listing);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationView.setLayoutManager(linearLayoutManager);
        notificationView.setHasFixedSize(true);

        Gson gson = ((CustomApplication)getActivity().getApplication()).getGsonObject();
        String storedNotification = ((CustomApplication)getActivity().getApplication()).getShared().getStoredNotifications();
        if(TextUtils.isEmpty(storedNotification)){
           Helper.displayErrorMessage(getActivity(), "You have not received any notification");
        }
        else{
            List<NotifyObject> updateNotification = gson.fromJson(storedNotification, new TypeToken<List<NotifyObject>>(){}.getType());
            if(updateNotification != null){
                NotificationAdapter mAdapter = new NotificationAdapter(getActivity(), updateNotification);
                notificationView.setAdapter(mAdapter);
            }else{
                Helper.displayErrorMessage(getActivity(), "You have not received any notification");
            }
        }
        return view;
    }
}
