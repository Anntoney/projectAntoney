package com.inducesmile.notification;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class NotificationJobService extends JobService{

    private static final String TAG = NotificationJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "Job dispatcher is completed");
        return false;
    }
}
