package com.example.hey.weather;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Location extends Activity {

    int province_pos = 0, city_pos = 0, county_pos = 0;
    Spinner s_province, s_city, s_county;
    Button b_ok;
    String province_name, city_name, county_name;
    String[] province_pos1 = new String[1000];
    String[] city_pos1 = new String[1000];
    String[] county_pos1 = new String[1000];
    String line;
    String weather_address;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String response;
            switch(msg.what) {
                case 0:
                    response = (String)msg.obj;
                    spinner_set(0, response);
                    break;
                case 1:
                    response = (String)msg.obj;
                    spinner_set(1, response);
                    break;
                case 2:
                    response = (String)msg.obj;
                    spinner_set(2, response);
                    break;
                case 3:
                    response = (String)msg.obj;
                    spinner_set(3, response);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        final String user = getIntent().getStringExtra("username");

        s_province = (Spinner) findViewById(R.id.province_location);
        s_city = (Spinner)findViewById(R.id.city_location);
        s_county = (Spinner)findViewById(R.id.county_location);
        b_ok = (Button) findViewById(R.id.ok_location);

        final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "User.db", null, 2);

        location_read(0, "http://www.weather.com.cn/data/list3/city.xml");

        b_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values1 = new ContentValues(), values2 = new ContentValues(), values3 = new ContentValues(), values4 = new ContentValues();
                values1.put("PROVINCE", province_name);
                values2.put("CITY", city_name);
                values3.put("COUNTY", county_name);
                values4.put("ADDRESS", weather_address);
                db.update("WEATHER", values1, "USER = ?", new String[]{user});
                db.update("WEATHER", values2, "USER = ?", new String[]{user});
                db.update("WEATHER", values3, "USER = ?", new String[]{user});
                db.update("WEATHER", values4, "USER = ?", new String[]{user});

                Intent i = new Intent(Location.this, Weather.class);
                String user = getIntent().getStringExtra("username");
                i.putExtra("username", user);
                startActivity(i);
            }
        });
    }

    public void location_read(final int n, final String urladdress) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(urladdress);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) response.append(line);

                    Message message = new Message();
                    message.what = n;
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) conn.disconnect();
                }

            }
        }).start();

    }

    public void spinner_set(int n, String response) {
        int num = 0;
        if(n != 3) parse_split(n, response);
        if(n == 0) {

            while(province_pos1[num++] != null) {}
            num--;
            String[] mar = new String[num];
            for (int i = 0; province_pos1[i] != null; i++) mar[i] = province_pos1[i];
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mar);
            s_province.setAdapter(arrayAdapter);

            s_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    province_name = province_pos1[position];
                    province_pos = position + 1;
                    String address;
                    if(province_pos < 10 && province_pos >= 0) {
                        address = "http://www.weather.com.cn/data/list3/city" + "0" + province_pos + ".xml";
                    }
                    else {
                        address = "http://www.weather.com.cn/data/list3/city" + province_pos + ".xml";
                    }

                    location_read(1, address);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        if(n == 1) {
            while(city_pos1[num++] != null) {}
            num--;
            String[] mar = new String[num];
            for (int i = 0; city_pos1[i] != null; i++) mar[i] = city_pos1[i];
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mar);
            s_city.setAdapter(arrayAdapter);
            s_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    city_name = city_pos1[position];
                    city_pos = position + 1;
                    String address, mark;
                    if(province_pos >= 0 && province_pos < 10) mark = "0" + province_pos;
                    else mark = "" + province_pos;
                    if(city_pos >= 0 && city_pos < 10) mark += "0" + city_pos;
                    else mark += city_pos;
                    address = "http://www.weather.com.cn/data/list3/city" + mark + ".xml";

                    location_read(2, address);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        if(n == 2) {
            while(county_pos1[num++] != null) {}
            num--;
            String[] mar = new String[num];
            for (int i = 0; county_pos1[i] != null; i++) mar[i] = county_pos1[i];
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mar);
            s_county.setAdapter(arrayAdapter);
            s_county.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    county_pos = position + 1;
                    county_name = county_pos1[position];
                    String address, mark;
                    if(province_pos >= 0 && province_pos < 10) mark = "0" + province_pos;
                    else mark = "" + province_pos;
                    if(city_pos >= 0 && city_pos < 10) mark += "0" + city_pos;
                    else mark += city_pos;
                    if(county_pos >= 0 && county_pos < 10) mark += "0" + county_pos;
                    else mark += county_pos;
                    address = "http://www.weather.com.cn/data/list3/city" + mark + ".xml";

                    location_read(3, address);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        if(n == 3) {
            String[] m = response.split("\\|");
            weather_address = "http://www.weather.com.cn/data/cityinfo/" + m[1] + ".html";
        }
    }

    public void parse_split(int n, String result) {
        String[] m = result.split(",");
        int k = 0;
        switch (n) {
            case 1: while(city_pos1[k] != null) city_pos1[k++] = null;
            case 2: while(county_pos1[k] != null) county_pos1[k++] = null;
        }
        for (int i = 0; i < m.length; i++) {
            String[] mark = m[i].split("\\|");
            if (mark[1] != null) {
                if(n == 0) province_pos1[i] = mark[1];
                if(n == 1) city_pos1[i] = mark[1];
                if(n == 2) county_pos1[i] = mark[1];
            }
        }
    }

}
