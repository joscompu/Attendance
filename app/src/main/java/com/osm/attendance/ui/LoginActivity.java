package com.osm.attendance.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.osm.attendance.R;
import com.osm.attendance.model.Authentication;
import com.osm.attendance.model.AuthenticationData;
import com.osm.attendance.model.Timetable;
import com.osm.attendance.services.GETHoursDay;
import com.osm.attendance.services.POSTAuthenticate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.txUsername);
        final EditText password = findViewById(R.id.txPassword);
        final Button btLogin = findViewById(R.id.btLogin);
        final TextView info = findViewById(R.id.tvInfo);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.100.12:8069/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                POSTAuthenticate service = retrofit.create(POSTAuthenticate.class);
                Authentication authentication = new
                        Authentication("2.0", new
                        Authentication.Params("odoo_osm",
                        username.getText().toString(),
                        password.getText().toString()));
                Call<AuthenticationData> repository = service.authenticate(authentication);
                login(repository, retrofit, info);
            }
        });
    }

    private void login(Call<AuthenticationData> repository, Retrofit retrofit, TextView info) {

        repository.enqueue(new Callback<AuthenticationData>() {
            @Override
            public void onResponse(Call<AuthenticationData> call, Response<AuthenticationData> response) {
                if (response.isSuccessful()){
                    AuthenticationData authenticationData = response.body();
                    if (authenticationData.result != null){
                        String sessionId = response
                                .headers()
                                .values("Set-Cookie")
                                .get(0)
                                .split(";")[0]
                                .split("=")[1];
                        GETHoursDay service = retrofit.create(GETHoursDay.class);
                        Call<List<Timetable>> repository = service
                                .getHours(
                                "session_id=" + sessionId + ";"
                                , authenticationData.result.uid,
                                0);
                        getHours(repository, sessionId, authenticationData, info);
                    }
                    else {
                        info.setText("username/password incorrect");
                    }
                }
                else {
                    info.setText("username/password incorrect");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationData> call, Throwable t) {
                info.setText("Authentication error try later");
            }
        });
    }

    private void getHours(Call<List<Timetable>> repository, String sessionId, AuthenticationData authenticationData, TextView info) {

        repository.enqueue(new Callback<List<Timetable>>() {
            @Override
            public void onResponse(Call<List<Timetable>> call, Response<List<Timetable>> response) {
                if (response.isSuccessful()){

                    List<Timetable> times = response.body();
                    if (times != null){
                        String hours = "";
                        for (Timetable t: times){
                            hours += t.hour_from + "-" + t.hour_to + " y de ";

                        }
                        hours += ".";
                        hours = hours.replace(" y de .", "");
                        Intent intent = new Intent(
                                LoginActivity.this, MainActivity.class);
                        intent.putExtra("session_id", sessionId);
                        intent.putExtra("uid", authenticationData.result.uid);
                        intent.putExtra("hours", hours);
                        startActivity(intent);
                    }
                    else {
                        info.setText("Error getting schedule");
                    }
                }
                else {
                    info.setText("It has not been possible to obtain the schedule");
                }
            }

            @Override
            public void onFailure(Call<List<Timetable>> call, Throwable t) {
                info.setText("It has not been possible to obtain the schedule");
            }
        });
    }
}