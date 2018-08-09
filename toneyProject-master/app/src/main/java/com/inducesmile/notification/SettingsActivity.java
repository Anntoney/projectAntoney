package com.inducesmile.notification;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.inducesmile.androidecommerceshop.R;


public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            Preference notificationPref = (SwitchPreference) findPreference("notifications_new_message");

            notificationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isNotificationOn = (Boolean) newValue;
                    if (preference.getKey().equals("notifications_new_message") && isNotificationOn) {
                        // call the push registration for this user

                        FirebaseMessaging.getInstance().subscribeToTopic("products");
                        String token = FirebaseInstanceId.getInstance().getToken();
                        Log.d(TAG, "Token value " + token);
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("products");
                    }
                    return true;
                }
            });
        }
    }
}
