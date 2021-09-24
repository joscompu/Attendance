package com.osm.attendance.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osm.attendance.R;
import com.osm.attendance.model.Attendance;
import com.osm.attendance.model.Authentication;
import com.osm.attendance.model.AuthenticationData;
import com.osm.attendance.model.Result;
import com.osm.attendance.model.Timetable;
import com.osm.attendance.services.GETHoursDay;
import com.osm.attendance.services.POSTAttendance;
import com.osm.attendance.services.POSTAuthenticate;
import com.osm.attendance.util.Time;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tb_logout);
        setSupportActionBar(toolbar);

        final TextView infoMain = findViewById(R.id.tvInfoMain);

        final TextView hours = findViewById(R.id.tvHoursToday);
        hours.setText(getIntent().getStringExtra("hours"));

        final FloatingActionButton add = findViewById(R.id.btnAddHour);
        final EditText begin = findViewById(R.id.txBegin);
        final EditText finish = findViewById(R.id.txFinish);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHours(begin, finish);

            }
        });

        final Button validate = findViewById(R.id.btValidate);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.100.12:8069/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                POSTAttendance service = retrofit.create(POSTAttendance.class);
                String sessionId = getIntent().getStringExtra("sessionId");
                String checkIn = Time.formatDate(begin.getText().toString());
                String chekOut = Time.formatDate(finish.getText().toString());

                Attendance attendance = new Attendance(checkIn, chekOut, 21, 1);
                Call<Result> repository = service.executeAttendance("session_id=" + sessionId + ";", attendance);
                getResultAttendance(repository, retrofit, infoMain, begin, finish);
            }
        });

    }

    private void getResultAttendance(Call<Result> repository, Retrofit retrofit, TextView infoMain,
                                     TextView begin, TextView finish) {

        repository.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()){
                    begin.setText("");
                    finish.setText("");
                    infoMain.setText("Satisfactory assistance");
                }
                else {
                    infoMain.setText("Error when entering punches");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                infoMain.setText("Error when entering punches");
            }
        });
    }

    private void showHours(EditText begin, EditText finish) {
        Calendar now = Calendar.getInstance();
        String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(now.get(Calendar.MINUTE));
        if (begin.getText().toString().isEmpty()){
            begin.setText(hour);
            return;
        }
        else {
            finish.setText(hour);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}