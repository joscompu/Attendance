package com.osm.attendance.services;

import com.osm.attendance.model.Attendance;
import com.osm.attendance.model.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface POSTAttendance {

    @POST("attendance/v1")
    Call<Result> executeAttendance(@Header("cookie") String sessionId,
                                   @Body Attendance attendance);
}
