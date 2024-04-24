package com.example.ramadanalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class IfterActivity extends AppCompatActivity implements View.OnClickListener {


    String[] city = {"Naogaon", "Dhaka", "Chittagong"};

    Button bt_city;
    ImageButton bt_settings;
    MaterialSwitch switch_theme;
    TextView tv_date, tv_ifter_time,tv_ifter_time_title, tv_rem_time, tv_rem_time_title;
    String ifter_time, sehri_time;
    Handler handler;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifter);
        this.savedInstanceState = savedInstanceState;

        decideNavigation();

        bt_city = findViewById(R.id.bt_city);
        bt_settings = findViewById(R.id.bt_settings);
        switch_theme = findViewById(R.id.switch_theme);

        Typeface tf_charu_regular = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Regular.ttf");
        Typeface tf_charu_bold = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Bold.ttf");

        tv_date = findViewById(R.id.tv_date);
        tv_date.setTypeface(tf_charu_regular);

        tv_ifter_time_title = findViewById(R.id.tv_iftar_time_title);
        tv_ifter_time = findViewById(R.id.tv_iftar_time);
        tv_ifter_time_title.setTypeface(tf_charu_bold);
        tv_ifter_time.setTypeface(tf_charu_regular);

        tv_rem_time_title = findViewById(R.id.tv_rem_time_title);
        tv_rem_time = findViewById(R.id.tv_rem_time);
        tv_rem_time_title.setTypeface(tf_charu_bold);
        tv_rem_time.setTypeface(tf_charu_regular);

        initialProcess();

        bt_city.setOnClickListener(this);
        bt_settings.setOnClickListener(this);
        switch_theme.setOnClickListener(this);

        handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

//                Date currentDate = Calendar.getInstance().getTime();
//                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//                String timeFormatted = timeFormat.format(currentDate);


                LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                LocalTime currentTime = LocalTime.now();

                LocalTime diff = iTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());
