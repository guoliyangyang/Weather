package com.example.hey.weather;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TextActivity extends Activity {

    Spinner s_province;
    Button b_ok;
    int province_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        s_province = (Spinner) findViewById(R.id.province_location);
        b_ok = (Button) findViewById(R.id.ok_location);
        String[] mar = {"asdfas", "asdgds", "dfgdrt"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mar);
        s_province = (Spinner)findViewById(R.id.province_text);
        s_province.setAdapter(arrayAdapter);
        s_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province_pos = position;
                Toast.makeText(TextActivity.this, "1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
