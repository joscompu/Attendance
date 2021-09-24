package com.osm.attendance.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osm.attendance.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tb_logout);
        setSupportActionBar(toolbar);

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