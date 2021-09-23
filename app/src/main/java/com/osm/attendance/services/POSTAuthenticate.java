package com.osm.attendance.services;

import com.osm.attendance.model.Authentication;
import com.osm.attendance.model.AuthenticationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface POSTAuthenticate {

    @POST("web/session/authenticate")
    Call<AuthenticationData> authenticate(@Body Authentication authentication);
}
