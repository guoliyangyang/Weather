package com.example.hey.weather;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button b_login, b_register, b_imag;
    EditText e_login, e_password;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_login = (Button)findViewById(R.id.login_main);
        b_register = (Button)findViewById(R.id.register_main);
        e_login = (EditText)findViewById(R.id.user_main);
        e_password = (EditText)findViewById(R.id.password_main);
        b_imag = (Button)findViewById(R.id.imag_main);

        final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "User.db", null, 2);

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean permission_user = false, permission_password = false, p_province = false;
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("WEATHER", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do {
                        username = cursor.getString(cursor.getColumnIndex("USER"));
                        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                        String province = cursor.getString(cursor.getColumnIndex("PROVINCE"));
                        if(username.equals(e_login.getText().toString())) {
                            permission_user = true;
                            if(password.equals(e_password.getText().toString())) {
                                permission_password = true;
                            }
                            if(province != null) {
                                p_province = true;
                            }
                        }
                    } while(cursor.moveToNext());
                    if(permission_user) {
                        if(permission_password) {
                            if(p_province) {
                                Intent i = new Intent(MainActivity.this, Weather.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                            else {
                                Intent i = new Intent(MainActivity.this, Location.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Password Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        e_password.setText("");
                        e_login.setText("");
                        Toast.makeText(MainActivity.this, "Username Wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        b_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "there is nothing to show u !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
