package com.osm.attendance.services;


import com.osm.attendance.model.Timetable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GETHoursDay {

    @GET("attendance/v1/hours/{userId}/{dayWeek}")
    Call<List<Timetable>> getHours(@Header("cookie") String sessionId,
                                   @Path("userId") int userId,
                                   @Path("dayWeek") int dayWeek);
}
