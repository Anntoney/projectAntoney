package com.inducesmile.androidecommerceshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inducesmile.androidecommerceshop.EditProfileActivity;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.entity.UserObject;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");

        UserObject user = ((CustomApplication)getActivity().getApplication()).getLoginUser();

        TextView fullnames = (TextView)view.findViewById(R.id.profile_name);
        fullnames.setText(user.getFullname());
        TextView address = (TextView)view.findViewById(R.id.profile_address);
        address.setText(user.getAddress());

        Button editProfileButton = (Button)view.findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(editIntent);
            }
        });

        return view;
    }

}
