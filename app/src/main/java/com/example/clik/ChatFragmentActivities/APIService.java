package com.example.clik.ChatFragmentActivities;

import com.example.clik.Notifications.MyResponse;
import com.example.clik.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAXgVHawQ:APA91bGqRYSIh0sUVcABrFNsFUlLtoia5ZwIcWxcHgF9-LctdIWaAj6HxgeYoyVQ05B7DjvsesCEryjWaF4iNnad3n9Y3qCt2-shFGSoD06jxV-U5AfCK0spF-N5NbA6vP6p8xxXyATU"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
