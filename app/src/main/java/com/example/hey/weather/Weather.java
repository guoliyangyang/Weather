package com.example.hey.weather;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather extends Activity {

    TextView city_text, min_temp, max_temp, weather_weather, post_time;
    Button rechoose, back;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String response;
                    response = msg.obj.toString();
                    String[] m = response.split(",");
                    city_text.setText(m[0]);
                    min_temp.setText(m[1]);
                    max_temp.setText(m[2]);
                    weather_weather.setText(m[3]);
                    post_time.setText(m[4]);
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city_text = (TextView)findViewById(R.id.cityname_weather);
        min_temp = (TextView)findViewById(R.id.min_temperature);
        max_temp = (TextView)findViewById(R.id.max_temperature);
        weather_weather = (TextView)findViewById(R.id.weather_weather);
        post_time = (TextView)findViewById(R.id.post_time);

        rechoose = (Button)findViewById(R.id.rechoose_weather);
        back = (Button)findViewById(R.id.back_weather);

        weather_read(address_get());

        rechoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Weather.this, Location.class);
                String user = getIntent().getStringExtra("username");
                i.putExtra("username", user);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Weather.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public String address_get() {
        final String user = getIntent().getStringExtra("username");
        final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "User.db", null, 2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("WEATHER", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("USER"));
                String result = cursor.getString(cursor.getColumnIndex("ADDRESS"));
                if(name.equals(user)) {
                    return result;
                }
            } while (cursor.moveToNext());
        }
        return null;
    }

    public void weather_read(final String url_address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    String line;
                    URL url = new URL(url_address);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.75 Safari/537.36");

                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int length = -1;
                    while((length = in.read(buf)) != -1) baos.write(buf, 0, length);
                    parseJSONWithJSONObject(new String(baos.toByteArray(), "UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) conn.disconnect();
                }

            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONObject discussComments = jsonObj.getJSONObject("weatherinfo");
            String city = discussComments.getString("city");
            String temp1 = discussComments.getString("temp1");
            String temp2 = discussComments.getString("temp2");
            String weather = discussComments.getString("weather");
            String post = discussComments.getString("ptime");
            String result = city + "," + temp1 + "," + temp2 + "," + weather + "," + post;
            Message msg = new Message();
            msg.obj = result;
            msg.what = 0;
            handler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
