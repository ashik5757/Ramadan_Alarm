package com.example.ramadanalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class LauncherActivity extends AppCompatActivity {

    String[] city = {"নওগাঁ", "ঢাকা", "চট্টগ্রাম"};
    private String citySelected = "";
//    Resources res = getResources();
//    String[] city = res.getStringArray(R.array.city_names);
    TextView tv_title;
    TextInputLayout til_select_city;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter_city;
    Button bt_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        decideNavigation();

        Typeface tf_charu_regular = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Regular.ttf");
        Typeface tf_charu_bold = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Bold.ttf");

        tv_title = findViewById(R.id.tv_title);
        tv_title.setTypeface(tf_charu_bold);
        autoCompleteTextView = findViewById(R.id.select_city);
        autoCompleteTextView.setTypeface(tf_charu_regular);
        bt_continue = findViewById(R.id.bt_continue);
        bt_continue.setTypeface(tf_charu_regular);

        til_select_city = findViewById(R.id.tiL_select_city);
        til_select_city.setTypeface(tf_charu_regular);


        arrayAdapter_city = new ArrayAdapter<>(this, R.layout.city_list, city);

        autoCompleteTextView.setAdapter(arrayAdapter_city);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                citySelected = adapterView.getItemAtPosition(position).toString();
                if (citySelected.length()!=0)
                    autoCompleteTextView.setError(null);
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_process();
            }
        });



    }





    private void decideNavigation() {
        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        String defCity = sp.getString("CITY","NONE");
        if (!defCity.equals("NONE")){


////            Toast.makeText(this, String.valueOf(compValue), Toast.LENGTH_SHORT).show();


            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
            String todayDate = dateFormat.format(currentDate);

            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);
            String ifter_time = ramadanTime.getIfterTime();
            String sehri_time = ramadanTime.getSehriTime();
            LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime sTime = LocalTime.parse(sehri_time, DateTimeFormatter.ofPattern("HH:mm:ss"));

            LocalTime currentTime = LocalTime.now();

            LocalTime time_0 = LocalTime.parse("23:59:59", DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime time_01 = LocalTime.parse("00:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
            int comp_time0 = currentTime.compareTo(time_0);
            int comp_time01 = currentTime.compareTo(time_01);


            LocalTime min5_AfterIftar = iTime.plusMinutes(5);
            LocalTime min5_AfterSehri = sTime.plusMinutes(5);
            int compIftar = currentTime.compareTo(min5_AfterIftar);
            int compSehri = currentTime.compareTo(min5_AfterSehri);




            SharedPreferences.Editor e = sp.edit();

            if (compIftar>=0 && comp_time0<0) {
                e.putBoolean("IS_SEHRI_NEXTDAY", true);
                e.commit();
                Intent intent = new Intent(LauncherActivity.this, SehriActivity.class);
                startActivity(intent);
                finish();
            }

            else if (compSehri<0) {
                e.putBoolean("IS_SEHRI_NEXTDAY", false);
                e.commit();
                Intent intent = new Intent(LauncherActivity.this, SehriActivity.class);
                startActivity(intent);
                finish();
            }

            else {
                Intent intent = new Intent(LauncherActivity.this, IfterActivity.class);
                startActivity(intent);
                finish();
            }


        }

    }

    private void continue_process() {
        if(citySelected.length()==0) {
            autoCompleteTextView.setError("অনুগ্রহ করে একটি শহর নির্বাচন করুন৷");
            return;
        }


        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("CITY",citySelected);
        e.putString("BEFORE_SEHRI", "নেই");
        e.putString("BEFORE_IFTAR", "নেই");
        e.putBoolean("AT_SEHRI", false);
        e.putBoolean("AT_IFTAR", false);
        e.commit();

        DatabaseHelper databaseHelper = new DatabaseHelper(LauncherActivity.this);
        databaseHelper.addTime("15-March,2024","18:11:00","16:48:00");
        databaseHelper.addTime("16-March,2024","18:12:00","04:47:00");
        databaseHelper.addTime("17-March,2024","18:12:00","04:46:00");
        databaseHelper.addTime("18-March,2024","18:12:00","04:45:00");
        databaseHelper.addTime("19-March,2024","18:13:00","04:44:00");
        databaseHelper.addTime("20-March,2024","18:13:00","04:43:00");
        databaseHelper.addTime("21-March,2024","18:13:00","04:42:00");
        databaseHelper.addTime("22-March,2024","18:14:00","04:41:00");
        databaseHelper.addTime("23-March,2024","18:14:00","04:40:00");
        databaseHelper.addTime("24-March,2024","18:14:00","04:39:00");
        databaseHelper.addTime("25-March,2024","18:15:00","04:38:00");
        databaseHelper.addTime("26-March,2024","18:15:00","04:36:00");
        databaseHelper.addTime("27-March,2024","18:16:00","04:35:00");
        databaseHelper.addTime("28-March,2024","18:16:00","04:34:00");
        databaseHelper.addTime("29-March,2024","18:17:00","04:33:00");
        databaseHelper.addTime("30-March,2024","18:17:00","04:31:00");
        databaseHelper.addTime("31-March,2024","18:18:00","04:30:00");
        databaseHelper.addTime("01-April,2024","18:18:00","04:29:00");
        databaseHelper.addTime("02-April,2024","18:19:00","04:28:00");
        databaseHelper.addTime("03-April,2024","18:19:00","04:27:00");
        databaseHelper.addTime("04-April,2024","18:19:00","04:26:00");
        databaseHelper.addTime("05-April,2024","18:20:00","04:24:00");
        databaseHelper.addTime("06-April,2024","18:20:00","04:24:00");
        databaseHelper.addTime("07-April,2024","18:21:00","04:23:00");
        databaseHelper.addTime("08-April,2024","18:21:00","04:22:00");
        databaseHelper.addTime("09-April,2024","18:21:00","04:21:00");
        databaseHelper.addTime("10-April,2024","18:22:00","04:20:00");


        Intent intent = new Intent(LauncherActivity.this, IfterActivity.class);
        startActivity(intent);
        finish();

    }






}