//                LocalTime diff = iTime.minusMinutes(currentTime.getMinute());


                if (!currentTime.isBefore(iTime)) {
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_20dp = (int) (20 * scale + 0.5f);

                    tv_rem_time_title.setText("ইফতারের দোয়া");
                    tv_rem_time.setText("আল্লাহুম্মা লাকা ছুমতু ওয়া আলা রিযক্বিকা ওয়া আফতারতু বিরাহমাতিকা ইয়া আরহামার রাহিমিন।");
                    tv_rem_time.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    tv_rem_time.setPadding(padding_20dp,padding_20dp,padding_20dp,padding_20dp);
                }


                else {
                    String hr = String.valueOf(diff.getHour());
                    hr = numberConvertor(hr);
                    String min = String.valueOf(diff.getMinute());
                    min = numberConvertor(min);
                    String sec = String.valueOf(diff.getSecond());
                    sec = numberConvertor(sec);


                    if (diff.getHour()==0 && diff.getMinute()==0)
                        tv_rem_time.setText(sec+" সেকেন্ড");
                    else if (diff.getHour()==0)
                        tv_rem_time.setText(min+" মিনিট "+sec+" সেকেন্ড");
                    else
                        tv_rem_time.setText(hr+" ঘন্টা "+min+" মিনিট\n"+sec+" সেকেন্ড");
                }


                handler.postDelayed(this,0);
            }
        };
        handler.postDelayed(runnable,0);

    }

    private void decideNavigation() {



    }


    private void initialProcess() {

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        String defCity = sp.getString("CITY","NONE");
        boolean dark_theme = sp.getBoolean("DARK_THEME", false);

        if (dark_theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switch_theme.setChecked(true);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switch_theme.setChecked(false);
        }


        if (!defCity.equals("NONE")) {
            bt_city.setText(defCity);

            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
            SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");

            String todayDate = dateFormat.format(currentDate);
            String weekday = weekFormat.format(currentDate);

            String dateText = dateConvertor(todayDate,weekday);
//            Toast.makeText(this,dateText,Toast.LENGTH_LONG).show();
            tv_date.setText(dateText);


            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);

            if (ramadanTime!=null) {

                String hr = "";
                String min = "";

                if (defCity.equals("নওগাঁ")) {
                    ifter_time = ramadanTime.getIfterTime();
                    LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    LocalTime updatedTime = iTime.plusMinutes(6);
                    ifter_time = updatedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    updatedTime = updatedTime.minusHours(12);
                    hr = String.valueOf(updatedTime.getHour());
                    min = String.valueOf(updatedTime.getMinute());
                }

                else if (defCity.equals("চট্টগ্রাম")) {
                    ifter_time = ramadanTime.getIfterTime();
                    LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    LocalTime updatedTime = iTime.minusMinutes(6);
                    ifter_time = updatedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    updatedTime = updatedTime.minusHours(12);
                    hr = String.valueOf(updatedTime.getHour());
                    min = String.valueOf(updatedTime.getMinute());
                }

                else {

                    ifter_time = ramadanTime.getIfterTime();
                    LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));

                    iTime = iTime.minusHours(12);
                    hr = String.valueOf(iTime.getHour());
                    min = String.valueOf(iTime.getMinute());
                    //String sehri_time = databaseHelper.getTodaysTime(dateFormatted).getSehriTime();
                }

                hr = numberConvertor(hr);
                min = numberConvertor(min);
                tv_ifter_time.setText(hr+" টা "+min+" মিনিট");


                sehri_time = ramadanTime.getSehriTime();

            }

        }

    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_city)
            showPopMenu();
        if (v.getId()==R.id.bt_settings){
            Intent intent = new Intent(IfterActivity.this, Settings.class);
            intent.putExtra("SEHRI_TIME", sehri_time);
            intent.putExtra("IFTAR_TIME", ifter_time);
            startActivity(intent);
        }
        if (v.getId()==R.id.switch_theme)
            themeChangeProcess();

    }

    private void themeChangeProcess() {
        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        if (switch_theme.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            e.putBoolean("DARK_THEME", true);
            e.commit();
        }

        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            e.putBoolean("DARK_THEME", false);
            e.commit();
        }

    }

    private void showPopMenu() {
        PopupMenu popupMenu = new PopupMenu(IfterActivity.this, bt_city);
        popupMenu.getMenuInflater().inflate(R.menu.popup_city, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bt_city.setText(item.getTitle());
                SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.putString("CITY", (String) item.getTitle());
//                Toast.makeText(IfterActivity.this, String.valueOf(R.id.naogaon),Toast.LENGTH_SHORT).show();
                e.commit();
//                onCreate(savedInstanceState);
                initialProcess();
                return true;
            }
        });
        popupMenu.show();

    }

    private String getCityTitle(long itemId) {

        if (itemId==R.id.naogaon)
            return "নওগাঁ";
        else if (itemId==R.id.dhaka)
            return "ঢাকা";
        else if (itemId==R.id.chittagong)
            return "চট্টগ্রাম";

        return "NONE";
    }



    private String citYToBangla(String city){
        city = city.replace("Naogaon", "নওগাঁ");
        city = city.replace("Dhaka", "ঢাকা");
        city = city.replace("Chittagong", "চট্টগ্রাম");
        return city;
    }

    private String numberConvertor(String digit){
        digit = digit.replace("0","০");
        digit = digit.replace("1","১");
        digit = digit.replace("2","২");
        digit = digit.replace("3","৩");
        digit = digit.replace("4","৪");
        digit = digit.replace("5","৫");
        digit = digit.replace("6","৬");
        digit = digit.replace("7","৭");
        digit = digit.replace("8","৮");
        digit = digit.replace("9","৯");
        return digit;
    }
    private String dateConvertor(String date, String weekday){

        date = date.replace("0","০");
        date = date.replace("1","১");
        date = date.replace("2","২");
        date = date.replace("3","৩");
        date = date.replace("4","৪");
        date = date.replace("5","৫");
        date = date.replace("6","৬");
        date = date.replace("7","৭");
        date = date.replace("8","৮");
        date = date.replace("9","৯");

        date = date.replace("January","জানুয়ারি");
        date = date.replace("February","ফেব্রুয়ারি");
        date = date.replace("March","মার্চ");
        date = date.replace("April","এপ্রিল");
        date = date.replace("May","মে");
        date = date.replace("June","জুন");
        date = date.replace("July","জুলাই");
        date = date.replace("August","আগস্ট");
        date = date.replace("September","সেপ্টেম্বর");
        date = date.replace("October","অক্টোবর");
        date = date.replace("November","নভেম্বর");
        date = date.replace("December","ডিসেম্বর");

        weekday = weekday.replace("Sunday", "রবিবার");
        weekday = weekday.replace("Monday", "সোমবার");
        weekday = weekday.replace("Tuesday", "মঙ্গলবার");
        weekday = weekday.replace("Wednesday", "বুধবার");
        weekday = weekday.replace("Thursday", "বৃহস্পতিবার");
        weekday = weekday.replace("Friday", "শুক্রবার");
        weekday = weekday.replace("Saturday", "শনিবার");

        return date+"\n"+weekday;
    }



}